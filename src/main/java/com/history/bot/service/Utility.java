package com.history.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

@Slf4j
public class Utility  {

    public static StringBuilder readFile(Path pathToFile) {
        StringBuilder builder = new StringBuilder();
        Scanner scanner;
        try {
            scanner = new Scanner(pathToFile);
            while (scanner.hasNext()) {
                String row = scanner.nextLine();
                builder.append(row).append("\n");
            }
        } catch (IOException e) {
            log.info("Не удалось прочитать файл.");
            builder.append("Информация скоро будет передана :)");
        }
        return builder;
    }

}
