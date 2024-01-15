package com.hype.classmate.ui.timetabletest;

import java.util.Calendar;

public class MyEvent {
    long id;
    String title;
    Calendar startTime;
    Calendar endTime;

    public MyEvent(long id, String title, Calendar startTime, Calendar endTime) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }
}
