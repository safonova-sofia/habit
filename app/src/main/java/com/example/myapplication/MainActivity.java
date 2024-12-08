package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Находим элементы
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Получаем фрагмент по умолчанию
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new HabitsFragment()).commit(); // Показываем HabitsFragment
        bottomNavigationView.setSelectedItemId(R.id.nav_habits); // Устанавливаем активную вкладку

        // Устанавливаем слушатель для BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Переключение фрагментов в зависимости от выбранной вкладки
            if (item.getItemId() == R.id.nav_account) {
                selectedFragment = new AccountFragment();
            } else if (item.getItemId() == R.id.nav_habits) {
                selectedFragment = new HabitsFragment();
            } else if (item.getItemId() == R.id.nav_history) {
                selectedFragment = new HistoryFragment();
            }

            if (selectedFragment != null) {
                // Заменяем текущий фрагмент на выбранный
                fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }

            return true;
        });
    }
}
