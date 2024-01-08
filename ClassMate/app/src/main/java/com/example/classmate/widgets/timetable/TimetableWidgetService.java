package com.example.classmate.widgets.timetable;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class TimetableWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TimetableWidgetDataProvider(this, intent);
    }
}
