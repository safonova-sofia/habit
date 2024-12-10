package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myapplication.LoginActivity.PREFS_NAME;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        monthTitle = view.findViewById(R.id.monthTitle);  // TextView для отображения месяца
        view.findViewById(R.id.monthTitle);

        Button prevMonthButton = view.findViewById(R.id.prevMonthButton);  // Кнопка для перехода к предыдущему месяцу
        Button nextMonthButton = view.findViewById(R.id.nextMonthButton);  // Кнопка для перехода к следующему месяцу

        // Инициализация данных
        currentMonth = YearMonth.now();
        habitCompletionMap = loadHabitCompletionData();
        updateCalendar();

        // Обработка клика по дню
        gridView.setOnItemClickListener((parent, v, position, id) -> {
            LocalDate selectedDate = currentMonth.atDay(position + 1);
            //noinspection DataFlowIssue
            int completionCount = habitCompletionMap.getOrDefault(selectedDate, 0);
            Toast.makeText(getContext(), "Выполнено привычек: " + completionCount, Toast.LENGTH_SHORT).show();
        });

        // Обработчик кнопки для предыдущего месяца
        prevMonthButton.setOnClickListener(v -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
        });

        // Обработчик кнопки для следующего месяца
        nextMonthButton.setOnClickListener(v -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar();
        });

        return view;
    }

    private void updateCalendar() {
        // Обновляем заголовок с текущим месяцем
        monthTitle.setText(getMonthTitle(currentMonth));
        // Генерация списка дней месяца
        List<DayData> days = new ArrayList<>();
        int totalDays = currentMonth.lengthOfMonth();
        for (int i = 1; i <= totalDays; i++) {
            LocalDate date = currentMonth.atDay(i);
            //noinspection DataFlowIssue
            int completionCount = habitCompletionMap.getOrDefault(date, 0);
            days.add(new DayData(date.getDayOfMonth(), completionCount));
        }

        // Обновление адаптера
        CalendarAdapter adapter = new CalendarAdapter(getContext(), days);
        gridView.setAdapter(adapter);
    }

    private String getMonthTitle(YearMonth yearMonth) {
        // Форматируем название месяца на русском, например: "9 декабря 2024"
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        return firstDayOfMonth.format(java.time.format.DateTimeFormatter.ofPattern("LLLL yyyy", java.util.Locale.forLanguageTag("ru")));
    }

    private Map<LocalDate, Integer> loadHabitCompletionData() {
        Map<LocalDate, Integer> data = new HashMap<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Загружаем user_id текущего пользователя из SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);  // Получаем текущий user_id

        if (userId == -1) {
            Toast.makeText(getContext(), "Ошибка: пользователь не найден", Toast.LENGTH_SHORT).show();
            return data; // Возвращаем пустую карту, если user_id не найден
        }

        // SQL-запрос для выборки данных только текущего пользователя
        Cursor cursor = db.rawQuery(
                "SELECT h.date, COUNT(*) " +
                        "FROM history h " +
                        "INNER JOIN habits hb ON h.habit_id = hb.id " +
                        "WHERE h.is_completed = 1 AND hb.user_id = ? " +
                        "GROUP BY h.date",
                new String[]{String.valueOf(userId)}
        );

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String dateStr = cursor.getString(cursor.getColumnIndex("date"));
            @SuppressLint("Range") int count = cursor.getInt(1);

            LocalDate date = LocalDate.parse(dateStr);
            data.put(date, count);
        }

        cursor.close();
        db.close();
        return data;
    }


}




