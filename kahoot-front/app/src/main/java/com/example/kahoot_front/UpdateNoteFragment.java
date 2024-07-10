package com.example.kahoot_front;

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

public class UpdateNoteFragment extends Fragment {
    private static final int PICK_FILE_REQUEST_CODE = 1;

    private EditText titleEditText;
    private EditText contentEditText;
    private RecyclerView filesRecyclerView;
    private FilesAdapter filesAdapter;
    private List<String> uploadedFilesUrls = new ArrayList<>();
    private Button uploadButton;
    private Button submitButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        contentEditText = view.findViewById(R.id.contentEditText);
        filesRecyclerView = view.findViewById(R.id.filesRecyclerView);
        uploadButton = view.findViewById(R.id.uploadButton);
        submitButton = view.findViewById(R.id.submitButton);

        filesAdapter = new FilesAdapter(uploadedFilesUrls);
        filesRecyclerView.setAdapter(filesAdapter);
        filesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        uploadButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
        });

        submitButton.setOnClickListener(v -> submitNote());

        if (getArguments() != null) {
            String title = getArguments().getString("noteTitle", "");
            String content = getArguments().getString("noteContent", "");
            List<String> fileUrls = getArguments().getStringArrayList("fileUrls");

            titleEditText.setText(title);
            contentEditText.setText(content);
            uploadedFilesUrls.clear();
            if (fileUrls != null) {
                uploadedFilesUrls.addAll(fileUrls);
            }
            filesAdapter.notifyDataSetChanged();
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            uploadFile(fileUri);
        }
    }

    private void submitNote() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setFileUrls(uploadedFilesUrls);
        Call<Note> call = NetworkService.getInstance().getApiJson().updateNote(note.getId(), note);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Note updated successfully", Toast.LENGTH_SHORT).show();
                    Log.d("UpdateNoteFragment", "Note updated: " + note.getTitle());
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    String errorMessage = "Unknown error";
                    try {
                        // Attempt to retrieve the error message from the server
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("UpdateNoteFragment", "Error reading error message", e);
                    }
                    Log.e("UpdateNoteFragment", "Failed to update note: " + errorMessage);
                    Toast.makeText(getContext(), "Failed to update note: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getContext(), "Failed to update note: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFile(Uri fileUri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(fileUri);
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

            RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(fileUri)), tempFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", tempFile.getName(), requestFile);

            Api apiScalar = NetworkService.getInstance().getApiScalar();
            apiScalar.uploadImage(body).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String imageUrl = response.body();
                        uploadedFilesUrls.add(imageUrl);
                        filesAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Upload failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getContext(), "Upload failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}