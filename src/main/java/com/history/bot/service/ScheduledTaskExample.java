package com.history.bot.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.LocalTime;

public class ScheduledTaskExample {

    public static void initTask() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        LocalTime timeToRun = LocalTime.of(15, 30); // Например, 12:00

        long initialDelay = calculateInitialDelay(timeToRun);

        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Задача выполняется в: " + LocalTime.now());
            // Здесь можно вызвать нужный метод
            myScheduledMethod();
        }, initialDelay, 24 * 60 * 60, TimeUnit.SECONDS); // Повторяем каждый день
    }

    private static long calculateInitialDelay(LocalTime timeToRun) {
        LocalTime now = LocalTime.now();
        long delay = timeToRun.toSecondOfDay() - now.toSecondOfDay();
        if (delay < 0) {
            delay += 24 * 60 * 60; // Добавляем 24 часа, если время уже прошло
        }
        return delay;
    }

    private static void myScheduledMethod() {
        System.out.println("Метод запущен!");
    }
}

