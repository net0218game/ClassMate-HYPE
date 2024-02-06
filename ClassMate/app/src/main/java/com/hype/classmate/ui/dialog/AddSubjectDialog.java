package com.hype.classmate.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hype.classmate.R;
import com.hype.classmate.interfaces.TimetableSubject;
import com.hype.classmate.ui.AddClassActivity;
import com.hype.classmate.ui.AddSubjectActivity;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddSubjectDialog {
    EditText classNameInput, teacherInput, noteInput;
    Button addSubjectButton, mPickColorButton;
    TextView addClassButton;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference dbReference;
    private int mDefaultColor;

    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_subject_dialog);

        classNameInput = dialog.findViewById(R.id.classClassName);
        teacherInput = dialog.findViewById(R.id.classTeacher);
        noteInput = dialog.findViewById(R.id.classNote);
        addSubjectButton = dialog.findViewById(R.id.addSubjectButton);
        addClassButton = dialog.findViewById(R.id.addClassButton);
        mPickColorButton = dialog.findViewById(R.id.pick_color_button);
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
                            Toast.makeText(dialog.getContext(), "Subject Added", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(dialog.getContext(), "Class and teacher must not be null.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public void openColorPickerDialogue() {
        final AmbilWarnaDialog colorPickerDialogue = new AmbilWarnaDialog(mPickColorButton.getContext(), mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // leave this function body as
                // blank, as the dialog
                // automatically closes when
                // clicked on cancel button
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                mPickColorButton.setBackgroundColor(mDefaultColor);
            }
        });
        colorPickerDialogue.show();
    }

}
