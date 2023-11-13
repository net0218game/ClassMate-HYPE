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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.classmate.R;
import com.example.classmate.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class DailyTimetableFragment extends Fragment {

    private DailyTimetableViewModel mViewModel;

    public static DailyTimetableFragment newInstance() {
        return new DailyTimetableFragment();
    }

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter myviewPagerAdapter;
    TextView title;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_daily_timetable, container, false);

        tabLayout = getActivity().findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //title.setText("Timetable for " + tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        String[] orak = {"Matek", "Angol", "Info", "Töri", "Magyar", "Tesi", "Német", "Fizika"};
        RecyclerView.Adapter<ClassAdapter.ViewHolder> adapter = new ClassAdapter(orak);

        // LayoutManager beallitasa RecyclerView-hoz.
        RecyclerView recyclerView
                = view.findViewById(R.id.timetable_recycler_view);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        // adapter beallitasa
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DailyTimetableViewModel.class);
        // TODO: Use the ViewModel
    }

}