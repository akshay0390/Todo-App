package com.walmartlabs.todo_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by akulka2 on 9/30/15.
 */
public class TodoItemAdapter extends ArrayAdapter<Todo> {

    public TodoItemAdapter(Context context, ArrayList<Todo> todoList) {
        super(context, 0, todoList);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){

        Todo todoItem = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }
        TextView todoItemDescription = (TextView) view.findViewById(R.id.tv_todoItemDescription);
        TextView todoItemPriority = (TextView) view.findViewById(R.id.tv_todoItemPriority);
        TextView todoDueDate = (TextView) view.findViewById(R.id.tv_todoItemDueDate);
        todoItemDescription.setText(todoItem.getDescription());
        todoItemPriority.setText(todoItem.getPriority());
        todoDueDate.setText(todoItem.getDueDate());
        return  view;
    }
}
