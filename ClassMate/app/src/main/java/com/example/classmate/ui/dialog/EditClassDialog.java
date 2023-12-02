package com.example.classmate.ui.dialog;

import static java.util.Arrays.asList;
import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import com.example.classmate.R;
import com.example.classmate.interfaces.TimetableClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EditClassDialog {

    FirebaseAuth mAuth;
    DatabaseReference dbReference;

    public void showDialog(Activity activity, String id, String msg, String classroom, String day, String start, String end) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.edit_class_dialog);

        Log.d("orarend ID", id);

        TextView title = dialog.findViewById(R.id.dialogTitle);
        title.setText(msg);

        TextInputEditText classroomInput = dialog.findViewById(R.id.classClassroom);
        classroomInput.setText(classroom);

        AutoCompleteTextView dayInput = dialog.findViewById(R.id.dayTextView);
        dayInput.setText(day);

        TextInputEditText startInput = dialog.findViewById(R.id.classStartTime);
        startInput.setText(start);

        TextInputEditText endInput = dialog.findViewById(R.id.classEndTime);
        endInput.setText(end);

        Button closeDialogButton = dialog.findViewById(R.id.cancelEditButton);
        Button editButton = dialog.findViewById(R.id.editClassButton);

        ArrayList<String> dayList = new ArrayList<>(asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(dialog.getContext(), R.layout.spinner_item, dayList);
        dayInput.setAdapter(dayAdapter);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimetableClass timetableClass = new TimetableClass(msg, classroomInput.getText().toString(), dayInput.getText().toString(), startInput.getText().toString(), endInput.getText().toString());
                dbReference = FirebaseDatabase.getInstance().getReference();
                dbReference.child("Classes").child(user.getUid() + "/" + id).setValue(timetableClass);
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
