package com.phivle.simpletodo;

import android.database.Cursor;

import org.json.JSONObject;

/**
 * Created by Vsin on 7/31/17.
 */

class Task {
    long id;
    String text;

    public Task(Cursor cursor) {
        try {
            this.id = cursor.getLong(cursor.getColumnIndex("_id"));
            this.text = cursor.getString(cursor.getColumnIndex("text"));
        } catch (Exception ignored) {

        }
    }

    public Task(long id, String text) {
        this.id = id;
        this.text = text;
    }
}
