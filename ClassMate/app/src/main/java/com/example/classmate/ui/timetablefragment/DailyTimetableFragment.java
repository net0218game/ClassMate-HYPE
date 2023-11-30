package com.example.classmate.ui.timetablefragment;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.classmate.R;
import com.example.classmate.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class DailyTimetableFragment extends Fragment {
    TabLayout tabLayout;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    ArrayList<ArrayList<String>> orak = new ArrayList<ArrayList<String>>();
    RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        timetable();

        View view = inflater.inflate(R.layout.fragment_daily_timetable, container, false);

        tabLayout = requireActivity().findViewById(R.id.tabLayout);

        RecyclerView.Adapter<ClassAdapter.ViewHolder> adapter = new ClassAdapter(orak);

        // LayoutManager beallitasa RecyclerView-hoz.
        recyclerView
                = view.findViewById(R.id.timetable_recycler_view);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        // adapter beallitasa
        recyclerView.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                timetable();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }

    public void timetable() {
        orak.clear();

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Subjects/" + user.getUid()).exists()) {
                    for (DataSnapshot classDataSnapshot : snapshot.child("Classes/" + user.getUid()).getChildren()) {
                        if (Objects.requireNonNull(classDataSnapshot.child("day").getValue()).toString().equals(days[tabLayout.getSelectedTabPosition()])) {
                            ArrayList<String> ora = new ArrayList<String>();
                            ora.add(0, Objects.requireNonNull(classDataSnapshot.child("subject").getValue()).toString());
                            ora.add(1, Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("teacher").getValue()).toString());
                            ora.add(2, Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("note").getValue()).toString());
                            ora.add(3, Objects.requireNonNull(classDataSnapshot.child("classroom").getValue()).toString());
                            ora.add(4, Objects.requireNonNull(classDataSnapshot.child("day").getValue()).toString());
                            ora.add(5, Objects.requireNonNull(classDataSnapshot.child("start").getValue()).toString());
                            ora.add(6, Objects.requireNonNull(classDataSnapshot.child("end").getValue()).toString());
                            ora.add(7, Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("color").getValue()).toString());
                            ora.add(8, Objects.requireNonNull(classDataSnapshot.child("subject").getRef().getParent().getKey()).toString());


                            Log.d("orarend", "Egyezes" + days[tabLayout.getSelectedTabPosition()] + ora);
                            orak.add(ora);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        orak.clear();

    }
}