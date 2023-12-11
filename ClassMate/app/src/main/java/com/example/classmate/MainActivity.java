package com.example.classmate;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.classmate.ui.data.model.LoggedInUser;
import com.example.classmate.ui.login.LoginActivity;
import com.example.classmate.ui.register.RegisterActivity;
import com.example.classmate.widgets.timetable.TimetableWidget;
import com.example.classmate.widgets.todo.TodoWidget;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.classmate.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        updateWidget();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWidget();

        com.example.classmate.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_timetable, R.id.navigation_calendar, R.id.navigation_settings, R.id.navigation_todo)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        updateWidget();

    }

    public void updateWidget() {
        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        ComponentName todoWidget = new ComponentName(context, TodoWidget.class);
        int[] todoAppWidgetIds = appWidgetManager.getAppWidgetIds(todoWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(todoAppWidgetIds, R.id.widgetListView);

        ComponentName timetableWidget = new ComponentName(context, TimetableWidget.class);
        int[] timetableAppWidgetIds = appWidgetManager.getAppWidgetIds(timetableWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(timetableAppWidgetIds, R.id.timetableWidgetListView);
    }

}