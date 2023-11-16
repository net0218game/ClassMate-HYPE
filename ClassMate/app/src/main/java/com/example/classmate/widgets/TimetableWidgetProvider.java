package com.example.classmate.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.classmate.MainActivity;
import com.example.classmate.R;

public class TimetableWidgetProvider extends AppWidgetProvider {
    /*
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timetable_widget);
            views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    } */
}
