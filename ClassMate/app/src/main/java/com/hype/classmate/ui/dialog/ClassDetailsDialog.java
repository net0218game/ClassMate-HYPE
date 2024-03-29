package com.hype.classmate.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hype.classmate.R;

public class ClassDetailsDialog {
    TextView subjbectTextView, classroomTextView, dayTextView, startTextView, teacherTextView;
    Button classColor;

    public void showDialog(Activity activity, String id, String subject, String classroom, String day, String start, String end, String color, String teacher) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.class_details_dialog);

        subjbectTextView = dialog.findViewById(R.id.subjbectTextView);
        subjbectTextView.setText(subject);

        teacherTextView = dialog.findViewById(R.id.teacherTextView);
        teacherTextView.setText(teacher);

        classroomTextView = dialog.findViewById(R.id.classroomTextView);
        classroomTextView.setText(classroom);

        dayTextView = dialog.findViewById(R.id.dayTextView);
        dayTextView.setText(day);

        startTextView = dialog.findViewById(R.id.startTextView);
        startTextView.setText(start + " - " + end);

        classColor = dialog.findViewById(R.id.classColor);
        classColor.setBackgroundColor(Color.parseColor(color));

        Button closeDialogButton = dialog.findViewById(R.id.cancelEditButton);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
