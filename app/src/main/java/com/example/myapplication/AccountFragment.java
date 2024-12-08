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
        // Перенаправление на экран входа
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        // Закрытие MainActivity
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
