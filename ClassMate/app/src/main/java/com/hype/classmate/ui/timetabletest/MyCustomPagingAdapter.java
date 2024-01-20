package com.hype.classmate.ui.timetabletest;

import android.content.Context;
import android.graphics.Color;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEntity;
import com.hype.classmate.R;

import java.util.Calendar;

public class MyCustomPagingAdapter extends WeekView.PagingAdapter<MyEvent> {
    @Override
    public WeekViewEntity onCreateEntity(MyEvent item) {
        WeekViewEntity.Style style = new WeekViewEntity.Style.Builder()
                .setBackgroundColor(item.getColor())
                .setCornerRadius(20)
                .build();
        return new WeekViewEntity.Event.Builder(item)
                .setId(item.getId())
                .setStyle(style)
                .setTitle(item.getTitle())
                .setSubtitle(item.getSubTitle())
                .setStartTime(item.getStartTime())
                .setEndTime(item.getEndTime())
                .build();
    }
}
