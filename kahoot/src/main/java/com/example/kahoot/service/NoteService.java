package com.example.kahoot.service;

import com.example.kahoot.entity.Note;
import com.example.kahoot.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note saveOrUpdate(Note note) {
        return noteRepository.save(note);
    }

    public Optional<Note> findById(int id) {
        return noteRepository.findById(id);
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public void delete(int id) {
        noteRepository.deleteById(id);
    }

    public List<Note> findByUser(String username) {
        return noteRepository.findByUserUsername(username);
    }
}