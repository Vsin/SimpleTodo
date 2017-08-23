package com.phivle.simpletodo;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Vsin on 7/31/17.
 */

public class TasksAdapter extends ArrayAdapter<Task> {

    private static class ViewHolder {
        TextView taskText;
        TextView taskDue;
    }

    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task;
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task_item, parent, false);
            viewHolder.taskText = (TextView) convertView.findViewById(R.id.taskText);
            viewHolder.taskDue = (TextView) convertView.findViewById(R.id.taskDue);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        task = getItem(position);

        viewHolder.taskText.setText(task.text);
        if (task.dueDate != null) {
            viewHolder.taskDue.setText(task.dueDateString());
        } else {
            viewHolder.taskDue.setText("");
        }

        return convertView;
    }
}
