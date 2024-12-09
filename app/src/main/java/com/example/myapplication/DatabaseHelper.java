package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habitTracker.db";
    private static final int DATABASE_VERSION = 8;

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
    public static final String HABITS_DESCRIPTION = "description";
    public static final String HABITS_USER_ID = "user_id";
    public static final String HABITS_CREATED_AT = "created_at";
    public static final String HABITS_REPEAT_TYPE = "repeat_type";
    public static final String HABITS_DAYS_OF_WEEK = "days_of_week";
    public static final String HABITS_DAYS_OF_MONTH = "days_of_month";
    public static final String HABITS_IS_COMPLETED = "is_completed";
    public static final String HABITS_BACKGROUND_COLOR = "backgroundColor";

    // Поля таблицы history
    public static final String HISTORY_ID = "id";
    public static final String HISTORY_HABIT_ID = "habit_id";
    public static final String HISTORY_DATE = "date";
    public static final String HISTORY_IS_COMPLETED = "is_completed"; // Столбец для статуса выполнения

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
                HABITS_DESCRIPTION + " TEXT, " +
                HABITS_USER_ID + " INTEGER, " +
                HABITS_CREATED_AT + " TEXT, " +
                HABITS_REPEAT_TYPE + " TEXT, " +
                HABITS_DAYS_OF_WEEK + " TEXT, " +
                HABITS_DAYS_OF_MONTH + " TEXT, " +
                "is_completed INTEGER DEFAULT 0, " +
                HABITS_BACKGROUND_COLOR + " TEXT, " +  // Новый столбец для цвета фона
                "FOREIGN KEY(" + HABITS_USER_ID + ") REFERENCES " + USERS_TABLE + "(" + USERS_ID + "))");


        db.execSQL("CREATE TABLE " + HISTORY_TABLE + " (" +
                HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HISTORY_HABIT_ID + " INTEGER, " +
                HISTORY_DATE + " TEXT, " +  // Новый столбец для даты выполнения
                HISTORY_IS_COMPLETED + " INTEGER, " +  // Статус выполнения (0 - не выполнено, 1 - выполнено)
                "FOREIGN KEY(" + HISTORY_HABIT_ID + ") REFERENCES " + HABITS_TABLE + "(" + HABITS_ID + "))");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
    public boolean addHabit(String title, String description, int userId, String backgroundColor, String repeatType, String daysOfWeek, String daysOfMonth, String createdAt, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HABITS_TITLE, title);
        values.put(HABITS_DESCRIPTION, description);
        values.put(HABITS_USER_ID, userId);
        values.put(HABITS_CREATED_AT, createdAt);
        values.put(HABITS_REPEAT_TYPE, repeatType);
        values.put(HABITS_DAYS_OF_WEEK, daysOfWeek);
        values.put(HABITS_DAYS_OF_MONTH, daysOfMonth);
        values.put(HABITS_BACKGROUND_COLOR, backgroundColor);
        values.put(HABITS_IS_COMPLETED, isCompleted ? 1 : 0);  // Сохраняем статус выполнения (1 — выполнена, 0 — не выполнена)

        long result = db.insert(HABITS_TABLE, null, values);
        db.close();
        return result != -1;  // Если результат -1, то произошла ошибка
    }



    public List<Habit> getHabitsByUserId(int userId) {
        List<Habit> habitList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + HABITS_TABLE + " WHERE " + HABITS_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(HABITS_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(HABITS_TITLE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(HABITS_DESCRIPTION));  // Получаем описание
                @SuppressLint("Range") String createdAt = cursor.getString(cursor.getColumnIndex(HABITS_CREATED_AT));  // Получаем дату создания
                @SuppressLint("Range") String repeatType = cursor.getString(cursor.getColumnIndex(HABITS_REPEAT_TYPE));  // Получаем тип повторения
                @SuppressLint("Range") String daysOfWeek = cursor.getString(cursor.getColumnIndex(HABITS_DAYS_OF_WEEK));  // Получаем дни недели
                @SuppressLint("Range") String daysOfMonth = cursor.getString(cursor.getColumnIndex(HABITS_DAYS_OF_MONTH));  // Получаем дни месяца
                @SuppressLint("Range") boolean isCompleted = cursor.getInt(cursor.getColumnIndex(HABITS_IS_COMPLETED)) == 1;  // Преобразуем int в boolean
                @SuppressLint("Range") String backgroundColor = cursor.getString(cursor.getColumnIndex(HABITS_BACKGROUND_COLOR));  // Получаем цвет фона

                habitList.add(new Habit(id, title, description, isCompleted, backgroundColor, createdAt, repeatType, daysOfWeek, daysOfMonth));  // Создаем Habit с параметрами
            } while (cursor.moveToNext());
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

    public boolean updateHabit(int habitId, String newTitle, String newDescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HABITS_TITLE, newTitle);  // Обновляем название привычки
        values.put(HABITS_DESCRIPTION, newDescription);  // Обновляем описание привычки

        int rowsUpdated = db.update(HABITS_TABLE, values, HABITS_ID + " = ?", new String[]{String.valueOf(habitId)});
        db.close();
        return rowsUpdated > 0;  // Если обновлены строки, то возвращаем true
    }


    public boolean updateHabitStatus(int habitId, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HABITS_IS_COMPLETED, isCompleted ? 1 : 0);

        // Обновляем статус выполнения привычки в таблице habits
        int rowsUpdated = db.update(HABITS_TABLE, values, HABITS_ID + " = ?", new String[]{String.valueOf(habitId)});
        db.close();

        // Добавляем запись в таблицу history
        boolean isHistoryAdded = addHistoryRecord(habitId, isCompleted);
        return rowsUpdated > 0 && isHistoryAdded;  // Если обновлена хотя бы одна строка и запись добавлена в историю, возвращаем true
    }


    public boolean deleteHabit(int habitId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Удаляем связанные записи в истории
        db.delete(HISTORY_TABLE, HISTORY_HABIT_ID + " = ?", new String[]{String.valueOf(habitId)});

        // Удаляем саму привычку из таблицы habits
        int rowsDeleted = db.delete(HABITS_TABLE, HABITS_ID + " = ?", new String[]{String.valueOf(habitId)});
        db.close();

        return rowsDeleted > 0;  // Если количество удаленных строк больше 0, значит удаление прошло успешно
    }



    public boolean addHistoryRecord(int habitId, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Получаем текущую дату
        String currentDate = LocalDate.now().toString();  // Формат даты YYYY-MM-DD

        // Добавляем запись в таблицу history
        values.put(HISTORY_HABIT_ID, habitId);
        values.put(HISTORY_DATE, currentDate);
        values.put(HISTORY_IS_COMPLETED, isCompleted ? 1 : 0);  // 1 — выполнена, 0 — не выполнена

        long result = db.insert(HISTORY_TABLE, null, values);
        db.close();
        return result != -1;  // Если результат -1, то произошла ошибка
    }

    public List<HistoryRecord> getHistoryByHabitId(int habitId) {
        List<HistoryRecord> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + HISTORY_TABLE + " WHERE " + HISTORY_HABIT_ID + " = ?", new String[]{String.valueOf(habitId)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(HISTORY_DATE));
                @SuppressLint("Range") boolean isCompleted = cursor.getInt(cursor.getColumnIndex(HISTORY_IS_COMPLETED)) == 1;
                historyList.add(new HistoryRecord(date, isCompleted));  // Добавляем новую запись истории
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return historyList;
    }

    public boolean deleteUserAndAssociatedData(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Удаляем связанные записи в таблице history
            db.delete(HISTORY_TABLE, HISTORY_HABIT_ID + " IN (SELECT " + HABITS_ID + " FROM " + HABITS_TABLE + " WHERE " + HABITS_USER_ID + " = ?)", new String[]{String.valueOf(userId)});

            // Удаляем привычки пользователя
            db.delete(HABITS_TABLE, HABITS_USER_ID + " = ?", new String[]{String.valueOf(userId)});

            // Удаляем пользователя
            int rowsDeleted = db.delete(USERS_TABLE, USERS_ID + " = ?", new String[]{String.valueOf(userId)});

            if (rowsDeleted > 0) {
                db.setTransactionSuccessful();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }




}


