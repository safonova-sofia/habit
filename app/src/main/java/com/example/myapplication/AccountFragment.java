package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.myapplication.LoginActivity.KEY_IS_LOGGED_IN;
import static com.example.myapplication.LoginActivity.PREFS_NAME;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
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

        SharedPreferences prefs = getActivity().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        int themeMode = prefs.getInt("theme", AppCompatDelegate.MODE_NIGHT_NO);  // Загрузка сохраненной темы
        AppCompatDelegate.setDefaultNightMode(themeMode);

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
        // Создаем TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> {
                    // Установить уведомление на выбранное время
                    setNotificationForTime(hourOfDay, minute);
                },
                12, 0, true // Устанавливаем значения по умолчанию (12:00) и формат 24 часа
        );

        timePickerDialog.show();
    }

    private void setNotificationForTime(int hour, int minute) {
        // Проверяем версию устройства
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            // Проверяем, есть ли разрешение на использование точных будильников
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(getContext(), "Приложению необходимо разрешение для установки точных уведомлений", Toast.LENGTH_SHORT).show();
                openAlarmPermissionSettings();
                return;
            }
        }

        // Планируем уведомление через AlarmManager
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getActivity(), NotificationReceiver.class);

        // Создаем PendingIntent с флагом FLAG_IMMUTABLE
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

    private void openAlarmPermissionSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        startActivity(intent);
    }

    private void changeTheme() {
        int currentMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        int newMode;
        if (currentMode == Configuration.UI_MODE_NIGHT_NO) {
            newMode = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            newMode = AppCompatDelegate.MODE_NIGHT_NO;
        }

        // Сохраняем выбранный режим в SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        prefs.edit().putInt("theme", newMode).apply();

        // Меняем тему
        AppCompatDelegate.setDefaultNightMode(newMode);

        // Перезапускаем активность
        getActivity().recreate();
    }



}
