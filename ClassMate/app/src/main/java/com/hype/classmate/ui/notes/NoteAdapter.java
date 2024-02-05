package com.hype.classmate.ui.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.hype.classmate.R;
import com.hype.classmate.ui.dialog.ShareDialog;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    ArrayList<ArrayList<String>> localDataSet;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference dbReference;
    FirebaseUser user = mAuth.getCurrentUser();

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, body;
        CardView noteCard;
        ImageButton noteShareButton;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            title = view.findViewById(R.id.noteTitle);
            body = view.findViewById(R.id.noteBody);
            noteCard = view.findViewById(R.id.noteCard);
            noteShareButton = view.findViewById(R.id.noteShareButton);
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
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        // Ertekek beallitasa
        viewHolder.title.setText(localDataSet.get(position).get(0));
        viewHolder.body.setText(localDataSet.get(position).get(1));

        viewHolder.noteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NoteActivity.class);
                Bundle b = new Bundle();
                b.putString("title", localDataSet.get(position).get(0)); //Your id
                b.putString("body", localDataSet.get(position).get(1)); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                v.getContext().startActivity(intent);
            }
        });

        viewHolder.noteShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog();
                shareDialog.showDialog((Activity) v.getContext(), localDataSet.get(position).get(2));
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
