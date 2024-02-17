package com.hype.classmate.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hype.classmate.R;

public class TodoDetailsDialog {
    TextView todoTitle, todoCategory, todoSubject, todoDue, todoDescription;

    public void showDialog(Activity activity, String id, String title, String category, String subject, String dueDate, String description, String color) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.todo_details_dialog);

        todoTitle = dialog.findViewById(R.id.todoTitle);
        todoTitle.setText(title);

        todoCategory = dialog.findViewById(R.id.todoCategory);
        todoCategory.setText(category);

        todoSubject = dialog.findViewById(R.id.todoSubject);
        todoSubject.setText(subject);

        todoDue = dialog.findViewById(R.id.todoDue);
        todoDue.setText(dueDate);

        todoDescription = dialog.findViewById(R.id.todoDescription);
        todoDescription.setText(description);
        todoDescription.setMovementMethod(new ScrollingMovementMethod());

        dialog.show();
    }

}
