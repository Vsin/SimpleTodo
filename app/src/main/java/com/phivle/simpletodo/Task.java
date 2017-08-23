package com.phivle.simpletodo;

import android.database.Cursor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vsin on 7/31/17.
 */

class Task {
    long id;
    String text;
    Date dueDate;
    final DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Task(Cursor cursor) {
        String dateTime;
        try {
            this.id = cursor.getLong(cursor.getColumnIndex("_id"));
            this.text = cursor.getString(cursor.getColumnIndex("text"));
            dateTime = cursor.getString(cursor.getColumnIndex("due_date"));
            this.dueDate = iso8601Format.parse(dateTime);
        } catch (Exception ignored) {

        }
    }

    public Task(long id, String text, String dueDate) {
        Date parsedDueDate = null;
        this.id = id;
        this.text = text;

        if (dueDate != null) {
            try {
                parsedDueDate = iso8601Format.parse(dueDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.dueDate = parsedDueDate;
    }

    public String dueDateString() {
        return iso8601Format.format(dueDate);
    }
}
