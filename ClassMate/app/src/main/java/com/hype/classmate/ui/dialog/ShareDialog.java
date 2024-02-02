package com.hype.classmate.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hype.classmate.R;
import com.hype.classmate.ui.notes.NoteAdapter;
import com.hype.classmate.ui.notes.ShareAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class ShareDialog {

    EditText usernameInput;
    RecyclerView usersRecyclerView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<ArrayList<String>> userList = new ArrayList<ArrayList<String>>();

    public void showDialog(Activity activity, String id) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.share_dialog);

        usernameInput = dialog.findViewById(R.id.usernameInput);
        usersRecyclerView = dialog.findViewById(R.id.usersRecyclerView);

        usersRecyclerView.setLayoutManager(new GridLayoutManager(dialog.getContext(), 1));

        ShareAdapter shareAdapter = new ShareAdapter(userList);

        usersRecyclerView.setAdapter(shareAdapter);

        Button closeDialogButton = dialog.findViewById(R.id.cancelEditButton);

        usernameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {

                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for (DataSnapshot userDataSnapshot : snapshot.child("Users").getChildren()) {
                            if (Objects.requireNonNull(userDataSnapshot.child("name").getValue()).toString().equals(usernameInput.getText().toString())) {
                                Log.d("anyadat", "te cigany");
                                ArrayList<String> userCucc = new ArrayList<String>();

                                userCucc.add(0, Objects.requireNonNull(userDataSnapshot.child("userId").getValue()).toString());
                                userCucc.add(1, Objects.requireNonNull(userDataSnapshot.child("name").getValue()).toString());
                                userCucc.add(2, id);

                                userList.add(userCucc);
                                Objects.requireNonNull(usersRecyclerView.getAdapter()).notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                return false;
            }
        });

        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
