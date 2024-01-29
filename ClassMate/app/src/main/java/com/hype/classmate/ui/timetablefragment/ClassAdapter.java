package com.hype.classmate.ui.timetablefragment;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hype.classmate.R;
import com.hype.classmate.ui.AddClassActivity;
import com.hype.classmate.ui.dialog.EditClassDialog;
import com.hype.classmate.ui.timetabletest.TestTimetableFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    ArrayList<ArrayList<String>> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView startTime, endTime, classroom, classTeacherTitle, classTitle, startTime2, endTime2;
        private final CardView classCard;
        private final Button currentClassColor;
        private final ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            classTitle = view.findViewById(R.id.currentClassTitle);
            classTeacherTitle = view.findViewById(R.id.currentClassTeacher);
            classroom = view.findViewById(R.id.currentClassRoom);
            startTime = view.findViewById(R.id.startTime);
            endTime = view.findViewById(R.id.endTime);
            startTime2 = view.findViewById(R.id.currentClassStartTime);
            endTime2 = view.findViewById(R.id.currentClassEndTime);
            classCard = view.findViewById(R.id.currentClass);
            progressBar = view.findViewById(R.id.currentClassProgressBar);
            currentClassColor = view.findViewById(R.id.currentClassColor);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAbsoluteAdapterPosition();
            // TODO: Open timetable fragment
            // https://developer.android.com/develop/ui/views/components/dialogs

            Toast.makeText(v.getContext(), "Open timetable", Toast.LENGTH_SHORT).show();
        }
    }

    public ClassAdapter(ArrayList<ArrayList<String>> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.current_class, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the contents of the view with that element
        viewHolder.classTitle.setText(localDataSet.get(position).get(0));
        viewHolder.classTeacherTitle.setText(localDataSet.get(position).get(6));
        viewHolder.classroom.setText(localDataSet.get(position).get(5));
        viewHolder.startTime.setText(localDataSet.get(position).get(3));
        viewHolder.endTime.setText(localDataSet.get(position).get(4));
        viewHolder.startTime2.setText(localDataSet.get(position).get(3));
        viewHolder.endTime2.setText(localDataSet.get(position).get(4));
        viewHolder.progressBar.setProgress(progress(localDataSet.get(position).get(3), localDataSet.get(position).get(4)));
        viewHolder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(localDataSet.get(position).get(1))));
        viewHolder.currentClassColor.setBackgroundColor(Color.parseColor(localDataSet.get(position).get(1)));
        viewHolder.classCard.setOnClickListener(viewHolder);
    }

    public static Integer progress(String startSH, String stopSH) {
        try {
            Date now = new Date();
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date startTime = parser.parse(startSH);
            Date endTime = parser.parse(stopSH);
            Date nowTime = parser.parse(now.getHours() + ":" + now.getMinutes());

            Float all = (float) (endTime.getTime() - startTime.getTime());

            return (int) (((nowTime.getTime() - startTime.getTime()) / all) * 100);

        } catch (java.text.ParseException e) {
            return null;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
