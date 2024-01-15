package com.hype.classmate.ui.timetabletest;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hype.classmate.R;
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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class TestTimetableFragment extends Fragment {

    private String[] day = {"Mon", "Tue", "Wen", "Thu", "Fri"};
    ArrayList<ArrayList<String>> orak = new ArrayList<ArrayList<String>>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<ScheduleEntity> scheduleList = new ArrayList<>();

    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

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

        /*FragmentWeekViewAdapter adapter = new FragmentWeekViewAdapter(viewModel::fetchEvents);
        view.getWeekView().setAdapter(adapter);
        binding.getWeekView().setMinDateAsLocalDate(YearMonth.now().atDay(1));
        binding.getWeekView().setMaxDateAsLocalDate(YearMonth.now().atEndOfMonth());
        viewModel.getViewState().observe(getViewLifecycleOwner(), viewState -> {
            adapter.submitList(viewState.getEntities());
        });*/

        MyCustomPagingAdapter adapter = new MyCustomPagingAdapter();
        weekView.setAdapter(adapter);

        /*

        Calendar endtime = Calendar.getInstance();
        endtime.add(Calendar.HOUR_OF_DAY, 2);
        MyEvent myEvent = new MyEvent(123421, "ASD", Calendar.getInstance(), endtime);
        events.add(myEvent);
        onCreateEntity(getContext(), myEvent);

        adapter.submitList(events);




        events.add(new MyEvent(1, "ASD", Calendar.getInstance(), endtime));
        */

        return view;
    }


    // ?????????
    public WeekViewEntity onCreateEntity(Context context, MyEvent item) {
        return new WeekViewEntity.Event.Builder(item)
                .setId(item.getId())
                .setTitle(item.getTitle())
                .setStartTime(item.getStartTime())
                .setEndTime(item.getEndTime())
                .build();
    }

    public void timetable() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orak.clear();
                if (snapshot.child("Subjects/" + user.getUid()).exists()) {
                    for (DataSnapshot classDataSnapshot : snapshot.child("Classes/" + user.getUid()).getChildren()) {
                        ArrayList<String> ora = new ArrayList<String>();
                        ora.add(0, Objects.requireNonNull(classDataSnapshot.child("subject").getValue()).toString());
                        ora.add(1, Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("teacher").getValue()).toString());
                        ora.add(2, Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("note").getValue()).toString());
                        ora.add(3, Objects.requireNonNull(classDataSnapshot.child("classroom").getValue()).toString());
                        ora.add(4, Objects.requireNonNull(classDataSnapshot.child("day").getValue()).toString());
                        ora.add(5, Objects.requireNonNull(classDataSnapshot.child("start").getValue()).toString());
                        ora.add(6, Objects.requireNonNull(classDataSnapshot.child("end").getValue()).toString());
                        ora.add(7, Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("color").getValue()).toString());
                        ora.add(8, Objects.requireNonNull(Objects.requireNonNull(classDataSnapshot.child("subject").getRef().getParent()).getKey()));

                        orak.add(ora);
                    }
                }
                final int COLUMN = 5;
                Comparator<ArrayList<String>> myComparator = new Comparator<ArrayList<String>>() {
                    @Override
                    public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                        try {
                            return new SimpleDateFormat("HH:mm").parse(o1.get(COLUMN)).compareTo(new SimpleDateFormat("HH:mm").parse(o2.get(COLUMN)));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };

                Collections.sort(orak, myComparator);
                for (int i = 0; i < orak.size(); i++) {
                    ScheduleEntity schedule = new ScheduleEntity(1, //originId
                            orak.get(i).get(0), //scheduleName
                            orak.get(i).get(3), //roomInfo
                            Arrays.asList(days).indexOf(orak.get(i).get(4)), //ScheduleDay object (MONDAY ~ SUNDAY)
                            orak.get(i).get(5), //startTime format: "HH:mm"
                            orak.get(i).get(6), //endTime  format: "HH:mm"
                            "#73fcae68", //backgroundColor (optional)
                            "#000000" //textcolor (optional)
                    );
                    scheduleList.add(schedule);
                }
                table.updateSchedules(scheduleList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}