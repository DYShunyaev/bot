package com.history.bot.service;

import com.history.bot.config.BotConfig;
import com.history.bot.model.DateForSendMessages;
import com.history.bot.model.SentMessages;
import com.history.bot.model.User;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot  {
    private final BotConfig config;

    @Autowired
    private UserService userService;

    public TelegramBot(BotConfig config) {
        this.config = config;
        initTask();
        List<BotCommand> myCommands = new ArrayList<>();
        myCommands.add(new BotCommand("/start", "Start this bot!"));
        myCommands.add(new BotCommand("/mydata", "Какие твои данные записаны в боте."));
        myCommands.add(new BotCommand("/deletedata", "Удаляет твои данные,"));
        myCommands.add(new BotCommand("/help", "Информация о том, что умеет этот бот."));
        try {
            this.execute(new SetMyCommands(myCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start" -> {
                    userService.registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    if (userService.checkSentMessages(chatId)) sendMessageBeforeThisTime(chatId);
                }
                case "/mydata" -> sendMessage(chatId, userService.findUserByChatId(chatId).toString());
                case "/deletedata" ->
                        sendMessage(chatId, userService.deleteUserDate(chatId, update.getMessage().getChat().getFirstName()));
                case "/help" -> {
                    try {
                        sendHelpMessage(chatId);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                default -> sendMessage(chatId, "Прости, но эта команда еще не добавлена.");
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        log.info("Replied to user " + name);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(EmojiParser.parseToUnicode("Привет " + name + ", рад видеть тебя!" + " :relaxed:"));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private static InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        var buttonGetMyAccounts = new InlineKeyboardButton();
        buttonList.add(buttonGetMyAccounts);
        rowsInline.add(buttonList);
        markupInLine.setKeyboard(rowsInline);
        return markupInLine;
    }

    private void sendHelpMessage(Long chatId) throws IOException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(Utility.readFile(Paths.get
                ("D:/Java/telegram_bot_money_spent_counter/telegram_bot_money_spent_counter/help.txt"))
                .toString());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendMessageBeforeThisTime(Long chatId) {
        for (DateForSendMessages sendMessages: DateForSendMessages.values()) {
            if (sendMessages.getTimestamp().before(new Timestamp(System.currentTimeMillis()))) {
                String text = Utility.readFile(sendMessages.getPathToFile()).toString();
                sendMessage(chatId, text);
                userService.safeSendMessage(new SentMessages(userService.findUserByChatId(chatId),
                        new Timestamp(System.currentTimeMillis()), sendMessages.getTimestamp().toLocalDateTime().toLocalDate()));
            };
        }
    }

    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    public void sendMessagesFromUsersProcess(LocalDate localDate) {
        DateForSendMessages dateMess = Arrays.stream(DateForSendMessages.values())
                .filter(dateForSendMessages -> dateForSendMessages.getTimestamp().toLocalDateTime().toLocalDate().equals(localDate))
                .findFirst()
                .orElseThrow();
        List<User> users = userService.getAllUsers();
        for (User user: users) {
            var messages = userService.sentMessagesByUserId(user.getChatId())
                    .stream()
                    .filter(mess -> mess.getTimestampMessage().equals(dateMess.getTimestamp().toLocalDateTime().toLocalDate()))
                    .toList();
            if (messages.isEmpty()) {
                String text = Utility.readFile(dateMess.getPathToFile()).toString();
                sendMessage(user.getChatId(), text);
                userService.safeSendMessage(new SentMessages(user,
                        new Timestamp(System.currentTimeMillis()), dateMess.getTimestamp().toLocalDateTime().toLocalDate()));
            }
        }

    }

    public void initTask() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        LocalTime timeToRun = LocalTime.of(15, 38);

        long initialDelay = calculateInitialDelay(timeToRun);

        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Задача выполняется в: " + LocalTime.now());
            // Здесь можно вызвать нужный метод
            sendMessagesFromUsersProcess(LocalDate.now());
        }, initialDelay, 2 * 60 * 60, TimeUnit.SECONDS); // Повторяем каждый день
    }

    private static long calculateInitialDelay(LocalTime timeToRun) {
        LocalTime now = LocalTime.now();
        long delay = timeToRun.toSecondOfDay() - now.toSecondOfDay();
        if (delay < 0) {
            delay += 24 * 60 * 60;
        }
        return delay;
    }
    

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }


}
