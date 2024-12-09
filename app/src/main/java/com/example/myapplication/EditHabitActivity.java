package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditHabitActivity extends AppCompatActivity {

    private EditText habitTitleEditText, habitDescriptionEditText;
    private DatabaseHelper databaseHelper;
    private Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        habitTitleEditText = findViewById(R.id.habitTitleEditText);
        habitDescriptionEditText = findViewById(R.id.habitDescriptionEditText);  // Поле для редактирования описания
        Button saveButton = findViewById(R.id.saveButton);
        Button deleteButton = findViewById(R.id.deleteButton);  // Получаем кнопку для удаления привычки
        databaseHelper = new DatabaseHelper(this);

        // Получаем информацию о привычке, переданную через Intent
        habit = (Habit) getIntent().getSerializableExtra("habit");

        // Логирование для отладки
        if (habit != null) {
            Log.d("EditHabitActivity", "Habit received: " + habit.getTitle());  // Логируем полученную привычку
            habitTitleEditText.setText(habit.getTitle()); // Заполняем поле старым названием привычки
            habitDescriptionEditText.setText(habit.getDescription()); // Заполняем поле старым описанием привычки
        } else {
            Log.e("EditHabitActivity", "Habit not found!");  // Логируем ошибку, если habit == null
            Toast.makeText(this, "Ошибка: привычка не найдена", Toast.LENGTH_SHORT).show();
            finish();  // Закрываем активность, если привычка не передана
        }

        saveButton.setOnClickListener(v -> {
            String habitTitle = habitTitleEditText.getText().toString().trim();
            String habitDescription = habitDescriptionEditText.getText().toString().trim();  // Получаем описание привычки
            if (!habitTitle.isEmpty()) {
                // Обновляем привычку в базе данных
                updateHabit(habitTitle, habitDescription);
                Toast.makeText(this, "Привычка обновлена", Toast.LENGTH_SHORT).show();
                finish();  // Закрываем активность
            } else {
                Toast.makeText(this, "Введите название привычки", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(v -> {
            // Удаляем привычку из базы данных
            deleteHabit();
        });
    }

    private void updateHabit(String newTitle, String newDescription) {
        // Обновляем привычку в базе данных, используя id привычки
        if (habit != null) {
            boolean isUpdated = databaseHelper.updateHabit(habit.getId(), newTitle, newDescription);
            if (!isUpdated) {
                Toast.makeText(this, "Ошибка при обновлении привычки", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteHabit() {
        // Удаляем привычку из базы данных
        if (habit != null) {
            boolean isDeleted = databaseHelper.deleteHabit(habit.getId());
            if (isDeleted) {
                Toast.makeText(this, "Привычка удалена", Toast.LENGTH_SHORT).show();
                finish();  // Закрываем активность после удаления
            } else {
                Toast.makeText(this, "Ошибка при удалении привычки", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
