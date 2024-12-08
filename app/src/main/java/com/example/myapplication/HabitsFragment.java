package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myapplication.LoginActivity.PREFS_NAME;

public class HabitsFragment extends Fragment {

    private RecyclerView recyclerView;
    private HabitsAdapter adapter;
    private List<Habit> habitList;
    private Button createHabitButton;
    private DatabaseHelper databaseHelper;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        // Инициализация компонентов
        recyclerView = view.findViewById(R.id.habitsRecyclerView);
        createHabitButton = view.findViewById(R.id.createHabitButton);
        databaseHelper = new DatabaseHelper(getContext());

        // Загружаем userId из SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);  // Загружаем userId

        // Проверяем, если userId не найден
        if (userId == -1) {
            // Можно обработать ошибку, если userId не найден (например, попросить повторно войти в систему)
            return view;
        }

        // Инициализация адаптера и RecyclerView
        habitList = databaseHelper.getHabitsByUserId(userId); // Загружаем привычки из базы данных
        adapter = new HabitsAdapter(habitList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Обработка нажатия кнопки для создания привычки
        createHabitButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateHabitActivity.class);
            startActivity(intent);  // Переход на активность создания привычки
        });

        return view;
    }

    private void loadHabits() {
        // Загружаем привычки из базы данных
        habitList.clear();
        habitList.addAll(databaseHelper.getHabitsByUserId(userId));

        // Обновляем адаптер
        adapter.notifyDataSetChanged();
    }
}
