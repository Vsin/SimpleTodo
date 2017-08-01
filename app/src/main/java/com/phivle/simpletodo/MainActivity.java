package com.phivle.simpletodo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.phivle.simpletodo.db.TaskContract;
import com.phivle.simpletodo.db.TaskDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> items;
    ArrayAdapter<Task> taskAdapter;
    ListView lvItems;
    TaskDbHelper mHelper;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        updateDisplayedItems();
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        deleteItem(pos);
                        return true;
                    }
                }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                            View item, int pos, long id) {
                        Task editItemTask = items.get(pos);
                        launchEditIntent(editItemTask, pos);
                    }
                }
        );
    }

    private void updateDisplayedItems() {
        mHelper = new TaskDbHelper(this);

        addItemsFromDb();
        updateTaskAdapter();
    }

    private void updateTaskAdapter() {
        if (taskAdapter == null) {
            taskAdapter = new TasksAdapter(this, items);
            lvItems.setAdapter(taskAdapter);
        } else {
            taskAdapter.clear();
            taskAdapter.addAll(items);
            taskAdapter.notifyDataSetChanged();
        }
    }

    private void addItemsFromDb() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = mHelper.getReadableDatabase();
            cursor = db.query(TaskContract.TaskEntry.TABLE,
                    new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TEXT},
                    null, null, null, null, null);
            while (cursor.moveToNext()) {
                items.add(new Task(cursor));
            }
        } catch (SQLiteException ignored) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    private void deleteItem(int pos) {
        mHelper = new TaskDbHelper(this);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String itemId = String.valueOf(items.get(pos).id);

        db.delete(TaskContract.TaskEntry.TABLE, TaskContract.TaskEntry._ID + " = ?", new String[] { itemId });

        db.close();

        items.remove(pos);
        taskAdapter.notifyDataSetChanged();
    }

    private void launchEditIntent(Task editItemTask, int pos) {
        Intent editItemIntent = new Intent(MainActivity.this, EditItemActivity.class);
        editItemIntent.putExtra("editItemId", editItemTask.id);
        editItemIntent.putExtra("editItemText", editItemTask.text);
        editItemIntent.putExtra("editItemIndex", pos);
        startActivityForResult(editItemIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editedItemText;
            int editedItemIndex;
            int editedItemId;
            SQLiteDatabase db = null;
            ContentValues values;

            editedItemText = data.getExtras().getString("editedItemText");
            editedItemIndex = data.getExtras().getInt("editedItemIndex");
            editedItemId = data.getExtras().getInt("editedItemId");

            try {
                db = mHelper.getWritableDatabase();
            } catch (SQLException ignored) {
            }

            values = new ContentValues();
            values.put(TaskContract.TaskEntry.COL_TASK_TEXT, editedItemText);

            if (db != null) {
                db.update(TaskContract.TaskEntry.TABLE, values,
                        TaskContract.TaskEntry._ID + " = ?",
                        new String[] { String.valueOf(editedItemId) });
                db.close();
            }

            items.set(editedItemIndex, new Task(editedItemId, editedItemText));
            taskAdapter.notifyDataSetChanged();
        }
    }

    public void onAddItem(View v) {
        EditText etNewItem;
        String itemText;
        SQLiteDatabase db = null;
        ContentValues values;
        long itemId = -1;

        etNewItem = (EditText) findViewById(R.id.etNewItem);
        itemText = etNewItem.getText().toString();
        try {
            db = mHelper.getWritableDatabase();
        } catch (SQLiteException ignored) {
        }

        values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TEXT, itemText);

        if (db != null) {
            itemId = db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
        }

        taskAdapter.add(new Task(itemId, itemText));
        etNewItem.setText("");
    }
}
