package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myapplication.LoginActivity.PREFS_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitsFragment extends Fragment implements HabitsAdapter.OnHabitClickListener {

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

        // Проверка на существование userId
        if (userId == -1) {
            Toast.makeText(getContext(), "Ошибка: пользователь не найден", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Загружаем привычки из базы данных
        habitList = databaseHelper.getHabitsByUserId(userId);

        // Сортируем привычки: невыполненные сверху, выполненные снизу
        sortHabitsByCompletionStatus(habitList);

        // Инициализация адаптера и RecyclerView
        adapter = new HabitsAdapter(habitList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Обработка нажатия кнопки для создания привычки
        createHabitButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateHabitActivity.class);
            startActivity(intent);  // Переход на активность создания привычки
        });

        // Настройка ItemTouchHelper для свайпов
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Получаем позицию свайпа
                int position = viewHolder.getAdapterPosition();
                Habit habit = habitList.get(position);

                if (habit.isCompleted() && direction == ItemTouchHelper.RIGHT) {
                    // Привычка выполнена, запретить свайп вправо
                    adapter.notifyItemChanged(position);  // Возвращаем элемент в исходное состояние
                    Toast.makeText(getContext(), "Эту привычку нельзя изменить на выполненную.", Toast.LENGTH_SHORT).show();
                } else if (!habit.isCompleted() && direction == ItemTouchHelper.LEFT) {
                    // Привычка не выполнена, запретить свайп влево
                    adapter.notifyItemChanged(position);  // Возвращаем элемент в исходное состояние
                    Toast.makeText(getContext(), "Эту привычку нельзя отменить, так как она не выполнена.", Toast.LENGTH_SHORT).show();
                } else {
                    // Выполняем обычный свайп
                    if (direction == ItemTouchHelper.RIGHT) {
                        // Свайп вправо — отмечаем как выполненную
                        habit.setCompleted(true); // Обновляем статус выполнения в объекте Habit
                        //habit.setBackgroundColor("#808080");  // Меняем цвет фона на серый
                        databaseHelper.updateHabitStatus(habit.getId(), true); // Обновляем в базе данных
                        // Добавляем запись в history
                        databaseHelper.addHistoryRecord(habit.getId(), true);
                        // Перемещаем привычку в конец списка
                        habitList.remove(position);
                        habitList.add(habit);  // Добавляем привычку в конец списка
                        adapter.notifyItemMoved(position, habitList.size() - 1);  // Перемещаем элемент в конец
                        adapter.notifyDataSetChanged();  // Обновляем адаптер
                        Toast.makeText(getContext(), "Привычка отмечена как выполненная!", Toast.LENGTH_SHORT).show();
                    } else if (direction == ItemTouchHelper.LEFT) {
                        // Свайп влево — отмечаем как невыполненную
                        habit.setCompleted(false); // Обновляем статус выполнения в объекте Habit
                        databaseHelper.updateHabitStatus(habit.getId(), false); // Обновляем статус привычки в базе данных


                        // Генерация случайного цвета для невыполненной привычки
                        String randomColor = ColorUtils.getRandomPastelColor();
                        habit.setBackgroundColor(randomColor);  // Присваиваем случайный цвет

                        databaseHelper.updateHabitStatus(habit.getId(), false); // Обновляем в базе данных

                        // Перемещаем привычку обратно в начало списка
                        habitList.remove(position);
                        habitList.add(0, habit);  // Добавляем в начало списка
                        adapter.notifyItemMoved(position, 0);  // Перемещаем элемент в начало
                        adapter.notifyDataSetChanged();  // Обновляем адаптер
                        Toast.makeText(getContext(), "Привычка отменена и получена новый цвет!", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                // Получаем привычку для обновления цвета и прозрачности
                Habit habit = habitList.get(viewHolder.getAdapterPosition());

                // Отображаем цвет на свайпе
                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                if (dX > 0) {
                    // Свайп вправо — цвет серый (для выполнения)
                    paint.setColor(Color.GREEN);
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), paint);
                } else {
                    // Свайп влево — цвет для отмены (например, красный)
                    paint.setColor(Color.RED);
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                }

                // Устанавливаем прозрачность в зависимости от выполнения привычки
                if (habit.isCompleted()) {
                    itemView.setAlpha(0.5f);  // Привычка выполнена - полупрозрачный фон
                } else {
                    itemView.setAlpha(1f);  // Привычка не выполнена - яркий фон
                }
            }
        };

        // Применение ItemTouchHelper к RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onHabitClick(Habit habit) {
        // Обработка клика по привычке
        openEditHabitActivity(habit);
    }

    private void openEditHabitActivity(Habit habit) {
        Intent intent = new Intent(getActivity(), EditHabitActivity.class);
        intent.putExtra("habit", habit);  // Передаем объект Habit в активность
        startActivity(intent);
    }

    // Метод для сортировки привычек по статусу выполнения (невыполненные в начале, выполненные в конце)
    private void sortHabitsByCompletionStatus(List<Habit> habits) {
        habits.sort((habit1, habit2) -> Boolean.compare(habit1.isCompleted(), habit2.isCompleted()));
    }
}

