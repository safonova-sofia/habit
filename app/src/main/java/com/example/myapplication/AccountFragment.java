package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myapplication.LoginActivity.KEY_IS_LOGGED_IN;
import static com.example.myapplication.LoginActivity.PREFS_NAME;

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

public class AccountFragment extends Fragment {

    private Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        logoutButton = view.findViewById(R.id.buttonLogout);

        // Обработка выхода из аккаунта
        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    private void logout() {
        // Используем requireContext() для получения контекста
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false); // Сбрасываем флаг
        editor.apply();

        // Переходим обратно на экран входа
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish(); // Закрыть текущую активность
    }
}
