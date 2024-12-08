package com.example.myapplication;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class HabitsFragment extends Fragment {

    private RecyclerView recyclerView;
    private HabitsAdapter adapter;
    private List<Habit> habitList;
    private Button createHabitButton;
    private DatabaseHelper databaseHelper;

    private int userId = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        recyclerView = view.findViewById(R.id.habitsRecyclerView);
        createHabitButton = view.findViewById(R.id.createHabitButton);
        habitList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getContext());

        // Инициализация списка привычек
        habitList = new ArrayList<>();
        adapter = new HabitsAdapter(habitList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        // Заполнение списка привычек из базы данных
        loadHabits();

        // Обработка нажатия кнопки создания привычки
        createHabitButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateHabitActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadHabits() {
        // Извлекаем привычки из базы данных
        habitList.clear();  // Очищаем список, если он уже был заполнен
        habitList.addAll(databaseHelper.getHabitsByUserId(userId));

        // Обновляем адаптер
        adapter.notifyDataSetChanged();
    }
}
