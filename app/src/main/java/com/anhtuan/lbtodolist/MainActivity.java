package com.anhtuan.lbtodolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    RequestQueueProvider requestQueueProvider;

    public MainActivity() {
        super();
        loginButton = (Button) findViewById(R.id.l_a_login_button);
        accountEditText = (EditText) findViewById(R.id.l_a_account_name);
        dialog = new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setCancelable(true).create();
        passwordEditText = (EditText) findViewById(R.id.l_a_password);
        requestQueueProvider = RequestQueueProvider.getRequestQueueProvider(this.getApplicationContext());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        cacher = DataCacher.getCacher(this.getApplicationContext());
        Bundle extras = i.getExtras();

        if (!cacher.readStringFromFile(cacher.localListFile).isEmpty() && extras == null){
            moveToListActivity();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    protected void login() {
        String id=  accountEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String authString = new String(Base64.getEncoder().encode((id+":"+password).getBytes()));
        cacher.saveStringToFile(cacher.basicAuthFile, authString);

        if (accountEditText.getText().toString().length() == 0 || passwordEditText.getText().toString().length()==0) {
            dialog.setMessage("User and Password can not be empty");
            dialog.show();
        } else {
            // Wait for response
            getListAndCache(authString);
        }
    }

    public void getListAndCache(String authString) {
        //TODO : Authenticate first
        Response.Listener responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cacher.cacheTodoListContent(response);
                moveToListActivity();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // back to old activity
                dialog.setMessage("Can not authenticate, please log in again");
                dialog.show();
            }
        };
        TodoListDAO.getInstance(this.getApplicationContext()).getList(responseListener, errorListener, authString);
    }

    private void moveToListActivity() {
        Intent moveToList = new Intent(this, ListActivity.class);
        startActivity(moveToList);
    }
}
