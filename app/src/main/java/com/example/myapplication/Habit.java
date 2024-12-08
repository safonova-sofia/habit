package com.example.myapplication;

import java.io.Serializable;

public class Habit implements Serializable {

    private int id;
    private String title;
    private boolean isCompleted;
    private String backgroundColor;  // Новое поле для хранения цвета фона

    public Habit(int id, String title, boolean isCompleted, String backgroundColor) {
        this.id = id;
        this.title = title;
        this.isCompleted = isCompleted;
        this.backgroundColor = backgroundColor;  // Инициализируем цвет
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getBackgroundColor() {
        return backgroundColor;  // Получаем цвет фона
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;  // Устанавливаем цвет фона
    }
}
