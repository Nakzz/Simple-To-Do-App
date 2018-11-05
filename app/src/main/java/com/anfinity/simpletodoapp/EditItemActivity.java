package com.anfinity.simpletodoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends AppCompatActivity {


    //track edit text
    EditText etItemText;
    //position of the item in list
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        EditText etItemText = (EditText) findViewById(R.id.etItemText);

        //set text field to show which item being edited
        etItemText.setText(getIntent().getStringExtra(MainActivity.ITEM_TEXT).toString());
        Log.e("onSaveItem", "Actual text: " + this.etItemText.getText());

        //update position from intent
        position = getIntent().getIntExtra(MainActivity.ITEM_POSITION, 0);

        //update title of the page
        getSupportActionBar().setTitle("Edit Item");
    }

    public void onSaveItem(View v){
        //prepare new intent to send back
        Intent i = new Intent();

        Log.e("onSaveItem", "Edited text: " + this.etItemText.getText());
        i.putExtra(MainActivity.ITEM_TEXT, this.etItemText.getText());
        i.putExtra(MainActivity.ITEM_POSITION, position);

        //set intent as the result of the activity
        setResult(RESULT_OK, i);
        finish();
    }

}
