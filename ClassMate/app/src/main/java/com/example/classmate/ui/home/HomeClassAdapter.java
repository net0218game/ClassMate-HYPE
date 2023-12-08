package com.example.classmate.ui.home;

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

public class HomeClassAdapter extends RecyclerView.Adapter<HomeClassAdapter.ViewHolder> {

    ArrayList<ArrayList<String>> localDataSet;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference dbReference;
    FirebaseUser user = mAuth.getCurrentUser();

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView classItem;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            classItem = view.findViewById(R.id.homeClassItem);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAbsoluteAdapterPosition();
            // TODO: TODO kattintas implementalasa
            // https://developer.android.com/develop/ui/views/components/dialogs
        }
    }

    public HomeClassAdapter(ArrayList<ArrayList<String>> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.home_class_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeClassAdapter.ViewHolder viewHolder, int position) {
        // Ertekek beallitasa
        viewHolder.classItem.setText(localDataSet.get(position).get(0));
    }


    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
