package com.hype.classmate.ui.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hype.classmate.R;
import com.hype.classmate.interfaces.NoteItem;
import com.hype.classmate.interfaces.TimetableSubject;
import com.hype.classmate.ui.AddSubjectActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    EditText titleEditText, bodyEditText;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private Menu menu;
    String noteID;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        titleEditText = findViewById(R.id.noteTitle);
        bodyEditText = findViewById(R.id.noteBody);

        Bundle b = getIntent().getExtras();

        if (b != null && b.getString("noteID") != null) {
            noteID = b.getString("noteID");
            dbReference.child("Notes/" + user.getUid()).child(noteID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    titleEditText.setText(task.getResult().child("title").getValue().toString());
                    bodyEditText.setText(task.getResult().child("body").getValue().toString());
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        saveNote();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.note_save) {
            saveNote();
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveNote() {
        if (!titleEditText.getText().toString().equals("") && !bodyEditText.getText().toString().equals("")) {
            FirebaseUser user = mAuth.getCurrentUser();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            NoteItem noteItem = new NoteItem(titleEditText.getText().toString(), bodyEditText.getText().toString());
            dbReference = FirebaseDatabase.getInstance("https://classmate-140fd-default-rtdb.firebaseio.com/").getReference();

            // felhasznalo IDjevel rogziti a nevet databaseben
            assert user != null;
            dbReference.child("Notes").child(user.getUid() + "/" + noteID).setValue(noteItem);
            finish();
        } else {
            Toast.makeText(NoteActivity.this, "Title must not be null.", Toast.LENGTH_SHORT).show();
        }
    }
}