package com.phivle.simpletodo;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity implements DatePickerFragment.Listener {
    private EditText etEditItem;
    private TextView etEditItemDueDate;
    private String editItemText;
    private int editItemIndex;
    private long etEditItemId;
    private String editItemDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        editItemText = getIntent().getStringExtra("editItemText");
        editItemIndex = getIntent().getIntExtra("editItemIndex", -1);
        editItemDueDate = getIntent().getStringExtra("editItemDueDate");

        etEditItemId = getIntent().getLongExtra("editItemId", -1);
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItemDueDate = (TextView) findViewById(R.id.etEditItemDueDate);
        populateEditText(editItemText);
        populateEditItemDueDate(editItemDueDate);
    }

    public void populateEditText(String editItemText) {
        etEditItem.setText(editItemText);
        etEditItem.setSelection(etEditItem.getText().length());
        etEditItem.requestFocus();
    }

    public void populateEditItemDueDate(String editItemDueDate) {
        etEditItemDueDate.setText(editItemDueDate);
    }

    public void showDatePicker(View v) {
        Bundle args;
        DialogFragment datePickerFragment;

        args = new Bundle();
        args.putString("date", editItemDueDate);

        datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(args);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDatePicked(String date) {
        editItemDueDate = date;
        populateEditItemDueDate(editItemDueDate);
    }

    public void onSave(View v) {
        Intent editedItemData = new Intent();
        editedItemData.putExtra("editedItemId", etEditItemId);
        editedItemData.putExtra("editedItemText", etEditItem.getText().toString());
        editedItemData.putExtra("editedItemIndex", editItemIndex);
        editedItemData.putExtra("editedItemDueDate", editItemDueDate);
        setResult(RESULT_OK, editedItemData);
        finish();
    }
}
