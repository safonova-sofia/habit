package com.example.myapplication;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.HabitViewHolder> {

    private List<Habit> habitList;
    private OnHabitClickListener listener;
    private GestureDetector gestureDetector;

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

        // Обработка клика по привычке
        holder.itemView.setOnClickListener(v -> listener.onHabitClick(habit));

        // Обработка долгого нажатия для редактирования привычки
        holder.itemView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
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

    // Интерфейс для обработки клика
    public interface OnHabitClickListener {
        void onHabitClick(Habit habit);
    }

    // Создание GestureDetector для обработки долгого нажатия
    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }
}
