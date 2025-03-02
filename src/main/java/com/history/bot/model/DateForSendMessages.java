package com.history.bot.model;

import lombok.Getter;

import java.nio.file.Path;
import java.sql.Timestamp;

@Getter
public enum DateForSendMessages {

    JAN_30(Timestamp.valueOf("2025-01-30 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\january_30.txt")),
    FEB_2(Timestamp.valueOf("2025-02-02 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\february_2.txt")),
    TEST(Timestamp.valueOf("2025-03-02 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\test.txt"));

    private final Timestamp timestamp;
    private Path pathToFile;

    DateForSendMessages(Timestamp timestamp, Path pathToFile) {
        this.timestamp = timestamp;
        this.pathToFile = pathToFile;
    }
}

