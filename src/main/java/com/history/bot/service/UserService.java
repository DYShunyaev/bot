package com.history.bot.service;

import com.history.bot.model.SentMessages;
import com.history.bot.model.User;
import com.history.bot.repository.SentMessagesRepository;
import com.history.bot.repository.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SentMessagesRepository sendMessagesRepository;

    @Autowired
    public UserService(UserRepository userRepository, SentMessagesRepository sendMessagesRepository) {
        this.userRepository = userRepository;
        this.sendMessagesRepository = sendMessagesRepository;
    }

    public void registerUser(Message msg) {
        if (userRepository.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();
            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            log.info("User save: " + user);
        }
    }

    boolean checkSentMessages(Long chatId) {
        return sendMessagesRepository.findByUserChatId(chatId).isEmpty();
    }


    void updateUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    String deleteUserDate(long chatId, String name) {
        userRepository.deleteById(chatId);
        String answer = EmojiParser.parseToUnicode(name + ", все удалено." + " :smirk:");
        log.info("Delete data from " + name);
        return answer;
    }

    List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    List<SentMessages> sentMessagesByUserId(Long userId) {
        return sendMessagesRepository.findByUserChatId(userId);
    }

    @Transactional
    User findUserByChatId(long chatId) {
        return userRepository.findById(chatId).orElseThrow();
    }

    void safeSendMessage(SentMessages sentMessages) {
        sendMessagesRepository.save(sentMessages);
    }
}
