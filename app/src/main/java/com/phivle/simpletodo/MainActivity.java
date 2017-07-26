package com.phivle.simpletodo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.phivle.simpletodo.db.TaskContract;
import com.phivle.simpletodo.db.TaskDbHelper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
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
                        String editItemText = items.get(pos).toString();
                        launchEditIntent(editItemText, pos);
                    }
                }
        );
    }

    private void updateDisplayedItems() {

        mHelper = new TaskDbHelper(this);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            items.add(cursor.getString(idx));
        }

        if (itemsAdapter == null) {
            itemsAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, items);
            lvItems.setAdapter(itemsAdapter);
        } else {
            itemsAdapter.clear();
            itemsAdapter.addAll(items);
            itemsAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    private void deleteItem(int pos) {
        mHelper = new TaskDbHelper(this);
        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.delete(TaskContract.TaskEntry.TABLE, TaskContract.TaskEntry.COL_TASK_TITLE + " = ?", new String[] { items.get(pos)});

        db.close();

        items.remove(pos);
        itemsAdapter.notifyDataSetChanged();
    }

    private void launchEditIntent(String editItemText, int pos) {
        Intent editItemIntent = new Intent(MainActivity.this, EditItemActivity.class);
        editItemIntent.putExtra("editItem", editItemText);
        editItemIntent.putExtra("editItemIndex", pos);
        startActivityForResult(editItemIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editedItemText = data.getExtras().getString("editedItemText");
            int editedItemIndex = data.getExtras().getInt("editedItemIndex");
            SQLiteDatabase db = mHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.COL_TASK_TITLE, editedItemText);

            db.update(TaskContract.TaskEntry.TABLE, values, TaskContract.TaskEntry.COL_TASK_TITLE + " = ?", new String[] { editedItemText });
            db.close();

            items.set(editedItemIndex, editedItemText);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, itemText);
        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
    }
}
