package com.hype.classmate.ui.timetabletest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hype.classmate.R;
import com.islandparadise14.mintable.MinTimeTableView;
import com.islandparadise14.mintable.model.ScheduleDay;
import com.islandparadise14.mintable.model.ScheduleEntity;
import com.islandparadise14.mintable.schedule.ScheduleView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Arrays;

public class TestTimetableFragment extends Fragment {

    private String[] day = {"Mon", "Tue", "Wen", "Thu", "Fri"};

    public TestTimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test_timetable, container, false);


        MinTimeTableView table = view.findViewById(R.id.table);
        table.initTable(day);

        ArrayList<ScheduleEntity> scheduleList = new ArrayList<>();
        ScheduleEntity schedule = new ScheduleEntity(
                1, //originId
                "SZIA SZILARD", //scheduleName
                "TMG", //roomInfo
                ScheduleDay.TUESDAY, //ScheduleDay object (MONDAY ~ SUNDAY)
                "8:20", //startTime format: "HH:mm"
                "10:30", //endTime  format: "HH:mm"
                "#73fcae68", //backgroundColor (optional)
                "#000000" //textcolor (optional)
        );
        scheduleList.add(schedule);
        table.updateSchedules(scheduleList);

        return view;
    }
}