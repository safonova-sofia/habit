package com.example.myapplication;

import java.util.Random;

public class ColorUtils {
    // Метод для случайного пастельного цвета
    public static String getRandomPastelColor() {
        Random random = new Random();
        // Массив пастельных цветов
        String[] pastelColors = {
                "#FBF8CC",
                "#FDE4CF",
                "#FFCFD2",
                "#F1C0E8",
                "#CFBAF0",
                "#A3C4F3",
                "#90DBF4",
                "#8EECF5",
                "#98F5E1",
                "#B9FBC0"
        };
        // Возвращаем случайный цвет из массива
        return pastelColors[random.nextInt(pastelColors.length)];
    }
}

