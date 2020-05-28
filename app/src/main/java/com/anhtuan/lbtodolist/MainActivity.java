package com.anhtuan.lbtodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
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
        dialog = new AlertDialog.Builder(this.getApplicationContext()).create();
        passwordEditText = (EditText) findViewById(R.id.l_a_password);
        setContentView(R.layout.activity_main);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    void login() {
        if (accountEditText.getText().toString().length() == 0 || passwordEditText.getText().toString().length()==0) {
            dialog.setMessage("Username or password is missing!");
            dialog.show();
        }
    }
}
