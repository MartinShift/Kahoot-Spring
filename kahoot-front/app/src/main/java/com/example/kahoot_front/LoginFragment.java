package com.example.kahoot_front;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kahoot_front.api.NetworkService;
import com.example.kahoot_front.model.LoginModel;
import com.example.kahoot_front.model.User;
import com.example.kahoot_front.service.*;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    // Replace these with your actual views
    private EditText loginEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Replace this with your actual layout
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize your views here
        loginEditText = view.findViewById(R.id.loginEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        return view;
    }

    private void loginUser() {
        String login = loginEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        LoginModel loginModel = new LoginModel(login, password);
        Call<User> call = NetworkService.getInstance().getApiXml().loginUser(loginModel);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    SharedPrefManager.getInstance(getContext()).saveCurrentUser(user);
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}