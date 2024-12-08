package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.myapplication.LoginActivity.KEY_IS_LOGGED_IN;
import static com.example.myapplication.LoginActivity.PREFS_NAME;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class AccountFragment extends Fragment {

    private Button buttonSetNotification, buttonChangeTheme, logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        logoutButton = view.findViewById(R.id.buttonLogout);
        buttonSetNotification = view.findViewById(R.id.buttonSetNotification);
        buttonChangeTheme = view.findViewById(R.id.buttonChangeTheme);

        // Обработка выхода из аккаунта
        logoutButton.setOnClickListener(v -> logout());

        buttonSetNotification.setOnClickListener(v -> {
            setNotification();
        });

        buttonChangeTheme.setOnClickListener(v -> {
            changeTheme();
        });

        return view;
    }

    private void logout() {
        // Используем requireContext() для получения контекста
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false); // Сбрасываем флаг
        editor.apply();

        // Переходим обратно на экран входа
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish(); // Закрыть текущую активность
    }

    private void setNotification() {
        // Окно выбора времени для уведомлений
        TimePicker timePicker = new TimePicker(requireContext());
        timePicker.setIs24HourView(true);

        // Время уведомления
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        // Планируем уведомление через AlarmManager
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Intent intent = new Intent(getActivity(), NotificationReceiver.class);

        // Создаем PendingIntent с флагом IMMUTABLE
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getActivity(),
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(getContext(), "Уведомление установлено!", Toast.LENGTH_SHORT).show();
        }
    }


    private void changeTheme() {
        int currentMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        // Переключаем между светлой и темной темой
        if (currentMode == Configuration.UI_MODE_NIGHT_NO) {
            // Устанавливаем темную тему
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);  // Темная тема
        } else {
            // Устанавливаем светлую тему
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);  // Светлая тема
        }

        // Перезапускаем активность для применения новой темы
        getActivity().recreate();  // Перезапуск активности
    }



}
