package com.example.classmate.widgets;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;

import com.example.classmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TodoWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    ArrayList<String> todoList = new ArrayList<String>();
    Context mContext;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public TodoWidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        getTodoList();
    }

    @Override
    public void onDataSetChanged() {
        getTodoList();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return todoList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_todo_item);
        view.setTextViewText(R.id.widgetTodoItem, todoList.get(position));
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void getTodoList() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                todoList.clear();
                if (snapshot.child("Todo/" + user.getUid()).exists()) {
                    for (DataSnapshot classDataSnapshot : snapshot.child("Todo/" + user.getUid()).getChildren()) {
                        if (Objects.requireNonNull(classDataSnapshot.child("done").getValue()).toString().equals("false")) {
                            todoList.add(Objects.requireNonNull(classDataSnapshot.child("title").getValue()).toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
