package com.example.classmate.ui.timetablefragment;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classmate.R;
import com.example.classmate.ui.dialog.EditClassDialog;

import java.util.ArrayList;


public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    ArrayList<ArrayList<String>> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView startTime, endTime, classroom, classTeacherTitle, classTitle;
        private final CardView classCard;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            classTitle = view.findViewById(R.id.classTitle);
            classTeacherTitle = view.findViewById(R.id.classTeacherTitle);
            classroom = view.findViewById(R.id.classroom);
            startTime = view.findViewById(R.id.startTime);
            endTime = view.findViewById(R.id.endTime);
            classCard = view.findViewById(R.id.classCard);
        }

        private String m_Text = "";

        @Override
        public void onClick(View v) {
            int position = this.getAbsoluteAdapterPosition();
            // TODO: Ora kattintas implementalasa
            // https://developer.android.com/develop/ui/views/components/dialogs

            EditClassDialog alert = new EditClassDialog();
            alert.showDialog((Activity) v.getContext(), String.valueOf(this.classCard.getTag()),
                    this.classTitle.getText().toString(), this.classroom.getText().toString(),
                    "Monday", this.startTime.getText().toString(),
                    this.endTime.getText().toString());
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
                .inflate(R.layout.classitem, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the contents of the view with that element
        viewHolder.classTitle.setText(localDataSet.get(position).get(0));
        viewHolder.classTeacherTitle.setText(localDataSet.get(position).get(1));
        viewHolder.classroom.setText(localDataSet.get(position).get(3));
        viewHolder.startTime.setText(localDataSet.get(position).get(5));
        viewHolder.endTime.setText(localDataSet.get(position).get(6));
        viewHolder.classCard.setCardBackgroundColor(Integer.parseInt(localDataSet.get(position).get(7)));
        viewHolder.classCard.setOnClickListener(viewHolder);
        viewHolder.classCard.setTag(localDataSet.get(position).get(8));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
