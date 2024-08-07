    package com.example.notpadapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

    public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "NotesPrefs";
    private static final String KEY_NOTE_COUNT = "NoteCount";

    private LinearLayout notesContainer;
    private List<Note> notes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesContainer = findViewById(R.id.notesLayout);
        Button addNoteButton = findViewById(R.id.addNoteButton);

        notes = new ArrayList<>();

        addNoteButton.setOnClickListener(v -> {
            saveNote();
        });
    }

    private void saveNote() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.descriptionEditText);

        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        if(title.isEmpty() || content.isEmpty()) {
            return;
        }

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        Date date = new Date();
        note.setDate(date.toString());

        notes.add(note);
        saveNotesToPreferences();

        createNotesView();
    }

        private void createNotesView() {
            View noteView = getLayoutInflater().inflate(R.layout.note_item.xml);
        }

        private void saveNotesToPreferences() {
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putInt(KEY_NOTE_COUNT, notes.size());
            for(int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                editor.putString("noteTitle" + i, note.getTitle());
                editor.putString("noteContent" + i, note.getContent());
                editor.putString("noteDate" + i, note.getDate());
            }
            editor.apply();
        }
    }