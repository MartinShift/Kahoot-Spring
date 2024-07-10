package com.example.kahoot_front;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kahoot_front.R;
import com.example.kahoot_front.adapter.NoteAdapter;
import com.example.kahoot_front.api.NetworkService;
import com.example.kahoot_front.model.Note;
import com.example.kahoot_front.service.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import lombok.var;
import retrofit2.Call;

public class MainFragment extends Fragment implements NoteAdapter.OnNoteListener  {

    private RecyclerView notesRecyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList = new ArrayList<>();

    @Override
    public void onNoteClick(Note note) {
        Bundle bundle = new Bundle();
        bundle.putString("noteTitle", note.getTitle());
        bundle.putString("noteContent", note.getContent());
        bundle.putStringArrayList("fileUrls", new ArrayList<>(note.getFileUrls()));

        UpdateNoteFragment updateNoteFragment = new UpdateNoteFragment();
        updateNoteFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, updateNoteFragment)
                .addToBackStack(null)
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        notesRecyclerView = view.findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noteAdapter = new NoteAdapter(noteList, this);
        notesRecyclerView.setAdapter(noteAdapter);
        loadNotes();
        setupSwipeToDelete();
        FloatingActionButton addNoteButton = view.findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddNoteFragment())
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }

    private void loadNotes() {
        Call<List<Note>> call = NetworkService.getInstance().getApiJson().getByUser(SharedPrefManager.getInstance(this.getContext()).getCurrentUser().getUsername());

        call.enqueue(new retrofit2.Callback<List<Note>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Note>> call, retrofit2.Response<List<Note>> response) {
                if (response.isSuccessful()) {
                    noteList.clear();
                    noteList.addAll(response.body());
                    noteAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Note>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                confirmDeletion(position);
            }
        };
        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(notesRecyclerView);
    }

    private void confirmDeletion(final int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteItem(position))
                .setNegativeButton(android.R.string.no, (dialog, which) -> noteAdapter.notifyItemChanged(position))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteItem(int position) {
        Note noteToDelete = noteList.get(position);

        Call<Void> call = NetworkService.getInstance().getApiJson().deleteNote(noteToDelete.getId());
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    noteList.remove(position);
                    noteAdapter.notifyItemRemoved(position);
                } else {
                    Toast.makeText(getContext(), "Failed to delete note", Toast.LENGTH_SHORT).show();
                    noteAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to delete note: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                noteAdapter.notifyItemChanged(position);
            }
        });
    }
}