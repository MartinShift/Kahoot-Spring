package com.example.kahoot_front.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kahoot_front.R;
import com.example.kahoot_front.model.Note;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes;
    private OnNoteListener onNoteListener;


    public NoteAdapter(List<Note> notes, OnNoteListener onNoteListener) {
        this.notes = notes;
        this.onNoteListener = onNoteListener;
    }

    public interface OnNoteListener {
        void onNoteClick(Note note);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.noteTitleTextView.setText(note.getTitle());
        holder.noteContentTextView.setText(note.getContent());
        holder.itemView.setOnClickListener(v -> onNoteListener.onNoteClick(notes.get(position)));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitleTextView, noteContentTextView;

        NoteViewHolder(View view) {
            super(view);
            noteTitleTextView = view.findViewById(R.id.noteTitleTextView);
            noteContentTextView = view.findViewById(R.id.noteContentTextView);
        }
    }


}