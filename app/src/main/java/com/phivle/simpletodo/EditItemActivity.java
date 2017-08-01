package com.phivle.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    private EditText etEditItem;
    String editItemText;
    private int editItemIndex;
    private int etEditItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        editItemText = getIntent().getStringExtra("editItemText");
        editItemIndex = getIntent().getIntExtra("editItemIndex", -1);
        etEditItemId = getIntent().getIntExtra("editItemId", -1);
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        populateEditText(editItemText);
    }

    public void populateEditText(String editItemText) {
        etEditItem.setText(editItemText);
        etEditItem.setSelection(etEditItem.getText().length());
        etEditItem.requestFocus();
    }

    public void onSave(View v) {
        Intent editedItemData = new Intent();
        editedItemData.putExtra("editedItemId", etEditItemId);
        editedItemData.putExtra("editedItemText", etEditItem.getText().toString());
        editedItemData.putExtra("editedItemIndex", editItemIndex);
        setResult(RESULT_OK, editedItemData);
        finish();
    }
}
