package com.example.myapplication;

import java.io.Serializable;


public class Habit implements Serializable {

    private int id;         // ID привычки
    private String title;   // Название привычки

    // Конструктор
    public Habit(int id, String title) {
        this.id = id;
        this.title = title;
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
}
