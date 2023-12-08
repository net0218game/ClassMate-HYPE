package com.example.classmate.ui.home;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Objects;

public class HomeTodoAdapter extends RecyclerView.Adapter<HomeTodoAdapter.ViewHolder> {

    ArrayList<ArrayList<String>> localDataSet;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference dbReference;
    FirebaseUser user = mAuth.getCurrentUser();

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView todoItem;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            todoItem = view.findViewById(R.id.homeTodoItem);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAbsoluteAdapterPosition();
            // TODO: TODO kattintas implementalasa
            // https://developer.android.com/develop/ui/views/components/dialogs
        }
    }

    public HomeTodoAdapter(ArrayList<ArrayList<String>> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.home_todo_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeTodoAdapter.ViewHolder viewHolder, int position) {
        // Ertekek beallitasa
        viewHolder.todoItem.setText(localDataSet.get(position).get(0));

        if (Objects.equals(localDataSet.get(position).get(1), "Homework")) {
            viewHolder.todoItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_edit_24, 0, 0, 0);
        } else if (Objects.equals(localDataSet.get(position).get(1), "Exam")) {
            viewHolder.todoItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_assignment_24, 0, 0, 0);
        } else if (Objects.equals(localDataSet.get(position).get(1), "Event")) {
            viewHolder.todoItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_event_24, 0, 0, 0);
        }

        viewHolder.todoItem.setOnClickListener(viewHolder);
    }


    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
