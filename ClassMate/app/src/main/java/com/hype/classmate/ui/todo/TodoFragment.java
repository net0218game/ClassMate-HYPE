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

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class TodoFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    RecyclerView todayRecyclerView, pastRecyclerView, futureRecyclerView;
    FloatingActionButton addTodoButton;
    ArrayList<ArrayList<String>> todayTodoList = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> pastTodoList = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> futureTodoList = new ArrayList<ArrayList<String>>();
    private Menu menu;

    Boolean showDone = false;


    TextView noEventToday, noEventPast, noEventFuture;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        setHasOptionsMenu(true);
        getTodayTodoList();
        updateWidget();

        RecyclerView.Adapter<TodoAdapter.ViewHolder> todayAdapter = new TodoAdapter(todayTodoList);
        RecyclerView.Adapter<TodoAdapter.ViewHolder> pastAdapter = new TodoAdapter(pastTodoList);
        RecyclerView.Adapter<TodoAdapter.ViewHolder> futureAdapter = new TodoAdapter(futureTodoList);

        // LayoutManager beallitasa RecyclerView-hoz.
        todayRecyclerView = view.findViewById(R.id.today_recycler_view);
        todayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pastRecyclerView = view.findViewById(R.id.past_recycler_view);
        pastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        futureRecyclerView = view.findViewById(R.id.future_recycler_view);
        futureRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // adapter beallitasa
        todayRecyclerView.setAdapter(todayAdapter);
        pastRecyclerView.setAdapter(pastAdapter);
        futureRecyclerView.setAdapter(futureAdapter);

        noEventToday = view.findViewById(R.id.noEventToday);
        noEventPast = view.findViewById(R.id.noEventPast);
        noEventFuture = view.findViewById(R.id.noEventFuture);

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

    public void getTodayTodoList() {
        todayTodoList.clear();
        pastTodoList.clear();
        futureTodoList.clear();
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                todayTodoList.clear();
                pastTodoList.clear();
                futureTodoList.clear();
                if (snapshot.child("Todo/" + user.getUid()).exists()) {
                    for (DataSnapshot classDataSnapshot : snapshot.child("Todo/" + user.getUid()).getChildren()) {
                        ArrayList<String> todo = new ArrayList<String>();
                        String dateString = Objects.requireNonNull(classDataSnapshot.child("dueDate").getValue()).toString();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                        Calendar calendarTomorrow = Calendar.getInstance();
                        calendarTomorrow.add(Calendar.DAY_OF_YEAR, 1);
                        Date tomorrow = calendarTomorrow.getTime();

                        todo.add(0, Objects.requireNonNull(classDataSnapshot.child("title").getValue()).toString());
                        todo.add(1, Objects.requireNonNull(classDataSnapshot.child("subject").getValue()).toString());
                        todo.add(2, Objects.requireNonNull(classDataSnapshot.child("description").getValue()).toString());
                        todo.add(3, Objects.requireNonNull(classDataSnapshot.child("dueDate").getValue()).toString());
                        todo.add(4, Objects.requireNonNull(classDataSnapshot.child("done").getValue()).toString());
                        todo.add(5, Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("color").getValue()).toString());
                        todo.add(6, classDataSnapshot.getKey());
                        todo.add(7, Objects.requireNonNull(classDataSnapshot.child("category").getValue()).toString());

                        if (showDone) {
                            // show tasks that are done
                            try {
                                Date date = format.parse(dateString);
                                if (DateUtils.isToday(date.getTime())) {
                                    // Event is Today
                                    todayTodoList.add(todo);
                                } else if (new Date().after(date) && !Objects.equals(date, new Date())) {
                                    // Past Event
                                    pastTodoList.add(todo);
                                } else if (new Date().before(date) && date != tomorrow) {
                                    // Future events
                                    futureTodoList.add(todo);
                                }
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            // hide tasks that are done
                            // TODO: Bug - kipipalja azt ami meg nincsen keszen
                            if (!Boolean.parseBoolean(todo.get(4))) {
                                try {
                                    Date date = format.parse(dateString);
                                    if (DateUtils.isToday(date.getTime())) {
                                        // Event is Today
                                        todayTodoList.add(todo);
                                    } else if (new Date().after(date) && !Objects.equals(date, new Date())) {
                                        // Past Event
                                        pastTodoList.add(todo);
                                    } else if (new Date().before(date) && date != tomorrow) {
                                        // Future events
                                        futureTodoList.add(todo);
                                    }
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }

                todayRecyclerView.getAdapter().notifyDataSetChanged();
                pastRecyclerView.getAdapter().notifyDataSetChanged();
                futureRecyclerView.getAdapter().notifyDataSetChanged();

                noEventToday.setVisibility(todayTodoList.isEmpty() ? View.VISIBLE : View.GONE);
                noEventPast.setVisibility(pastTodoList.isEmpty() ? View.VISIBLE : View.GONE);
                noEventFuture.setVisibility(futureTodoList.isEmpty() ? View.VISIBLE : View.GONE);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.todo_menu, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.todo_viewmode_action) {
            // Show / Hide Done Tasks
            if (showDone) {
                menu.getItem(0).setIcon(R.drawable.baseline_check_box_outline_blank_24);
                showDone = false;
                getTodayTodoList();
            } else {
                menu.getItem(0).setIcon(R.drawable.baseline_check_box_24);
                showDone = true;
                getTodayTodoList();
            }

        } else if (item.getItemId() == R.id.todo_archiveDone_action) {
            // Archive Done Tasks
        } else if (item.getItemId() == R.id.todo_delete_action) {
            // Delete All Tasks
        }

        return super.onOptionsItemSelected(item);
    }
}