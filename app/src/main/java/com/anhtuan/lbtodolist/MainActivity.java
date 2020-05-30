package com.anhtuan.lbtodolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.anhtuan.http.HttpRequestImpl;
import com.anhtuan.http.RequestQueueProvider;
import com.anhtuan.http.TodoListDAO;

import java.util.Base64;

public class MainActivity extends Activity {
    Button loginButton;
    EditText accountEditText;
    EditText passwordEditText;
    AlertDialog dialog;
    DataCacher cacher;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cacher = DataCacher.getCacher(this.getApplicationContext());
        requestQueue = RequestQueueProvider.getRequestQueue(this.getApplicationContext());

        loginButton = (Button) findViewById(R.id.l_a_login_button);
        accountEditText = (EditText) findViewById(R.id.l_a_account_name);
        dialog = new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setCancelable(true).create();
        passwordEditText = (EditText) findViewById(R.id.l_a_password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    protected void login() {
        String id=  accountEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String authString = Base64.getEncoder().encode((id+":"+password).getBytes()).toString();
        Log.d("DEBUG", authString);
        cacher.saveStringToFile(cacher.basicAuthFile, authString);

        if (accountEditText.getText().toString().length() == 0 || passwordEditText.getText().toString().length()==0) {
            dialog.setMessage("User and Password can not be empty");
            dialog.show();
        } else {
            // Wait for response
            getList();
        }
    }

    public void getList() {
        StringRequest request = new HttpRequestImpl(Request.Method.GET, TodoListDAO.addUrl,"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cacher.cacheTodoListContent(response);
                // move to new Activity
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // back to old activity
            }
        });
        requestQueue.add(request);
    }

    private
}
