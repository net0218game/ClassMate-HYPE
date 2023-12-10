package com.example.classmate.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classmate.R;
import com.example.classmate.databinding.FragmentHomeBinding;
import com.example.classmate.ui.login.LoginActivity;
import com.example.classmate.ui.todo.TodoAdapter;
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

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        RecyclerView.Adapter<HomeClassAdapter.ViewHolder> homeClassAdapter = new HomeClassAdapter(homeClassList);
        RecyclerView.Adapter<HomeTodoAdapter.ViewHolder> homeTodoAdapter = new HomeTodoAdapter(homeTodoList);

        // LayoutManager beallitasa RecyclerView-hoz.
        classRecyclerView
                = view.findViewById(R.id.homeClassRecyclerView);
        classRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        // adapter beallitasa
        classRecyclerView.setAdapter(homeClassAdapter);

        todoRecyclerView
                = view.findViewById(R.id.homeTodoRecyclerView);
        todoRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        // adapter beallitasa
        todoRecyclerView.setAdapter(homeTodoAdapter);

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}