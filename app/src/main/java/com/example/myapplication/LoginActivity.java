package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private DatabaseHelper databaseHelper;

    // Имя файла для SharedPreferences
    static final String PREFS_NAME = "UserPrefs";
    static final String KEY_IS_LOGGED_IN = "is_logged_in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Проверка, если пользователь уже залогинен
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false);

        if (isLoggedIn) {
            // Если пользователь уже авторизован, сразу переходим на MainActivity
            startMainActivity();
            return;
        }

        setContentView(R.layout.activity_login);

        // Инициализация элементов
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        registerButton = findViewById(R.id.buttonRegister);

        // Инициализация базы данных
        databaseHelper = new DatabaseHelper(this);

        // Обработка входа
        loginButton.setOnClickListener(v -> login());

        // Переход на экран регистрации
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверка в базе данных
        boolean isAuthenticated = databaseHelper.loginUser(email, password);
        if (isAuthenticated) {
            // Сохранение информации о пользователе
            int userId = databaseHelper.getUserIdByEmail(email);  // Предположим, что есть метод getUserIdByEmail
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("user_id", userId); // Сохраняем userId
            editor.putBoolean(KEY_IS_LOGGED_IN, true); // Флаг успешного входа
            editor.apply();

            Toast.makeText(this, "Вход выполнен", Toast.LENGTH_SHORT).show();
            startMainActivity(); // Переход к MainActivity
        } else {
            Toast.makeText(this, "Неверные данные или пользователь не существует", Toast.LENGTH_SHORT).show();
        }
    }


    // Метод для перехода в MainActivity
    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Закрытие текущей активности
    }
}
