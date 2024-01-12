package com.hype.classmate.ui.home;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hype.classmate.MainActivity;
import com.hype.classmate.R;
import com.hype.classmate.databinding.FragmentHomeBinding;
import com.hype.classmate.ui.login.LoginActivity;
import com.hype.classmate.widgets.timetable.TimetableWidget;
import com.hype.classmate.widgets.todo.TodoWidget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    RecyclerView classRecyclerView, todoRecyclerView;
    ArrayList<ArrayList<String>> homeClassList = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> homeTodoList = new ArrayList<ArrayList<String>>();
    TextView title;
    CardView classesCard, todoCard;
    Button updateWidgetsButton;
    LinearLayout emptyTodoList, emptyClassList;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        updateWidget();

        RecyclerView.Adapter<HomeClassAdapter.ViewHolder> homeClassAdapter = new HomeClassAdapter(homeClassList);
        RecyclerView.Adapter<HomeTodoAdapter.ViewHolder> homeTodoAdapter = new HomeTodoAdapter(homeTodoList);

        // LayoutManager beallitasa RecyclerView-hoz.
        classRecyclerView = view.findViewById(R.id.homeClassRecyclerView);
        classRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // adapter beallitasa
        classRecyclerView.setAdapter(homeClassAdapter);

        todoRecyclerView = view.findViewById(R.id.homeTodoRecyclerView);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // adapter beallitasa
        todoRecyclerView.setAdapter(homeTodoAdapter);


        emptyTodoList = view.findViewById(R.id.emptyTasks);
        emptyClassList = view.findViewById(R.id.emptyClasses);


        title = view.findViewById(R.id.homeTitle);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            getClasses();
            getTodoList();
            FirebaseDatabase.getInstance().getReference("Users/" + user.getUid()).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    title.setText("Welcome " + Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
        }

        classesCard = view.findViewById(R.id.classesCard);
        todoCard = view.findViewById(R.id.todoCard);
        updateWidgetsButton = view.findViewById(R.id.button4);

        updateWidgetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWidget();

                int reqCode = 1;
                Intent intent = new Intent(getContext(), MainActivity.class);
                MainActivity.showNotification(getContext(), "SZIA SZILAAARD", "MUKODIK!!!Ez egy ClassMate ertesites!!! Szia Szilard!!!!", intent, reqCode);
            }
        });

        // TODO: Onclick listener
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getClasses() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                homeClassList.clear();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                Date date = new Date();
                String day = simpleDateFormat.format(date);
                if (day.equals("Saturday") || day.equals("Sunday")) {
                    day = "Monday";
                }
                updateWidget();

                if (snapshot.child("Subjects/" + user.getUid()).exists()) {
                    for (DataSnapshot classDataSnapshot : snapshot.child("Classes/" + user.getUid()).getChildren()) {
                        if (Objects.requireNonNull(classDataSnapshot.child("day").getValue()).toString().equals(day)) {
                            ArrayList<String> ora = new ArrayList<String>();
                            ora.add(0, Objects.requireNonNull(classDataSnapshot.child("subject").getValue()).toString());
                            ora.add(1, Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("color").getValue()).toString());
                            ora.add(2, Objects.requireNonNull(Objects.requireNonNull(classDataSnapshot.child("subject").getRef().getParent()).getKey()));
                            ora.add(3, Objects.requireNonNull(classDataSnapshot.child("start").getValue()).toString());
                            ora.add(4, Objects.requireNonNull(classDataSnapshot.child("end").getValue()).toString());

                            homeClassList.add(ora);
                        }
                    }
                }
                final int COLUMN = 3;
                Comparator<ArrayList<String>> myComparator = new Comparator<ArrayList<String>>() {
                    @Override
                    public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                        try {
                            return new SimpleDateFormat("HH:mm").parse(o1.get(COLUMN)).compareTo(new SimpleDateFormat("HH:mm").parse(o2.get(COLUMN)));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };

                Collections.sort(homeClassList, myComparator);

                classRecyclerView.getAdapter().notifyDataSetChanged();

                if (homeClassList.isEmpty()) {
                    emptyClassList.setVisibility(View.VISIBLE);
                    classRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyClassList.setVisibility(View.GONE);
                    classRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getTodoList() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                homeTodoList.clear();
                if (snapshot.child("Todo/" + user.getUid()).exists()) {
                    for (DataSnapshot classDataSnapshot : snapshot.child("Todo/" + user.getUid()).getChildren()) {
                        if (Objects.requireNonNull(classDataSnapshot.child("done").getValue()).toString().equals("false")) {
                            ArrayList<String> todo = new ArrayList<String>();
                            todo.add(0, Objects.requireNonNull(classDataSnapshot.child("title").getValue()).toString());
                            todo.add(1, Objects.requireNonNull(classDataSnapshot.child("category").getValue()).toString());
                            homeTodoList.add(todo);
                        }
                    }
                }
                todoRecyclerView.getAdapter().notifyDataSetChanged();
                if (homeTodoList.isEmpty()) {
                    emptyTodoList.setVisibility(View.VISIBLE);
                    todoRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyTodoList.setVisibility(View.GONE);
                    todoRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void updateWidget() {
        Context context = requireContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        ComponentName todoWidget = new ComponentName(context, TodoWidget.class);
        int[] todoAppWidgetIds = appWidgetManager.getAppWidgetIds(todoWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(todoAppWidgetIds, R.id.widgetListView);

        ComponentName timetableWidget = new ComponentName(context, TimetableWidget.class);
        int[] timetableAppWidgetIds = appWidgetManager.getAppWidgetIds(timetableWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(timetableAppWidgetIds, R.id.timetableWidgetListView);
    }
}