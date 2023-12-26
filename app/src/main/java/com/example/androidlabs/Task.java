package com.example.androidlabs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {
    private String description;
    private boolean completed;
    private String date;

    public Task(String description, boolean completed, String date) {
        this.description = description;
        this.completed = completed;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getDate() {
        return date;
    }

    // Метод для получения текущей даты в формате "dd-MM-yyyy"
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
}
