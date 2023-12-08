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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    RecyclerView recyclerView;
    ArrayList<ArrayList<String>> homeClassList = new ArrayList<ArrayList<String>>();

    TextView title;

    CardView classesCard, todoCard;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        RecyclerView.Adapter<HomeClassAdapter.ViewHolder> adapter = new HomeClassAdapter(homeClassList);

        // LayoutManager beallitasa RecyclerView-hoz.
        recyclerView
                = view.findViewById(R.id.homeClassRecyclerView);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        // adapter beallitasa
        recyclerView.setAdapter(adapter);

        title = view.findViewById(R.id.homeTitle);
        if (user != null) {
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

    public void timetable() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                homeClassList.clear();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                Date date = new Date();
                String day = simpleDateFormat.format(date).toLowerCase();
                if(snapshot.child("Subjects/" + user.getUid()).exists()) {
                    for (DataSnapshot classDataSnapshot : snapshot.child("Classes/" + user.getUid()).getChildren()) {
                        if (Objects.requireNonNull(classDataSnapshot.child("day").getValue()).toString().equals(day)) {
                            ArrayList<String> ora = new ArrayList<String>();
                            ora.add(0, Objects.requireNonNull(classDataSnapshot.child("subject").getValue()).toString());
                            ora.add(1, Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("color").getValue()).toString());
                            ora.add(2, Objects.requireNonNull(Objects.requireNonNull(classDataSnapshot.child("subject").getRef().getParent()).getKey()));

                            homeClassList.add(ora);
                        }
                    }
                }

                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}