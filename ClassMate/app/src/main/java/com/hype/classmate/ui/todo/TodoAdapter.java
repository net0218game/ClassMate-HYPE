package com.hype.classmate.ui.todo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hype.classmate.R;
import com.hype.classmate.ui.dialog.ClassDetailsDialog;
import com.hype.classmate.ui.dialog.EditTodoDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hype.classmate.ui.dialog.TodoDetailsDialog;

import java.util.ArrayList;
import java.util.Objects;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    ArrayList<ArrayList<String>> localDataSet;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference dbReference;
    FirebaseUser user = mAuth.getCurrentUser();

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView todoCard;
        TextView title;
        TextView subject;
        TextView dueDate;
        TextView description;
        TextView itemNumber;
        TextView category;
        CheckBox isDone;
        ImageView todoEdit;
        Button todoColor;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            title = view.findViewById(R.id.todoTitle);
            subject = view.findViewById(R.id.todoSubject);
            category = view.findViewById(R.id.todoCategory);
            dueDate = view.findViewById(R.id.todoDueDate);
            description = view.findViewById(R.id.todoDescription);
            isDone = view.findViewById(R.id.todoCheckBox);
            itemNumber = view.findViewById(R.id.item_number);
            todoCard = view.findViewById(R.id.todoCard);
            todoEdit = view.findViewById(R.id.todoEdit);
            todoColor = view.findViewById(R.id.todoColor);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAbsoluteAdapterPosition();
            // TODO: TODO kattintas implementalasa
            // https://developer.android.com/develop/ui/views/components/dialogs

            EditTodoDialog alert = new EditTodoDialog();
            alert.showDialog((Activity) v.getContext(), this.todoCard.getTag().toString(),
                    this.title.getText().toString(), this.title.getText().toString(),
                    this.category.getText().toString(), this.subject.getText().toString(),
                    this.dueDate.getText().toString(), this.description.getText().toString());
        }
    }

    public TodoAdapter(ArrayList<ArrayList<String>> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.todo_item, viewGroup, false);

        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        // Ertekek beallitasa a taskoknal
        viewHolder.title.setText(localDataSet.get(position).get(0));
        viewHolder.subject.setText(localDataSet.get(position).get(1));
        viewHolder.category.setText(localDataSet.get(position).get(7));
        viewHolder.description.setText(localDataSet.get(position).get(2));
        viewHolder.dueDate.setText(localDataSet.get(position).get(3));
        viewHolder.itemNumber.setText(String.valueOf(position + 1));
        viewHolder.todoColor.setBackgroundColor(Color.parseColor(localDataSet.get(position).get(5)));

        // Edit task OnClickListener
        viewHolder.todoEdit.setOnClickListener(viewHolder);

        // OnClickListener a checkboxra
        viewHolder.isDone.setOnClickListener((v) -> {
            dbReference = FirebaseDatabase.getInstance().getReference();
            dbReference.child("Todo").child(user.getUid() + "/" + localDataSet.get(position).get(6)).child("done").setValue(viewHolder.isDone.isChecked());
        });

        viewHolder.todoCard.setTag(localDataSet.get(position).get(6));

        viewHolder.todoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoDetailsDialog todoDetailsDialog = new TodoDetailsDialog();
                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    private String id, title, category, subject, dueDate, description, color;

                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("Todo/" + user.getUid()).exists()) {
                            for (DataSnapshot todoDataSnapshot : snapshot.child("Todo/" + user.getUid()).getChildren()) {
                                if (Objects.equals(todoDataSnapshot.getKey(), localDataSet.get(position).get(6))) {

                                    id = todoDataSnapshot.getKey();
                                    title = Objects.requireNonNull(todoDataSnapshot.child("title").getValue()).toString();
                                    category = Objects.requireNonNull(todoDataSnapshot.child("category").getValue()).toString();
                                    subject = Objects.requireNonNull(Objects.requireNonNull(todoDataSnapshot.child("subject").getValue()).toString());
                                    color = Objects.requireNonNull(snapshot.child("Subjects/" + user.getUid() + "/" + todoDataSnapshot.child("subject").getValue()).child("color").getValue()).toString();
                                    dueDate = Objects.requireNonNull(todoDataSnapshot.child("dueDate").getValue()).toString();
                                    description = Objects.requireNonNull(todoDataSnapshot.child("description").getValue()).toString();
                                }
                            }
                            todoDetailsDialog.showDialog((Activity) v.getContext(), id, title, category, subject, dueDate, description, color);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });

        // Checkbox kipipalasa ha a task mar keszen van (isDone == true)
        if (Objects.equals(localDataSet.get(position).get(4), "true")) {
            viewHolder.isDone.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
