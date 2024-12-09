package com.example.myapplication;

import static com.example.myapplication.LoginActivity.PREFS_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateHabitActivity extends AppCompatActivity {

    private EditText habitTitleEditText, habitDescriptionEditText;
    private DatabaseHelper databaseHelper;
    private int userId;

    public CreateHabitActivity() {
        // Список привычек для обновления
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);

        habitTitleEditText = findViewById(R.id.habitTitleEditText);
        habitDescriptionEditText = findViewById(R.id.habitDescriptionEditText);
        Button saveButton = findViewById(R.id.saveButton);
        databaseHelper = new DatabaseHelper(this);

        // Загружаем userId из SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);  // Загружаем userId

        // Проверяем, если userId не найден
        if (userId == -1) {
            Toast.makeText(this, "Ошибка: пользователь не найден", Toast.LENGTH_SHORT).show();
            finish(); // Закрываем активность, если userId не найден
            return;
        }

        saveButton.setOnClickListener(v -> {
            String habitTitle = habitTitleEditText.getText().toString().trim();
            String habitDescription = habitDescriptionEditText.getText().toString().trim();  // Получаем описание привычки
            if (!habitTitle.isEmpty()) {
                // Сохраняем привычку в базе данных с привязкой к текущему пользователю
                saveHabit(habitTitle, habitDescription);
                Toast.makeText(this, "Привычка сохранена", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Введите название привычки", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveHabit(String title, String description) {
        // Генерируем случайный цвет
        String randomColor = ColorUtils.getRandomPastelColor();

        // Устанавливаем статус выполнения как невыполненный (is_completed = false)
        boolean isCompleted = false;

        String createdAt = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

        // Тип повторения и дни недели/месяца (можно установить по умолчанию, или дать пользователю выбрать)
        String repeatType = "daily";  // Пример: ежедневное или еженедельное повторение
        String daysOfWeek = "";  // Пример: для повторений по дням недели
        String daysOfMonth = "";  // Пример: для повторений в определенные дни месяца

        // Сохраняем привычку в базе данных
        boolean isSaved = databaseHelper.addHabit(title, description, userId, randomColor, repeatType, daysOfWeek, daysOfMonth, createdAt, isCompleted);

        if (!isSaved) {
            Toast.makeText(this, "Ошибка при сохранении привычки", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Привычка сохранена", Toast.LENGTH_SHORT).show();
            // Возвращаемся на экран привычек и обновляем данные
            Intent intent = new Intent(CreateHabitActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}


