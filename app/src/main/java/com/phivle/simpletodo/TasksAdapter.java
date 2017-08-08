package com.phivle.simpletodo;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Vsin on 7/31/17.
 */

public class TasksAdapter extends ArrayAdapter<Task> {

    private static class ViewHolder {
        TextView taskText;
    }

    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task;
        TextView taskText;

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task_item, parent, false);
            viewHolder.taskText = (TextView) convertView.findViewById(R.id.taskText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        taskText = (TextView) convertView.findViewById(R.id.taskText);
        task = getItem(position);

        taskText.setText(task.text);

        return convertView;
    }
}
