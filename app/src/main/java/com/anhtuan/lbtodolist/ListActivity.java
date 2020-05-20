package com.anhtuan.lbtodolist;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.anhtuan.custom.ListViewArrayAdapter;
import com.anhtuan.http.HttpRequestImpl;
import com.anhtuan.http.RequestQueueProvider;
import com.anhtuan.pojo.TodoEntry;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ListActivity extends Activity {
    private static String baseUrl = "http://192.168.178.26:8080";
    private static String addUrl = baseUrl + "/todolist";
    private static String postUrl = baseUrl + "/addtodolist";
    private static String deleteUrl = baseUrl + "/deletetodolist";

    private ArrayList<String> listItems=new ArrayList<>();
    private ListViewArrayAdapter adapter;
    Button clickButton;
    ImageButton addButton;
    ListView todoListView;
    Dialog createDialog;
    Gson gson = new Gson();
    Button closeDiaglogButton;
    Button createDiaglogButton;

    // TextView for Dialog
    EditText cdNameET;
    EditText cdLanguageET;
    EditText cdCategoryET;
    // Time makes no senses
    EditText cdAmountET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_activity);

        clickButton = (Button) findViewById(R.id.placeholder);
        addButton = (ImageButton) findViewById(R.id.addEntryButton);
        todoListView = (ListView) findViewById(R.id.todoList);

        adapter = new ListViewArrayAdapter(this.getApplicationContext(), android.R.layout.simple_list_item_1, this);
        todoListView.setAdapter(adapter);

        createDialog = new Dialog(this);
        createDialog.setContentView(R.layout.add_item_dialog);
        createDialog.setTitle("Add entry");

        closeDiaglogButton = (Button) createDialog.findViewById(R.id.create_dialog_cancel_button);
        createDiaglogButton = (Button) createDialog.findViewById(R.id.create_dialog_create_button);
        cdNameET = (EditText) createDialog.findViewById(R.id.create_dialog_et_1);
        cdLanguageET = (EditText) createDialog.findViewById(R.id.create_dialog_et_2);
        cdCategoryET = (EditText) createDialog.findViewById(R.id.create_dialog_et_3);
        cdAmountET = (EditText) createDialog.findViewById(R.id.create_dialog_et_4);

        closeDiaglogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog.dismiss();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateDialog();
            }
        });

        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });

        createDiaglogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToListRequest();
                createDialog.dismiss();
            }
        });
    }

    public void getList() {
        StringRequest request = new HttpRequestImpl(Request.Method.GET, addUrl, new Response.Listener<String>() {
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
        Log.d("List Items Size", adapter.getCount() + " ");
        adapter.notifyDataSetChanged();
    }

    public void openCreateDialog() {
        createDialog.show();
    }

    public void addToListRequest() {
        String itemName = cdNameET.getText().toString();
        String itemLanguage = cdLanguageET.getText().toString();
        String itemCategory = cdCategoryET.getText().toString();
        Long itemAmount = Long.valueOf(cdAmountET.getText().toString());
        TodoEntry entry = new TodoEntry(itemName, System.currentTimeMillis(), itemLanguage, itemCategory, itemAmount);
        //TODO :Fix this abomination
        String jsonBody = "["+gson.toJson(entry).toString()+"]";
        Log.d("DEBUG", "Add to list this : " + jsonBody);

        StringRequest request = new HttpRequestImpl(Request.Method.POST, postUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Response :" + response);
                getList();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Onclick", "Error : "+ error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonBody == null ? null : jsonBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody, "utf-8");
                    return null;
                }
            }

        };
        RequestQueue requestQueue = RequestQueueProvider.getRequestQueue(this.getApplicationContext());
        requestQueue.add(request);
    }

    public void deleteFromListRequest(TodoEntry entry) {
        String jsonBody = "["+gson.toJson(entry)+"]";
        Log.d("DELETE", jsonBody);
        StringRequest request = new HttpRequestImpl(Request.Method.DELETE, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Response :" + response);
                getList();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("On Delete Request", "Error : "+ error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonBody == null ? null : jsonBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody, "utf-8");
                    return null;
                }
            }

        };
        try {
            Log.d("Request", new String(request.getBody()));
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        RequestQueue requestQueue = RequestQueueProvider.getRequestQueue(this.getApplicationContext());
        requestQueue.add(request);
    }

    public void clearEditTexts() {
        cdNameET.setText("");
        cdLanguageET.setText("");
        cdCategoryET.setText("");
        cdAmountET.setText("");
    }

}
