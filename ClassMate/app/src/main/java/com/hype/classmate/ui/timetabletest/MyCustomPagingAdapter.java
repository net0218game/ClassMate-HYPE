package com.hype.classmate.ui.timetabletest;

import android.content.Context;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEntity;

import java.util.Calendar;

public class MyCustomPagingAdapter extends WeekView.PagingAdapter<MyEvent> {

    public WeekViewEntity onCreateEntity(Context context, MyEvent item) {
        return new WeekViewEntity.Event.Builder(item)
                .setId(item.getId())
                .setTitle(item.getTitle())
                .setStartTime(item.getStartTime())
                .setEndTime(item.getEndTime())
                .build();
    }
}
