package com.example.myapplication;


public class DayData {
    private final int day;
    private final int habitCompletionCount;

    public DayData(int day, int habitCompletionCount) {
        this.day = day;
        this.habitCompletionCount = habitCompletionCount;
    }

    public int getDay() {
        return day;
    }

    public int getHabitCompletionCount() {
        return habitCompletionCount;
    }
}

