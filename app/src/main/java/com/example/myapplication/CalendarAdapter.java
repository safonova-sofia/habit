package com.example.myapplication;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private final Context context;
    private final List<DayData> days;

    public CalendarAdapter(Context context, List<DayData> days) {
        this.context = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_calendar_day, parent, false);
        }

        TextView dayText = convertView.findViewById(R.id.calendarDayText);
        DayData dayData = days.get(position);

        dayText.setText(String.valueOf(dayData.getDay()));

        // Установка цвета в зависимости от количества выполненных привычек
        int completionCount = dayData.getHabitCompletionCount();
        int colorIntensity = Math.min(255, completionCount * 30);
        dayText.setBackgroundColor(Color.rgb(255 - colorIntensity, 255, 255 - colorIntensity));

        return convertView;
    }
}

