package com.example.kahoot_front;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kahoot_front.api.Api;
import com.example.kahoot_front.api.NetworkService;
import com.example.kahoot_front.model.RegisterModel;
import com.example.kahoot_front.model.User;
import com.example.kahoot_front.service.SharedPrefManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    private EditText loginEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private ImageView avatarImageView;
    private String uploadedImageUrl;
    private Button registerButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private Button uploadButton;

    private Api api;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        initializeViews(view);

        registerButton.setOnClickListener(v -> registerUser());
        uploadButton.setOnClickListener(v -> openImageChooser());

        return view;
    }

    private void initializeViews(View view) {
        loginEditText = view.findViewById(R.id.loginEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);
        avatarImageView = view.findViewById(R.id.avatarImageView);
        registerButton = view.findViewById(R.id.registerButton);
        uploadButton = view.findViewById(R.id.uploadButton);
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void registerUser() {
        String login = loginEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        RegisterModel registerModel = new RegisterModel(login, email, password, confirmPassword, uploadedImageUrl);
        Call<User> call = NetworkService.getInstance().getApiJson().registerUser(registerModel);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    SharedPrefManager.getInstance(getContext()).saveCurrentUser(user);
                    Log.i("RegisterFragment", "User registered successfully: " + user);
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.e("RegisterFragment", "Failed to register user: " + errorMessage);
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("RegisterFragment", "Failed to register user", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            avatarImageView.setImageURI(imageUri);
            uploadImageToServer(imageUri);
        }
    }

    private void uploadImageToServer(Uri imageUri) {


        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
            if (inputStream == null) return;

            File tempFile = File.createTempFile("upload", "tmp", getContext().getCacheDir());
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(imageUri)), tempFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", tempFile.getName(), requestFile);

            Api apiScalar = NetworkService.getInstance().getApiScalar();
            apiScalar.uploadImage(body).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        uploadedImageUrl = response.body();
                        Log.d("UploadImage", "Image uploaded successfully: " + uploadedImageUrl);
                    } else {
                        Log.e("UploadImage", "Failed to upload image: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("UploadImage", "Failed to upload image", t);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("UploadImage", "Error uploading image", e);
        }
    }
}