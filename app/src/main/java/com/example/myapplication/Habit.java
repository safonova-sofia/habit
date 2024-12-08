package com.example.myapplication;

import java.io.Serializable;

public class Habit implements Serializable {

    private int id;
    private String title;
    private String description;
    private boolean isCompleted;
    private String backgroundColor;
    private String createdAt;
    private String repeatType;
    private String daysOfWeek; // JSON строка
    private String daysOfMonth; // JSON строка

    public Habit(int id, String title, String description, boolean isCompleted, String backgroundColor,
                 String createdAt, String repeatType, String daysOfWeek, String daysOfMonth) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
        this.backgroundColor = backgroundColor;
        this.createdAt = createdAt;
        this.repeatType = repeatType;
        this.daysOfWeek = daysOfWeek;
        this.daysOfMonth = daysOfMonth;
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

    public String getDescription() { return description; }
    public String getCreatedAt() { return createdAt; }
    public String getRepeatType() { return repeatType; }
    public String getDaysOfWeek() { return daysOfWeek; }
    public String getDaysOfMonth() { return daysOfMonth; }
}
