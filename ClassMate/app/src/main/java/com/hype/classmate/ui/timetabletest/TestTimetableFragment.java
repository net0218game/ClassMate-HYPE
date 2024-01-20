package com.hype.classmate.ui.timetabletest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEntity;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hype.classmate.R;
import com.hype.classmate.ui.AddSubjectActivity;
import com.islandparadise14.mintable.MinTimeTableView;
import com.islandparadise14.mintable.model.ScheduleEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class TestTimetableFragment extends Fragment {

    private String[] day = {"Mon", "Tue", "Wen", "Thu", "Fri"};
    ArrayList<ArrayList<String>> orak = new ArrayList<ArrayList<String>>();
    Integer classId = 0;
    FloatingActionButton addClassButton;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<ScheduleEntity> scheduleList = new ArrayList<>();

    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    MyCustomPagingAdapter adapter = new MyCustomPagingAdapter();


    MinTimeTableView table;

    Button todayButton, changeViewButton;

    ArrayList<MyEvent> events = new ArrayList<>();

    public TestTimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test_timetable, container, false);

        WeekView weekView = view.findViewById(R.id.weekView);
        todayButton = view.findViewById(R.id.todayButton);
        changeViewButton = view.findViewById(R.id.changeView);
        addClassButton = view.findViewById(R.id.addClassButton);

        // Jump to today
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekView.scrollToDate(Calendar.getInstance());
            }
        });

        // Day or week view
        changeViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weekView.getNumberOfVisibleDays() == 5) {
                    weekView.setNumberOfVisibleDays(1);
                    changeViewButton.setText("Week");

                } else if (weekView.getNumberOfVisibleDays() == 1) {
                    weekView.setNumberOfVisibleDays(5);
                    changeViewButton.setText("Day");

                }
            }
        });

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddSubjectActivity.class);
                startActivity(intent);
            }
        });


        weekView.setHorizontalScrollingEnabled(true);
        weekView.setShowFirstDayOfWeekFirst(true);

        Calendar currentMonday = Calendar.getInstance();
        currentMonday.set(Calendar.DAY_OF_WEEK, 2);
        weekView.scrollToDate(currentMonday);
        weekView.setMinDate(currentMonday);

        weekView.setMinHour(7);
        weekView.setMaxHour(15);

        currentMonday.add(Calendar.DAY_OF_WEEK, 6);
        weekView.setMaxDate(currentMonday);

        weekView.setAdapter(adapter);

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_WEEK, 2);

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.DAY_OF_WEEK, 2);
        endTime.add(Calendar.HOUR_OF_DAY, 2);

        timetable();
        return view;
    }

    public void timetable() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orak.clear();
                if (snapshot.child("Subjects/" + user.getUid()).exists()) {
                    for (DataSnapshot classDataSnapshot : snapshot.child("Classes/" + user.getUid()).getChildren()) {
                        String title = Objects.requireNonNull(classDataSnapshot.child("subject").getValue()).toString();
                        String subTitle = Objects.requireNonNull(classDataSnapshot.child("classroom").getValue()).toString();
                        String day = Objects.requireNonNull(Objects.requireNonNull(classDataSnapshot.child("day").getValue()).toString());
                        String color = Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("color").getValue()).toString();
                        String[] startTimeString = Objects.requireNonNull(classDataSnapshot.child("start").getValue()).toString().split(":");
                        String[] endTimeString = Objects.requireNonNull(classDataSnapshot.child("end").getValue()).toString().split(":");
                        classId += 1;

                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.DAY_OF_WEEK, Arrays.asList(days).indexOf(day) + 2);
                        startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeString[0]));
                        startTime.set(Calendar.MINUTE, Integer.parseInt(startTimeString[1]));

                        Calendar endTime = Calendar.getInstance();
                        endTime.set(Calendar.DAY_OF_WEEK, Arrays.asList(days).indexOf(day) + 2);
                        endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimeString[0]));
                        endTime.set(Calendar.MINUTE, Integer.parseInt(endTimeString[1]));


                        Log.d("szin", color + " " + String.valueOf(Color.parseColor(color)));

                        events.add(new MyEvent(classId, title, subTitle, startTime, endTime, Color.parseColor(color)));

                        /* ora.add(1, Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("teacher").getValue()).toString());
                        ora.add(2, Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("note").getValue()).toString());
                        ora.add(4, Objects.requireNonNull(classDataSnapshot.child("day").getValue()).toString());
                        ora.add(7, Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("color").getValue()).toString());
                        ora.add(8, Objects.requireNonNull(Objects.requireNonNull(classDataSnapshot.child("subject").getRef().getParent()).getKey()));*/

                    }
                    adapter.submitList(events);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}