package com.hype.classmate.ui.timetable;

import androidx.lifecycle.ViewModelProvider;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hype.classmate.R;
import com.hype.classmate.ViewPagerAdapter;
import com.hype.classmate.ui.AddSubjectActivity;
import com.hype.classmate.widgets.timetable.TimetableWidget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class TimetableFragment extends Fragment {

    private TimetableViewModel mViewModel;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter myviewPagerAdapter;
    FloatingActionButton addClassButton;

    public static TimetableFragment newInstance() {
        return new TimetableFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager2);
        // Set the adapter for the viewpager
        myviewPagerAdapter = new ViewPagerAdapter(this.requireActivity());
        viewPager2.setAdapter(myviewPagerAdapter);

        updateWidget();

        addClassButton = view.findViewById(R.id.floatingActionButton);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddSubjectActivity.class);
                startActivity(intent);
            }
        }

        );
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TimetableViewModel.class);
        // TODO: Use the ViewModel
    }

    public void updateWidget() {
        if (getActivity() != null) {
            Context context = this.getContext();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            assert context != null;
            ComponentName timetableWidget = new ComponentName(context, TimetableWidget.class);
            int[] todoAppWidgetIds = appWidgetManager.getAppWidgetIds(timetableWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(todoAppWidgetIds, R.id.timetableWidgetListView);
        }
    }
}