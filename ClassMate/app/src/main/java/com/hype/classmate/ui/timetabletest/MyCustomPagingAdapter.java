package com.hype.classmate.ui.timetabletest;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hype.classmate.ui.dialog.ClassDetailsDialog;
import com.hype.classmate.ui.dialog.EditClassDialog;

import java.util.Objects;

public class MyCustomPagingAdapter extends WeekView.PagingAdapter<MyEvent> {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public WeekViewEntity onCreateEntity(MyEvent item) {
        WeekViewEntity.Style style = new WeekViewEntity.Style.Builder().setBackgroundColor(item.getColor()).setCornerRadius(20).build();
        return new WeekViewEntity.Event.Builder(item).setId(item.getId()).setStyle(style).setTitle(item.getTitle()).setSubtitle(item.getSubTitle()).setStartTime(item.getStartTime()).setEndTime(item.getEndTime()).build();
    }

    @Override
    public void onEventClick(MyEvent data) {
        super.onEventClick(data);
        ClassDetailsDialog detailsDialog = new ClassDetailsDialog();
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            private String id, subject, classroom, day, color, startTimeString, endTimeString, teacher;

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Subjects/" + user.getUid()).exists()) {
                    for (DataSnapshot classDataSnapshot : snapshot.child("Classes/" + user.getUid()).getChildren()) {
                        if (Objects.equals(classDataSnapshot.getKey(), String.valueOf(data.getId()))) {
                            id = classDataSnapshot.getKey();
                            subject = Objects.requireNonNull(classDataSnapshot.child("subject").getValue()).toString();
                            classroom = Objects.requireNonNull(classDataSnapshot.child("classroom").getValue()).toString();
                            day = Objects.requireNonNull(Objects.requireNonNull(classDataSnapshot.child("day").getValue()).toString());
                            color = Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("color").getValue()).toString();
                            teacher = Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("teacher").getValue()).toString();
                            startTimeString = Objects.requireNonNull(classDataSnapshot.child("start").getValue()).toString();
                            endTimeString = Objects.requireNonNull(classDataSnapshot.child("end").getValue()).toString();
                        }
                    }
                    detailsDialog.showDialog((Activity) getContext(), id, subject, classroom, day, startTimeString, endTimeString, color, teacher);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onEventLongClick(MyEvent data) {
        super.onEventLongClick(data);
        EditClassDialog editDialog = new EditClassDialog();

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            private String id, subject, classroom, day, color, startTimeString, endTimeString;

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Subjects/" + user.getUid()).exists()) {
                    for (DataSnapshot classDataSnapshot : snapshot.child("Classes/" + user.getUid()).getChildren()) {
                        if (Objects.equals(classDataSnapshot.getKey(), String.valueOf(data.getId()))) {
                            id = classDataSnapshot.getKey();
                            subject = Objects.requireNonNull(classDataSnapshot.child("subject").getValue()).toString();
                            classroom = Objects.requireNonNull(classDataSnapshot.child("classroom").getValue()).toString();
                            day = Objects.requireNonNull(Objects.requireNonNull(classDataSnapshot.child("day").getValue()).toString());
                            color = Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + classDataSnapshot.child("subject").getValue()).child("color").getValue()).toString();
                            startTimeString = Objects.requireNonNull(classDataSnapshot.child("start").getValue()).toString();
                            endTimeString = Objects.requireNonNull(classDataSnapshot.child("end").getValue()).toString();
                        }
                    }
                    editDialog.showDialog((Activity) getContext(), id, subject, classroom, day, startTimeString, endTimeString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
