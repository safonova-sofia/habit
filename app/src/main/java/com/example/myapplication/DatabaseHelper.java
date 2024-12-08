package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habitTracker.db";
    private static final int DATABASE_VERSION = 1;

    // Таблицы
    public static final String USERS_TABLE = "users";
    public static final String HABITS_TABLE = "habits";
    public static final String HISTORY_TABLE = "history";

    // Поля таблицы users
    public static final String USERS_ID = "id";
    public static final String USERS_EMAIL = "email";
    public static final String USERS_PASSWORD = "password";

    // Поля таблицы habits
    public static final String HABITS_ID = "id";
    public static final String HABITS_TITLE = "title";
    public static final String HABITS_USER_ID = "user_id";

    // Поля таблицы history
    public static final String HISTORY_ID = "id";
    public static final String HISTORY_HABIT_ID = "habit_id";
    public static final String HISTORY_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблиц
        db.execSQL("CREATE TABLE " + USERS_TABLE + " (" +
                USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERS_EMAIL + " TEXT UNIQUE, " +
                USERS_PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE " + HABITS_TABLE + " (" +
                HABITS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HABITS_TITLE + " TEXT, " +
                HABITS_USER_ID + " INTEGER, " +
                "FOREIGN KEY(" + HABITS_USER_ID + ") REFERENCES " + USERS_TABLE + "(" + USERS_ID + "))");

        db.execSQL("CREATE TABLE " + HISTORY_TABLE + " (" +
                HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HISTORY_HABIT_ID + " INTEGER, " +
                HISTORY_DATE + " TEXT, " +
                "FOREIGN KEY(" + HISTORY_HABIT_ID + ") REFERENCES " + HABITS_TABLE + "(" + HABITS_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление базы данных
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HABITS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE);
        onCreate(db);
    }

    // Метод для регистрации пользователя
    public boolean registerUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERS_EMAIL, email);
        values.put(USERS_PASSWORD, password);
        long result = db.insert(USERS_TABLE, null, values);
        db.close();
        return result != -1;
    }

    // Метод для входа пользователя
    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USERS_EMAIL + " = ? AND " + USERS_PASSWORD + " = ?", new String[]{email, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    // Метод для проверки, зарегистрирован ли email
    public boolean isEmailRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USERS_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // метод addHabit, который будет сохранять привычку в базе данных
    public boolean addHabit(String title, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HABITS_TITLE, title);
        values.put(HABITS_USER_ID, userId);  // ID пользователя, для которого сохраняется привычка

        long result = db.insert(HABITS_TABLE, null, values);
        db.close();
        return result != -1;  // Если результат -1, то произошла ошибка
    }

    public List<Habit> getHabitsByUserId(int userId) {
        List<Habit> habitList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Запрос для получения привычек по userId
        Cursor cursor = db.rawQuery("SELECT * FROM " + HABITS_TABLE + " WHERE " + HABITS_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(HABITS_TITLE));
                habitList.add(new Habit(title));  // Добавляем привычку в список
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return habitList;
    }
}
