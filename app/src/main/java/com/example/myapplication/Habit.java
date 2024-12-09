package com.example.myapplication;

import java.io.Serializable;

public class Habit implements Serializable {

    private int id;
    private final String title;
    private final String description;
    private boolean isCompleted;
    private String backgroundColor;
    // JSON строка
    // JSON строка

    public Habit(int id, String title, String description, boolean isCompleted, String backgroundColor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
        this.backgroundColor = backgroundColor;
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

    public String getDescription() { return description; }

}
