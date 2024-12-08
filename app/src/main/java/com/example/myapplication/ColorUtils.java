package com.example.myapplication;

import java.util.Random;

public class ColorUtils {
    // Метод для случайного пастельного цвета
    public static String getRandomPastelColor() {
        Random random = new Random();
        // Массив пастельных цветов
        String[] pastelColors = {
                "#FFB3BA", // розовый
                "#FFDFD3", // светлый персиковый
                "#FFEC8B", // светлый желтый
                "#C2F0C2", // светлый зеленый
                "#B3D9FF"  // светлый голубой
        };
        // Возвращаем случайный цвет из массива
        return pastelColors[random.nextInt(pastelColors.length)];
    }
}

