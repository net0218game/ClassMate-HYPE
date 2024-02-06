package com.hype.classmate.ui.notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hype.classmate.R;
import com.hype.classmate.ui.AddTodoActivity;
import com.hype.classmate.ui.todo.TodoAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class NotesFragment extends Fragment {

    FloatingActionButton addNoteButton;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference dbReference;

    RecyclerView notesRecyclerView;
    ArrayList<ArrayList<String>> noteList = new ArrayList<ArrayList<String>>();
    TextView noNotes;
    private Menu menu;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        addNoteButton = view.findViewById(R.id.addNoteButton);
        noNotes = view.findViewById(R.id.noNotes);

        getTodayTodoList();
        setHasOptionsMenu(true);

        notesRecyclerView = view.findViewById(R.id.notes_recycler_view);
        notesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        RecyclerView.Adapter<NoteAdapter.ViewHolder> noteAdapter = new NoteAdapter(noteList);

        notesRecyclerView.setAdapter(noteAdapter);


        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void getTodayTodoList() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteList.clear();
                if (snapshot.child("Notes/" + user.getUid()).exists()) {
                    for (DataSnapshot classDataSnapshot : snapshot.child("Notes/" + user.getUid()).getChildren()) {
                        ArrayList<String> note = new ArrayList<String>();

                        note.add(0, Objects.requireNonNull(classDataSnapshot.child("title").getValue()).toString());
                        note.add(1, Objects.requireNonNull(classDataSnapshot.child("body").getValue()).toString());
                        note.add(2, Objects.requireNonNull(classDataSnapshot.getKey()));

                        noteList.add(note);
                    }
                }

                notesRecyclerView.getAdapter().notifyDataSetChanged();

                noNotes.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.note_nav_menu, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.note_filter) {
            Toast.makeText(getContext(), "Feature Coming Soon:)", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.note_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(true);
            builder.setTitle("Delete all notes?");
            builder.setMessage("Are you sure you want to delete all of your notes?\nThis action can not be undone and all of your notes will be lost.");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface alertDialog, int which) {
                    dbReference = FirebaseDatabase.getInstance().getReference();
                    dbReference.child("Notes").child(user.getUid()).removeValue();
                    alertDialog.dismiss();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface alertDialog, int which) {
                    alertDialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}