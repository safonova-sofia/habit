package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateHabitActivity extends AppCompatActivity {

    private EditText habitTitleEditText;
    private Button saveButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);

        habitTitleEditText = findViewById(R.id.habitTitleEditText);
        saveButton = findViewById(R.id.saveButton);
        databaseHelper = new DatabaseHelper(this);

        saveButton.setOnClickListener(v -> {
            String habitTitle = habitTitleEditText.getText().toString().trim();
            if (!habitTitle.isEmpty()) {
                // Сохранить привычку в базе данных
                saveHabit(habitTitle);
                Toast.makeText(this, "Привычка сохранена", Toast.LENGTH_SHORT).show();
                finish();  // Закрыть текущую активность
            } else {
                Toast.makeText(this, "Введите название привычки", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveHabit(String title) {
        // Здесь сохраняем привычку в базу данных
        // Например, userId = 1 для теста
        boolean isSaved = databaseHelper.addHabit(title, 1);
        if (!isSaved) {
            Toast.makeText(this, "Ошибка при сохранении привычки", Toast.LENGTH_SHORT).show();
        }
    }
}
