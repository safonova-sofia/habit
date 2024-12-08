package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Показываем уведомление
        Toast.makeText(context, "Напоминание о привычке!", Toast.LENGTH_SHORT).show();

        // Можно также реализовать уведомление через NotificationManager
    }
}
