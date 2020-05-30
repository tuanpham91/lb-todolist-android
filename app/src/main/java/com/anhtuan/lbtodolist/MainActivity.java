package com.anhtuan.lbtodolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.anhtuan.http.HttpRequestImpl;

public class MainActivity extends Activity {
    Button loginButton;
    EditText accountEditText;
    EditText passwordEditText;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        if (accountEditText.getText().toString().length() == 0 || passwordEditText.getText().toString().length()==0) {
            dialog.setMessage("User and Password can not be empty");
            dialog.show();
        }
    }

    public void getList() {
        StringRequest request = new HttpRequestImpl(Request.Method.GET, addUrl,"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cacheTodoListContent(response);
                updateList(response);
            }
        });
        requestQueue.add(request);
    }
}
