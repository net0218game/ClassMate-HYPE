package com.hype.classmate.ui.timetabletest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alamkanak.weekview.WeekView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hype.classmate.R;
import com.hype.classmate.ui.AddClassActivity;
import com.hype.classmate.ui.AddSubjectActivity;
import com.hype.classmate.ui.dialog.AddClassDialog;
import com.hype.classmate.ui.dialog.AddSubjectDialog;
import com.hype.classmate.ui.dialog.ClassDetailsDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class TestTimetableFragment extends Fragment {

    ArrayList<ArrayList<String>> orak = new ArrayList<ArrayList<String>>();
    FloatingActionButton addClassButton, addSubjectButton;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    MyCustomPagingAdapter adapter = new MyCustomPagingAdapter();
    WeekView weekView;
    private Menu menu;
    ArrayList<MyEvent> events = new ArrayList<>();

    public TestTimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timetable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test_timetable, container, false);

        setHasOptionsMenu(true);

        weekView = view.findViewById(R.id.weekView);
        addClassButton = view.findViewById(R.id.addClassButton);
        addSubjectButton = view.findViewById(R.id.addSubjectButton);

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addSubjectButton.getVisibility() == View.GONE) {
                    addSubjectButton.setVisibility(View.VISIBLE);
                } else if (addSubjectButton.getVisibility() == View.VISIBLE) {
                    AddClassDialog addClassDialog = new AddClassDialog();
                    addClassDialog.showDialog((Activity) getContext());
                }
            }
        });

        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSubjectDialog addSubjectDialog = new AddSubjectDialog();
                addSubjectDialog.showDialog((Activity) getContext());

                /*Intent intent = new Intent(getActivity(), AddSubjectActivity.class);
                startActivity(intent);*/
            }
        });

        weekView.setHorizontalScrollingEnabled(true);
        weekView.setShowFirstDayOfWeekFirst(true);

        Calendar currentMonday = Calendar.getInstance();
        currentMonday.set(Calendar.DAY_OF_WEEK, 2);
        currentMonday.set(Calendar.HOUR_OF_DAY, 9);
        weekView.scrollToDateTime(currentMonday);
        weekView.setMinDate(currentMonday);

        currentMonday.add(Calendar.DAY_OF_WEEK, 6);
        weekView.setMaxDate(currentMonday);

        weekView.setAdapter(adapter);

        return view;
    }

    public void timetable() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orak.clear();
                events.clear();
                if (snapshot.child("Subjects/" + user.getUid()).exists()) {
                    for (DataSnapshot classDataSnapshot : snapshot.child("Classes/" + user.getUid()).getChildren()) {
                        String title = Objects.requireNonNull(classDataSnapshot.child("subject").getValue()).toString();
                        String subTitle = Objects.requireNonNull(classDataSnapshot.child("classroom").getValue()).toString();
                        String day = Objects.requireNonNull(Objects.requireNonNull(classDataSnapshot.child("day").getValue()).toString());
                        String color = Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("color").getValue()).toString();
                        String[] startTimeString = Objects.requireNonNull(classDataSnapshot.child("start").getValue()).toString().split(":");
                        String[] endTimeString = Objects.requireNonNull(classDataSnapshot.child("end").getValue()).toString().split(":");
                        long classId = Long.parseLong(Objects.requireNonNull(classDataSnapshot.getKey()));

                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.DAY_OF_WEEK, Arrays.asList(days).indexOf(day) + 2);
                        startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeString[0]));
                        startTime.set(Calendar.MINUTE, Integer.parseInt(startTimeString[1]));

                        Calendar endTime = Calendar.getInstance();
                        endTime.set(Calendar.DAY_OF_WEEK, Arrays.asList(days).indexOf(day) + 2);
                        endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimeString[0]));
                        endTime.set(Calendar.MINUTE, Integer.parseInt(endTimeString[1]));


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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.timetable_menu, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.timetable_viewmode_action) {
            // Day or week view
            if (weekView.getNumberOfVisibleDays() == 5) {
                weekView.setNumberOfVisibleDays(1);
                weekView.scrollToDate(Calendar.getInstance());
                menu.getItem(0).setIcon(R.drawable.baseline_calendar_month_24);
            } else if (weekView.getNumberOfVisibleDays() == 1) {
                weekView.setNumberOfVisibleDays(5);
                menu.getItem(0).setIcon(R.drawable.baseline_calendar_view_day_24);
            }
        } else if (item.getItemId() == R.id.timetable_today_action) {
            // Jump to today
            weekView.scrollToDate(Calendar.getInstance());
        }

        return super.onOptionsItemSelected(item);
    }
}