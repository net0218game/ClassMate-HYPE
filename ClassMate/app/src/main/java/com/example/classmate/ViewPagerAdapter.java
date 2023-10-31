package com.example.classmate;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.classmate.ui.calendar.CalendarFragment;
import com.example.classmate.ui.home.HomeFragment;
import com.example.classmate.ui.settings.SettingsFragment;
import com.example.classmate.ui.timetablefragment.DailyTimetableFragment;
import com.example.classmate.ui.todo.ItemFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new DailyTimetableFragment();
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
