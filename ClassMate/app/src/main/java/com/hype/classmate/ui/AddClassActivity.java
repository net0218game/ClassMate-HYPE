package com.hype.classmate.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hype.classmate.R;
import com.hype.classmate.interfaces.TimetableClass;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddClassActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference dbReference;
    Button addClassButton;
    TextInputEditText classroomInput, startInput, endInput;
    AutoCompleteTextView subjectSpinner, daySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        mAuth = FirebaseAuth.getInstance();

        addClassButton = findViewById(R.id.addClassButton);

        subjectSpinner = findViewById(R.id.subjectTextView);
        daySpinner = findViewById(R.id.dayTextView);
        classroomInput = findViewById(R.id.classClassroom);
        startInput = findViewById(R.id.classStartTime);
        endInput = findViewById(R.id.classEndTime);

        startInput.setInputType(InputType.TYPE_NULL);
        startInput.setKeyListener(null);

        endInput.setInputType(InputType.TYPE_NULL);
        endInput.setKeyListener(null);

        startInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting the
                // instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddClassActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.
                                startInput.setText(hourOfDay + ":" + minute);

                            }
                        }, hour, minute, true);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }
        });

        endInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting the
                // instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddClassActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.
                                endInput.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, true);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }
        });

        ArrayList<String> subjectList = getSubjectList();
        ArrayList<String> dayList = getDayList();

        //Create adapter
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(AddClassActivity.this, R.layout.spinner_item, subjectList);
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(AddClassActivity.this, R.layout.spinner_item, dayList);

        //Set adapter
        subjectSpinner.setAdapter(subjectAdapter);
        daySpinner.setAdapter(dayAdapter);

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String subject, classroom, day, start, end;
                subject = subjectSpinner.getText().toString();
                classroom = classroomInput.getText().toString();
                day = daySpinner.getText().toString();
                start = startInput.getText().toString();
                end = endInput.getText().toString();

                FirebaseUser user = mAuth.getCurrentUser();

                TimetableClass timetableClass = new TimetableClass(subject, classroom, day, start, end);

                // nem mindenkeppen kell a .getInstace()-be a link de neha buta:( es kell neki
                dbReference = FirebaseDatabase.getInstance("https://classmate-140fd-default-rtdb.firebaseio.com/").getReference();
                // felhasznalo IDjevel rogziti a nevet databaseben
                assert user != null;
                // datum es ido megszerzese
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                // A jelenlegi datum lesz a class field neve yyyyMMddHHmmss formatumban, mert az biztos h unique:D
                dbReference.child("Classes").child(user.getUid() + "/" + currentDateandTime).setValue(timetableClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        subjectSpinner.setText("");
                        classroomInput.setText("");
                        daySpinner.setText("");
                        startInput.setText("");
                        endInput.setText("");
                        Toast.makeText(AddClassActivity.this, "Class Added", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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

    private ArrayList<String> getDayList() {
        ArrayList<String> days = new ArrayList<>();
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        return days;
    }
}