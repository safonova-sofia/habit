package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habitTracker.db";
    private static final int DATABASE_VERSION = 4;

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
                "is_completed INTEGER DEFAULT 0, " +  // Статус выполнения
                "backgroundColor TEXT, " +  // Столбец для хранения цвета фона
                "FOREIGN KEY(" + HABITS_USER_ID + ") REFERENCES " + USERS_TABLE + "(" + USERS_ID + "))");

        db.execSQL("CREATE TABLE " + HISTORY_TABLE + " (" +
                HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HISTORY_HABIT_ID + " INTEGER, " +
                HISTORY_DATE + " TEXT, " +
                "FOREIGN KEY(" + HISTORY_HABIT_ID + ") REFERENCES " + HABITS_TABLE + "(" + HABITS_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Удаляем старую таблицу (если нужно)
            db.execSQL("DROP TABLE IF EXISTS " + HABITS_TABLE);

            // Создаем новую таблицу с нужной структурой
            db.execSQL("CREATE TABLE " + HABITS_TABLE + " (" +
                    HABITS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    HABITS_TITLE + " TEXT, " +
                    HABITS_USER_ID + " INTEGER, " +
                    "is_completed INTEGER DEFAULT 0, " +
                    "backgroundColor TEXT, " +  // Новый столбец для цвета фона
                    "FOREIGN KEY(" + HABITS_USER_ID + ") REFERENCES " + USERS_TABLE + "(" + USERS_ID + "))");
        }
        // Добавьте другие проверки для разных версий базы данных, если необходимо
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
    public boolean addHabit(String title, int userId, String backgroundColor, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HABITS_TITLE, title);
        values.put(HABITS_USER_ID, userId);
        values.put("backgroundColor", backgroundColor);  // Сохраняем цвет фона
        values.put("is_completed", isCompleted ? 1 : 0);  // Сохраняем статус выполнения (1 — выполнена, 0 — не выполнена)

        long result = db.insert(HABITS_TABLE, null, values);
        db.close();
        return result != -1;  // Если результат -1, то произошла ошибка
    }





    public List<Habit> getHabitsByUserId(int userId) {
        List<Habit> habitList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Запрос для получения привычек по userId
        Cursor cursor = db.rawQuery("SELECT * FROM " + HABITS_TABLE + " WHERE " + HABITS_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            // Получаем индексы столбцов для HABITS_ID, HABITS_TITLE, is_completed, backgroundColor
            int idColumnIndex = cursor.getColumnIndex(HABITS_ID);
            int titleColumnIndex = cursor.getColumnIndex(HABITS_TITLE);
            int isCompletedColumnIndex = cursor.getColumnIndex("is_completed");
            int backgroundColorColumnIndex = cursor.getColumnIndex("backgroundColor");

            if (idColumnIndex != -1 && titleColumnIndex != -1 && isCompletedColumnIndex != -1 && backgroundColorColumnIndex != -1) {
                do {
                    int id = cursor.getInt(idColumnIndex);  // Получаем ID привычки
                    String title = cursor.getString(titleColumnIndex);  // Получаем название привычки
                    boolean isCompleted = cursor.getInt(isCompletedColumnIndex) == 1;  // Преобразуем int в boolean
                    String backgroundColor = cursor.getString(backgroundColorColumnIndex);  // Получаем цвет фона

                    habitList.add(new Habit(id, title, isCompleted, backgroundColor));  // Создаем Habit с параметрами
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
        return habitList;
    }


    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + USERS_ID + " FROM " + USERS_TABLE + " WHERE email = ?", new String[]{email});

        // Проверяем, найден ли столбец USERS_ID
        int columnIndex = cursor.getColumnIndex(USERS_ID);
        if (columnIndex == -1) {
            // Выводим сообщение об ошибке, если столбец не найден
            Log.e("DatabaseError", "Column " + USERS_ID + " not found.");
            cursor.close();
            db.close();
            return -1;  // Столбец не найден
        }

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(columnIndex);
            cursor.close();
            db.close();
            return userId;
        }

        cursor.close();
        db.close();
        return -1;  // Если пользователь не найден
    }

    public boolean updateHabit(int habitId, String newTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HABITS_TITLE, newTitle);  // Обновляем только название привычки

        int rowsUpdated = db.update(HABITS_TABLE, values, HABITS_ID + " = ?", new String[]{String.valueOf(habitId)});
        db.close();
        return rowsUpdated > 0;  // Если обновлены строки, то возвращаем true
    }

    public boolean updateHabitStatus(int habitId, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_completed", isCompleted ? 1 : 0);  // 1 для выполненной привычки, 0 для невыполненной

        int rowsUpdated = db.update(HABITS_TABLE, values, HABITS_ID + " = ?", new String[]{String.valueOf(habitId)});
        db.close();
        return rowsUpdated > 0;  // Если обновлена хотя бы одна строка, возвращаем true
    }






}
