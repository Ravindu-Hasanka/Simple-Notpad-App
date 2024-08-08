package com.example.notpadapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Date;
import java.util.List;

import com.example.notepadapp.Note;


public class MainActivity extends AppCompatActivity {

    private LinearLayout notesContainer;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesContainer = findViewById(R.id.notesLayout);
        Button addNoteButton = findViewById(R.id.addNoteButton);

        databaseHelper = new DatabaseHelper(this);

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        displayNotes();
    }

    private void displayNotes() {
        List<com.example.notepadapp.Note> notes = databaseHelper.getAllNotes();
        for (Note note : notes) {
            createNotesView(note);
        }
    }

    private void saveNote() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.descriptionEditText);

        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        if (title.isEmpty() || content.isEmpty()) {
            return;
        }

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        Date date = new Date();
        note.setDate(date.toString());

        databaseHelper.addNote(note);
        createNotesView(note);
        clearInputFields();
    }

    private void clearInputFields() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.descriptionEditText);

        titleEditText.getText().clear();
        contentEditText.getText().clear();
    }

    private void createNotesView(final Note note) {
        View noteView = getLayoutInflater().inflate(R.layout.note_item, null);
        TextView titleTextView = noteView.findViewById(R.id.titleTextView);

        titleTextView.setText(note.getTitle());

        noteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
                intent.putExtra("title", note.getTitle());
                intent.putExtra("date", note.getDate());
                intent.putExtra("content", note.getContent());
                startActivity(intent);
            }
        });

        noteView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(note);
                return true;
            }
        });

        notesContainer.addView(noteView);
    }

    private void showDeleteDialog(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseHelper.deleteNoteById(note.getId());
                refreshNoteViews();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void refreshNoteViews() {
        notesContainer.removeAllViews();
        displayNotes();
    }
}
