package com.example.classmate.ui.todo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classmate.R;
import com.example.classmate.ui.dialog.EditClassDialog;
import com.example.classmate.ui.dialog.EditTodoDialog;

import java.util.ArrayList;
import java.util.Objects;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    ArrayList<ArrayList<String>> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView todoCard;
        TextView title, subject, dueDate, description, itemNumber;
        CheckBox isDone;
        ImageView todoEdit;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            title = view.findViewById(R.id.todoTitle);
            subject = view.findViewById(R.id.todoSubject);
            dueDate = view.findViewById(R.id.todoDueDate);
            //description = view.findViewById(R.id.todoDescription);
            isDone = view.findViewById(R.id.todoCheckBox);
            itemNumber = view.findViewById(R.id.item_number);
            todoCard = view.findViewById(R.id.todoCard);
            todoEdit = view.findViewById(R.id.todoEdit);

        }

        @Override
        public void onClick(View v) {
            int position = this.getAbsoluteAdapterPosition();
            // TODO: TODO kattintas implementalasa
            // https://developer.android.com/develop/ui/views/components/dialogs

            EditTodoDialog alert = new EditTodoDialog();
            alert.showDialog((Activity) v.getContext(), this.todoCard.getTag().toString(),
                    this.title.getText().toString(), this.title.getText().toString(),
                    "Homework", this.subject.getText().toString(),
                    this.dueDate.getText().toString(), "asd");
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.title.setText(localDataSet.get(position).get(0));
        viewHolder.subject.setText(localDataSet.get(position).get(1));
        viewHolder.dueDate.setText(localDataSet.get(position).get(3));
        viewHolder.itemNumber.setText(String.valueOf(position));

        viewHolder.todoEdit.setOnClickListener(viewHolder);
        viewHolder.todoCard.setTag(localDataSet.get(position).get(6));

        if (Objects.equals(localDataSet.get(position).get(4), "true")) {
            viewHolder.isDone.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
