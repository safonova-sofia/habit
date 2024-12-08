package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Показываем уведомление
        Toast.makeText(context, "Напоминание о привычке!", Toast.LENGTH_SHORT).show();

        // Используем NotificationManager для создания уведомления
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Создаем канал уведомлений (для Android 8.0 и выше)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "habit_reminder", "Напоминание о привычке", NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Создаем уведомление
        Notification notification = new NotificationCompat.Builder(context, "habit_reminder")
                .setContentTitle("Напоминание о привычке")
                .setContentText("Не забудьте выполнить свою привычку!")
                .setSmallIcon(R.drawable.ic_habits)
                .build();

        // Отправляем уведомление
        notificationManager.notify(1, notification);
    }
}


