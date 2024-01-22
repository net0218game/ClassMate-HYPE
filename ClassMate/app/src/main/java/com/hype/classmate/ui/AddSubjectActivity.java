package com.hype.classmate.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hype.classmate.MainActivity;
import com.hype.classmate.R;
import com.hype.classmate.interfaces.TimetableSubject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hype.classmate.ui.login.LoginActivity;

import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddSubjectActivity extends AppCompatActivity {

    EditText classNameInput, teacherInput, noteInput;
    Button addSubjectButton, mPickColorButton;
    TextView addClassButton;
    FirebaseAuth mAuth;
    DatabaseReference dbReference;
    private int mDefaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        classNameInput = findViewById(R.id.classClassName);
        teacherInput = findViewById(R.id.classTeacher);
        noteInput = findViewById(R.id.classNote);
        addSubjectButton = findViewById(R.id.addSubjectButton);
        addClassButton = findViewById(R.id.addClassButton);

        mAuth = FirebaseAuth.getInstance();

        mPickColorButton = findViewById(R.id.pick_color_button);
        mDefaultColor = 0;

        mPickColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPickerDialogue();
            }
        });
        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!classNameInput.getText().toString().equals("") && !teacherInput.getText().toString().equals("")) {

                    String className, teacher, note;
                    className = classNameInput.getText().toString();
                    teacher = teacherInput.getText().toString();
                    note = noteInput.getText().toString();

                    FirebaseUser user = mAuth.getCurrentUser();

                    TimetableSubject timetableSubject = new TimetableSubject(className, teacher, note, String.format("#%06X", (0xFFFFFF & mDefaultColor)));

                    // :) nem mindenkeppen kell a .getInstace()-be a link de neha buta:( es kell neki
                    dbReference = FirebaseDatabase.getInstance("https://classmate-140fd-default-rtdb.firebaseio.com/").getReference();
                    // felhasznalo IDjevel rogziti a nevet databaseben
                    assert user != null;
                    dbReference.child("Subjects").child(user.getUid() + "/" + className).setValue(timetableSubject).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            classNameInput.setText("");
                            teacherInput.setText("");
                            noteInput.setText("");
                            Toast.makeText(AddSubjectActivity.this, "Subject Added", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Class and teacher must not be null.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSubjectActivity.this, AddClassActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    public void openColorPickerDialogue() {

        // the AmbilWarnaDialog callback needs 3 parameters
        // one is the context, second is default color,
        final AmbilWarnaDialog colorPickerDialogue = new AmbilWarnaDialog(this, mDefaultColor,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // leave this function body as
                        // blank, as the dialog
                        // automatically closes when
                        // clicked on cancel button
                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        // change the mDefaultColor to
                        // change the GFG text color as
                        // it is returned when the OK
                        // button is clicked from the
                        // color picker dialog
                        mDefaultColor = color;
                        mPickColorButton.setBackgroundColor(mDefaultColor);
                        Log.d("color", String.format("#%06X", (0xFFFFFF & mDefaultColor)));
                    }
                });
        colorPickerDialogue.show();
    }
}