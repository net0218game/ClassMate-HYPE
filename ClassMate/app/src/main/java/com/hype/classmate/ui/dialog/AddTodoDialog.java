package com.hype.classmate.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hype.classmate.R;
import com.hype.classmate.interfaces.TimetableSubject;
import com.hype.classmate.interfaces.TodoItem;
import com.hype.classmate.ui.AddTodoActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddTodoDialog {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference dbReference;
    TextInputEditText dateInput, todoTitleInput, noteInput;
    AutoCompleteTextView categorySpinner, subjectSpinner;
    Button addTodoButton;
    String subject = null;

    public void showDialog(Activity activity, String subject) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_todo_dialog);

        subjectSpinner = dialog.findViewById(R.id.subjectTextView);
        categorySpinner = dialog.findViewById(R.id.todoCategory);

        if (subject != null) {
            this.subject = subject;
        }

        ArrayList<String> subjectList = getSubjectList();
        ArrayList<String> dayList = new ArrayList<>(Arrays.asList(dialog.getContext().getResources().getStringArray(R.array.categories)));

        //Create adapter
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(dialog.getContext(), R.layout.spinner_item, subjectList);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(dialog.getContext(), R.layout.spinner_item, dayList);

        subjectSpinner.setAdapter(subjectAdapter);
        categorySpinner.setAdapter(categoryAdapter);

        todoTitleInput = dialog.findViewById(R.id.todoTitle);
        noteInput = dialog.findViewById(R.id.todoNote);

        addTodoButton = dialog.findViewById(R.id.addTodoButton);

        dateInput = dialog.findViewById(R.id.todoDueDate);
        dateInput.setInputType(InputType.TYPE_NULL);
        dateInput.setKeyListener(null);

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        dialog.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateInput.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);

                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!subjectSpinner.getText().toString().equals("") && !categorySpinner.getText().toString().equals("") && !todoTitleInput.getText().toString().equals("") && !dateInput.getText().toString().equals("")) {
                    String title, subject, category, dueDate, description;
                    title = todoTitleInput.getText().toString();
                    subject = subjectSpinner.getText().toString();
                    category = categorySpinner.getText().toString();
                    dueDate = dateInput.getText().toString();
                    description = noteInput.getText().toString();

                    FirebaseUser user = mAuth.getCurrentUser();

                    TodoItem todoItem = new TodoItem(title, subject, category, dueDate, description, Boolean.FALSE);

                    // nem mindenkeppen kell a .getInstace()-be a link de neha buta:( es kell neki
                    dbReference = FirebaseDatabase.getInstance("https://classmate-140fd-default-rtdb.firebaseio.com/").getReference();
                    // felhasznalo IDjevel rogziti a nevet databaseben
                    assert user != null;
                    // datum es ido megszerzese
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());
                    // A jelenlegi datum lesz a class field neve yyyyMMddHHmmss formatumban, mert az biztos h unique:D
                    dbReference.child("Todo").child(user.getUid() + "/" + currentDateandTime).setValue(todoItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(dialog.getContext(), "Task Added", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(dialog.getContext(), "Fields must not be null.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private ArrayList<String> getSubjectList() {
        ArrayList<String> subjects = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase.getInstance().getReference("Subjects/" + user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        subjects.add(Objects.requireNonNull(ds.child("className").getValue()).toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return subjects;

    }
}
