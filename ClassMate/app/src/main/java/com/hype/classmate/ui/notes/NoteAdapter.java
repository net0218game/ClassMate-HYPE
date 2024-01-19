package com.hype.classmate.ui.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.hype.classmate.R;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    ArrayList<ArrayList<String>> localDataSet;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference dbReference;
    FirebaseUser user = mAuth.getCurrentUser();

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, body;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            title = view.findViewById(R.id.noteTitle);
            body = view.findViewById(R.id.noteBody);

        }

        @Override
        public void onClick(View v) {

        }
    }

    public NoteAdapter(ArrayList<ArrayList<String>> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder viewHolder, int position) {
        // Ertekek beallitasa
        viewHolder.title.setText(localDataSet.get(position).get(0));
        viewHolder.body.setText(localDataSet.get(position).get(1));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
