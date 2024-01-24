package com.hype.classmate.ui.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hype.classmate.R;
import com.hype.classmate.interfaces.NoteItem;
import com.hype.classmate.interfaces.TimetableSubject;
import com.hype.classmate.ui.AddSubjectActivity;

public class NoteActivity extends AppCompatActivity {

    ImageButton saveButton;
    EditText titleEditText, bodyEditText;
    FirebaseAuth mAuth;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        saveButton = findViewById(R.id.saveNoteButton);
        mAuth = FirebaseAuth.getInstance();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!titleEditText.getText().toString().equals("")) {
                    FirebaseUser user = mAuth.getCurrentUser();

                    NoteItem noteItem = new NoteItem(titleEditText.getText().toString(), bodyEditText.getText().toString());
                    dbReference = FirebaseDatabase.getInstance("https://classmate-140fd-default-rtdb.firebaseio.com/").getReference();

                    // felhasznalo IDjevel rogziti a nevet databaseben
                    assert user != null;
                    dbReference.child("Notes").child(user.getUid() + "/" + titleEditText.getText().toString()).setValue(noteItem);
                    finish();
                } else {
                    Toast.makeText(NoteActivity.this, "Title must not be null.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        titleEditText = findViewById(R.id.noteTitle);
        bodyEditText = findViewById(R.id.noteBody);
    }
}