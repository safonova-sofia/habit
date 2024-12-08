package com.example.myapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private GridView gridView;
    private TextView monthTitle;
    private Map<LocalDate, Integer> habitCompletionMap;
    private YearMonth currentMonth;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Инициализируем databaseHelper
        databaseHelper = new DatabaseHelper(getContext());

        gridView = view.findViewById(R.id.calendarGridView);
        monthTitle = view.findViewById(R.id.monthTitle);

        // Инициализация данных
        habitCompletionMap = loadHabitCompletionData();
        currentMonth = YearMonth.now();
        updateCalendar();

        // Обработка клика по дню
        gridView.setOnItemClickListener((AdapterView<?> parent, View v, int position, long id) -> {
            LocalDate selectedDate = currentMonth.atDay(position + 1);
            int completionCount = habitCompletionMap.getOrDefault(selectedDate, 0);
            Toast.makeText(getContext(), "Выполнено привычек: " + completionCount, Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void updateCalendar() {
        // Установить название текущего месяца
        monthTitle.setText(currentMonth.getMonth().toString());

        // Генерация списка дней месяца
        List<DayData> days = new ArrayList<>();
        int totalDays = currentMonth.lengthOfMonth();
        for (int i = 1; i <= totalDays; i++) {
            LocalDate date = currentMonth.atDay(i);
            int completionCount = habitCompletionMap.getOrDefault(date, 0);  // Получаем количество выполненных привычек для этого дня
            days.add(new DayData(date.getDayOfMonth(), completionCount));
        }

        // Установка адаптера для GridView
        CalendarAdapter adapter = new CalendarAdapter(getContext(), days);
        gridView.setAdapter(adapter);
    }


    private Map<LocalDate, Integer> loadHabitCompletionData() {
        Map<LocalDate, Integer> data = new HashMap<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT date, COUNT(*) FROM history WHERE is_completed = 1 GROUP BY date", null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String dateStr = cursor.getString(cursor.getColumnIndex("date"));
            int count = cursor.getInt(1);  // Количество выполненных привычек в этот день

            LocalDate date = LocalDate.parse(dateStr);
            data.put(date, count);  // Сохраняем количество выполненных привычек для этой даты
        }
        cursor.close();
        db.close();

        return data;
    }


}
