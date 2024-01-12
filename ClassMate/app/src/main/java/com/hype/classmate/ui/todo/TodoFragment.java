package com.hype.classmate.ui.todo;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hype.classmate.R;
import com.hype.classmate.ui.AddTodoActivity;
import com.hype.classmate.widgets.todo.TodoWidget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class TodoFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    RecyclerView recyclerView;
    FloatingActionButton addTodoButton;
    ArrayList<ArrayList<String>> todoList = new ArrayList<ArrayList<String>>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        getTodoList();
        updateWidget();

        RecyclerView.Adapter<TodoAdapter.ViewHolder> adapter = new TodoAdapter(todoList);

        // LayoutManager beallitasa RecyclerView-hoz.
        recyclerView
                = view.findViewById(R.id.todo_recycler_view);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        // adapter beallitasa
        recyclerView.setAdapter(adapter);

        addTodoButton = view.findViewById(R.id.addTodoButton);
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTodoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void getTodoList() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                todoList.clear();
                if (snapshot.child("Todo/" + user.getUid()).exists()) {
                    for (DataSnapshot classDataSnapshot : snapshot.child("Todo/" + user.getUid()).getChildren()) {
                        ArrayList<String> todo = new ArrayList<String>();
                        todo.add(0, Objects.requireNonNull(classDataSnapshot.child("title").getValue()).toString());
                        todo.add(1, Objects.requireNonNull(classDataSnapshot.child("subject").getValue()).toString());
                        todo.add(2, Objects.requireNonNull(classDataSnapshot.child("description").getValue()).toString());
                        todo.add(3, Objects.requireNonNull(classDataSnapshot.child("dueDate").getValue()).toString());
                        todo.add(4, Objects.requireNonNull(classDataSnapshot.child("done").getValue()).toString());
                        todo.add(5, Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("color").getValue()).toString());
                        todo.add(6, Objects.requireNonNull(Objects.requireNonNull(classDataSnapshot.child("subject").getRef().getParent()).getKey()));
                        todo.add(7, Objects.requireNonNull(classDataSnapshot.child("category").getValue()).toString());
                        Log.d("todo list", todo.toString());
                        todoList.add(todo);
                    }
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                updateWidget();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void updateWidget() {
        if (getActivity() != null) {
            Context context = this.getContext();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            assert context != null;
            ComponentName todoWidget = new ComponentName(context, TodoWidget.class);
            int[] todoAppWidgetIds = appWidgetManager.getAppWidgetIds(todoWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(todoAppWidgetIds, R.id.widgetListView);
        }
    }
}