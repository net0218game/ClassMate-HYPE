package com.example.classmate.widgets.timetable;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.classmate.widgets.todo.TodoWidgetDataProvider;

public class TimetableWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TimetableWidgetDataProvider(this, intent);
    }
}
