package com.example.myapplication;

public class HistoryRecord {
    private String date;
    private boolean isCompleted;

    public HistoryRecord(String date, boolean isCompleted) {
        this.date = date;
        this.isCompleted = isCompleted;
    }

    public String getDate() {
        return date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}

