package com.anfinity.simpletodoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //a numberic code to identify the edit activity
    public final static int EDIT_REQUEST_CODE =20;
    //keys used for passing data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";


    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //read filesystem items. If DNE, create new arrayList
        readItems();

        //initializes variables
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        //test data
//        items.add("First item");
//        items.add("Second item");

        setupListViewListener();
    }

    public void onAddItem(View v){
        //Get Text Field Values
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String newItem = etNewItem.getText().toString();

        //Add item to adapter.
        //Question: Why the adapter and not the list?
        // Because the adapter shows in the mobile.
//        this.itemsAdapter.add(newItem);

        //Try adding to the array and reloading the adapter.
        //Works as well.
        items.add(newItem);
        itemsAdapter.notifyDataSetChanged();

        Log.i("onAddItem", "Item added to list");

        //Write to the local file
        writeItems();

        //Empties the Text Field
        etNewItem.setText("");

        //Display Toast
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener(){
        Log.i("MainActivity", "Setting up Listener on list View");
        this.lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Item Removed From list: " + position);
                //remove item at the position of the list
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();

                //Write to the local file
                writeItems();

                return true;
            }
        });

        //set up item listener for edit (regular click)
        this.lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //create new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                //pass the data being edited
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);

                //display the activity
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    //handle results form the edit activity


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check for proper activity code
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            //extract updated item
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            int position = data.getExtras().getInt(ITEM_POSITION);

            //updata model, view and local file
            items.set(position, updatedItem);
            writeItems();
            itemsAdapter.notifyDataSetChanged();

            //notify user of change
            Toast.makeText(getApplicationContext(), "Updated item: "+ position, Toast.LENGTH_SHORT);
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error Reading File.",e);
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error Writing to File.",e);
        }
    }
}
