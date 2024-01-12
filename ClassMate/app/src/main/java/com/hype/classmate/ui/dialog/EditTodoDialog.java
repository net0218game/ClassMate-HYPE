package com.hype.classmate.ui.dialog;

import static java.util.Arrays.asList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hype.classmate.R;
import com.hype.classmate.interfaces.TodoItem;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class EditTodoDialog {

    FirebaseAuth mAuth;
    DatabaseReference dbReference;

    public void showDialog(Activity activity, String id, String msg, String title, String category, String subject, String date, String description) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.edit_todo_dialog);

        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        dialogTitle.setText(msg);

        TextInputEditText titleInput = dialog.findViewById(R.id.todoTitle);
        titleInput.setText(title);

        AutoCompleteTextView categoryInput = dialog.findViewById(R.id.todoCategory);
        categoryInput.setText(category);

        AutoCompleteTextView subjectInput = dialog.findViewById(R.id.subjectTextView);
        subjectInput.setText(subject);

        TextInputEditText dueDateInput = dialog.findViewById(R.id.todoDueDate);
        dueDateInput.setText(date);

        TextInputEditText descriptionInput = dialog.findViewById(R.id.todoNote);
        descriptionInput.setText(description);

        Button closeDialogButton = dialog.findViewById(R.id.cancelEditButton);
        Button editButton = dialog.findViewById(R.id.editClassButton);

        ArrayList<String> categoryList = new ArrayList<>(asList("Homework", "Event", "Exam"));
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(dialog.getContext(), R.layout.spinner_item, categoryList);
        categoryInput.setAdapter(categoryAdapter);

        ArrayList<String> subjectList = getSubjectList();
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(dialog.getContext(), R.layout.spinner_item, subjectList);
        subjectInput.setAdapter(subjectAdapter);

        dueDateInput.setInputType(InputType.TYPE_NULL);
        dueDateInput.setKeyListener(null);
        dueDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        dialog.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dueDateInput.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoItem todoItem = new TodoItem(titleInput.getText().toString(), subjectInput.getText().toString(), categoryInput.getText().toString(), dueDateInput.getText().toString(), descriptionInput.getText().toString(), false);
                dbReference = FirebaseDatabase.getInstance().getReference();
                dbReference.child("Todo").child(user.getUid() + "/" + id).setValue(todoItem);
                dialog.dismiss();
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
