package com.example.kahoot_front;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.kahoot_front.model.User;
import com.example.kahoot_front.service.SharedPrefManager;

public class ProfileFragment extends Fragment {

    private TextView usernameTextView, emailTextView;
    private ImageView userImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        loadUserProfile();
    }

    private void initViews(View view) {
        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        userImageView = view.findViewById(R.id.userImageView);
    }

    private void loadUserProfile() {
        User currentUser = SharedPrefManager.getInstance(getContext()).getCurrentUser();
        if (currentUser != null) {
            usernameTextView.setText(currentUser.getUsername());
            emailTextView.setText(currentUser.getEmail());
            Glide.with(this).load(currentUser.getAvatarUrl()).into(userImageView);
        }
    }
}