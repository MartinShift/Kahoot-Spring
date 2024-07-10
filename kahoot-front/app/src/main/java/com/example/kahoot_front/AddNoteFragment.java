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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kahoot_front.adapter.FilesAdapter;
import com.example.kahoot_front.api.Api;
import com.example.kahoot_front.model.Note;
import com.example.kahoot_front.api.NetworkService;
import com.example.kahoot_front.service.SharedPrefManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNoteFragment extends Fragment {

    private EditText titleEditText, contentEditText;
    private Button submitButton, uploadButton;
    private static final int PICK_FILE_REQUEST_CODE = 1;
    private List<String> uploadedFilesUrls = new ArrayList<>();
    private RecyclerView filesRecyclerView;
    private FilesAdapter filesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        contentEditText = view.findViewById(R.id.contentEditText);
        submitButton = view.findViewById(R.id.submitButton);
        uploadButton = view.findViewById(R.id.uploadButton);
        submitButton.setOnClickListener(v -> submitNote());
        filesRecyclerView = view.findViewById(R.id.filesRecyclerView);
        filesAdapter = new FilesAdapter(uploadedFilesUrls);
        filesRecyclerView.setAdapter(filesAdapter);
        filesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        uploadButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri fileUri = data.getData();
            // Assume you have a method to upload the file and get the URL
            uploadFile(fileUri);
        }
    }
    private void submitNote() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        Note note = new Note(); // Assuming Note class has a constructor that accepts title and content
        note.setTitle(title);
        note.setContent(content);
        note.setUser(SharedPrefManager.getInstance(getContext()).getCurrentUser());
        note.setFileUrls(uploadedFilesUrls);
        Call<Note> call = NetworkService.getInstance().getApiJson().createNote(note);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (response.isSuccessful()) {
                    Note createdNote = response.body();
                    Log.d("AddNoteFragment", "Note created: " + createdNote.getTitle());
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else{
                    Log.e("AddNoteFragment", "Failed to create note: " + response.message());
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void uploadFile(Uri imageUri) {
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
                        String image = response.body();
                        uploadedFilesUrls.add(image);
                        if(filesAdapter != null) {
                            getActivity().runOnUiThread(() -> filesAdapter.notifyDataSetChanged());
                        }
                        else{
                            Log.e("UploadImage", "Adapter is null");
                        }
                        Log.d("UploadImage", "Image uploaded successfully: " + image);
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