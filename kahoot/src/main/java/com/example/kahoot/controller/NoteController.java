package com.example.kahoot.controller;

import com.example.kahoot.entity.Note;
import com.example.kahoot.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/notes")
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        Note savedNote = noteService.saveOrUpdate(note);
        return ResponseEntity.ok(savedNote);
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable int id) {
        return noteService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/notes")
    public ResponseEntity<List<Note>> getAllNotes() {
        List<Note> notes = noteService.findAll();
        return ResponseEntity.ok(notes);
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable int id, @RequestBody Note note) {
        return noteService.findById(id)
                .map(existingNote -> {
                    note.setId(id);
                    Note updatedNote = noteService.saveOrUpdate(note);
                    return ResponseEntity.ok(updatedNote);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable int id) {
        return noteService.findById(id)
                .map(note -> {
                    noteService.delete(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/notes/user/{username}")
    public ResponseEntity<List<Note>> getByUser(@PathVariable String username)
    {
        List<Note> notes = noteService.findByUser(username);
        return ResponseEntity.ok(notes);
    }
}