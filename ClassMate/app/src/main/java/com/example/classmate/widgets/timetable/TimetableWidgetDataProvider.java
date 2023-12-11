package com.example.classmate.widgets.timetable;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.NonNull;

import com.example.classmate.R;
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

public class TimetableWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    ArrayList<ArrayList<String>> classList = new ArrayList<ArrayList<String>>();

    Context mContext;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public TimetableWidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        getClasses();
    }

    @Override
    public void onDataSetChanged() {
        getClasses();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return classList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.home_class_item);
        Log.d("orarend", classList.toString());
        view.setTextViewText(R.id.homeClassItem, classList.get(position).get(0));
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void getClasses() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classList.clear();
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

                            classList.add(ora);
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

                Collections.sort(classList, myComparator);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}