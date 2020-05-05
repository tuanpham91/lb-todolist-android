package com.anhtuan.lbtodolist;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.anhtuan.http.HttpRequestImpl;
import com.anhtuan.http.RequestQueueProvider;

import java.util.ArrayList;
import java.util.Arrays;

public class ListActivity extends Activity {

    private static String url = "http://192.168.178.26:8080/todolists";

    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;
    Button clickButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_activity);
        clickButton = (Button) findViewById(R.id.placeholder);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);

        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });
    }

    public void getList() {
        //TODO : Add authentication to this :
        StringRequest request = new HttpRequestImpl(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Onclick", "Respond : ");

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
        ArrayList<String> list = new ArrayList<>(Arrays.asList(response.split(";")));
        adapter.clear();
        adapter.addAll(list);
    }

}
