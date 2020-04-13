package com.anhtuan.lbtodolist;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;

    Button clickButton = (Button) findViewById(R.id.placeholder);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_activity);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
