package com.hype.classmate.ui.timetabletest;

import com.alamkanak.weekview.WeekViewEntity;

import java.util.Calendar;

public class MyEvent {
    long id;
    String title, subTitle;
    Calendar startTime;
    Calendar endTime;
    Integer color;

    public MyEvent() {
    }


    public MyEvent(long id, String title, String subTitle, Calendar startTime, Calendar endTime, Integer color) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

}

