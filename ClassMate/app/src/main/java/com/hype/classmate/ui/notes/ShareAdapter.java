package com.hype.classmate.ui.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hype.classmate.R;
import com.hype.classmate.interfaces.NoteItem;
import com.hype.classmate.ui.dialog.ShareDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    ArrayList<ArrayList<String>> localDataSet;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = mAuth.getCurrentUser();

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView shareItemUsername;
        Button shareItemShareButton;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            shareItemUsername = view.findViewById(R.id.shareItemUsername);
            shareItemShareButton = view.findViewById(R.id.shareItemShareButton);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public ShareAdapter(ArrayList<ArrayList<String>> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ShareAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.share_item, viewGroup, false);

        return new ShareAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        // Ertekek beallitasa
        viewHolder.shareItemUsername.setText(localDataSet.get(position).get(1));
        viewHolder.shareItemShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    private String title, body;
                    private NoteItem myNoteItem;

                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("Notes/" + user.getUid()).exists()) {
                            for (DataSnapshot todoDataSnapshot : snapshot.child("Notes/" + user.getUid()).getChildren()) {
                                if (Objects.equals(todoDataSnapshot.getKey(), localDataSet.get(position).get(2))) {

                                    title = Objects.requireNonNull(todoDataSnapshot.child("title").getValue()).toString();
                                    body = Objects.requireNonNull(todoDataSnapshot.child("body").getValue()).toString();
                                }
                            }
                        }
                        myNoteItem = new NoteItem(title, body);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                        String currentDateandTime = sdf.format(new Date());
                        dbReference.child("Notes").child(localDataSet.get(position).get(0) + "/" + currentDateandTime).setValue(myNoteItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // TODO: Close Share Dialog
                                viewHolder.shareItemShareButton.setText("Sent");
                                viewHolder.shareItemShareButton.setEnabled(false);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
