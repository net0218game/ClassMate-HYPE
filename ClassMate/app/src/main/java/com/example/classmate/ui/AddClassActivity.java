package com.example.classmate.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.classmate.R;
import com.example.classmate.interfaces.TimetableClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AddClassActivity extends AppCompatActivity {

    EditText classNameInput, teacherInput, noteInput;
    Button addClassButton;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        classNameInput = findViewById(R.id.classClassName);
        teacherInput = findViewById(R.id.classTeacher);
        noteInput = findViewById(R.id.classNote);
        addClassButton = findViewById(R.id.addClassButton);

        mAuth = FirebaseAuth.getInstance();

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String className, teacher, note;
                className = classNameInput.getText().toString();
                teacher = teacherInput.getText().toString();
                note = noteInput.getText().toString();

                FirebaseUser user = mAuth.getCurrentUser();

                TimetableClass timetableClass = new TimetableClass(className, teacher, note);

                // nem mindenkeppen kell a .getInstace()-be a link de neha buta:( es kell neki
                dbReference = FirebaseDatabase.getInstance("https://classmate-140fd-default-rtdb.firebaseio.com/").getReference();
                // felhasznalo IDjevel rogziti a nevet databaseben
                assert user != null;
                dbReference.child("Users/" + user.getUid()).child("Classes/" + className).setValue(timetableClass).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("adatbazis", e.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("adatbazis", "DATABASE UPDATED");
                    }
                });
            }
        });


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}