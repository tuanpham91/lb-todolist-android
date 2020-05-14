package com.anhtuan.lbtodolist;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.anhtuan.custom.ListViewArrayAdapter;
import com.anhtuan.http.HttpRequestImpl;
import com.anhtuan.http.RequestQueueProvider;
import com.anhtuan.pojo.TodoEntry;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ListActivity extends Activity {

    private static String url = "http://192.168.178.26:8080/todolist";

    ArrayList<String> listItems=new ArrayList<>();
    ListViewArrayAdapter adapter;
    Button clickButton;
    ListView todoListView;
    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_activity);
        clickButton = (Button) findViewById(R.id.placeholder);
        todoListView = (ListView) findViewById(R.id.todoList);

        adapter = new ListViewArrayAdapter(this.getApplicationContext(), android.R.layout.simple_list_item_1);
        todoListView.setAdapter(adapter);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });
    }

    public void getList() {
        StringRequest request = new HttpRequestImpl(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Onclick", "Respond : " + response);
                updateList(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Onclick", "Error");
            }
        });
        RequestQueue requestQueue = RequestQueueProvider.getRequestQueue(this.getApplicationContext());
        requestQueue.add(request);
    }

    public void updateList(String response) {
        TodoEntry[] entryList = gson.fromJson(response, TodoEntry[].class);
        adapter.clear();
        adapter.addAll(entryList);
        adapter.notifyDataSetChanged();
    }

}
