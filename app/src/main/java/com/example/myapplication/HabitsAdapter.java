package com.example.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.HabitViewHolder> {

    private final List<Habit> habitList;
    private final OnHabitClickListener listener;


    public HabitsAdapter(List<Habit> habitList, OnHabitClickListener listener) {
        this.habitList = habitList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habitList.get(position);
        holder.titleTextView.setText(habit.getTitle());

        // Устанавливаем цвет фона
        holder.itemView.setBackgroundColor(Color.parseColor(habit.getBackgroundColor()));  // Используем сохраненный цвет

        // Устанавливаем прозрачность в зависимости от выполнения привычки
        if (habit.isCompleted()) {
            holder.itemView.setAlpha(0.5f);  // Привычка выполнена - полупрозрачный фон
        } else {
            holder.itemView.setAlpha(1f);  // Привычка не выполнена - яркий фон
        }

        holder.itemView.setOnClickListener(v -> listener.onHabitClick(habit));
    }


    @Override
    public int getItemCount() {
        return habitList.size();
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public HabitViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.habitTitle);
        }
    }

    // Интерфейс для клика по привычке
    public interface OnHabitClickListener {
        void onHabitClick(Habit habit);
    }
}

