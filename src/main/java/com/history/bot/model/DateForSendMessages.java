package com.history.bot.model;

import lombok.Getter;

import java.nio.file.Path;
import java.sql.Timestamp;

@Getter
public enum DateForSendMessages {

    JAN_30(Timestamp.valueOf("2025-01-30 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\january_30.txt")),

    FEB_2(Timestamp.valueOf("2025-02-02 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\february_2.txt")),
    FEB_4_11(Timestamp.valueOf("2025-02-04 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\february_4-11.txt")),

    APR_9(Timestamp.valueOf("2025-04-09 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\april_9.txt")),
    APR_16(Timestamp.valueOf("2025-04-16 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\april_16.txt")),
    APR_20(Timestamp.valueOf("2025-04-20 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\april_20.txt")),
    APR_25(Timestamp.valueOf("2025-04-25 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\april_25.txt")),
    APR_30(Timestamp.valueOf("2025-04-30 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\april_30.txt")),

    MAY_2(Timestamp.valueOf("2025-05-02 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\may_2.txt")),
    MAY_6(Timestamp.valueOf("2025-05-06 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\may_6-11.txt")),
    MAY_8(Timestamp.valueOf("2025-05-08 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\may_8.txt")),
    MAY_9(Timestamp.valueOf("2025-05-09 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\may_9.txt")),

    TEST(Timestamp.valueOf("2025-03-12 12:00:00.000"), Path.of("D:\\Java\\bot\\src\\main\\resources\\text_from_messages\\test.txt"));

    private final Timestamp timestamp;
    private final Path pathToFile;

    DateForSendMessages(Timestamp timestamp, Path pathToFile) {
        this.timestamp = timestamp;
        this.pathToFile = pathToFile;
    }
}

