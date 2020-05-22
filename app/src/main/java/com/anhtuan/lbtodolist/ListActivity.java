package com.anhtuan.lbtodolist;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.anhtuan.custom.ListViewArrayAdapter;
import com.anhtuan.http.HttpRequestImpl;
import com.anhtuan.http.RequestQueueProvider;
import com.anhtuan.pojo.TodoEntry;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class ListActivity extends Activity {
    // Pi : 26
    private static String baseUrl = "http://192.168.178.26:8080";
    private static String addUrl = baseUrl + "/todolist";
    private static String postUrl = baseUrl + "/addtodolist";
    private static String deleteUrl = baseUrl + "/deletetodolist";
    private static String updateUrl = baseUrl + "/updatetodolist";
    private static String allItemUrl = baseUrl + "/allitemlist";
    private static String language = "Deutsch";

    private ListViewArrayAdapter adapter;
    Button clickButton;
    ImageButton addButton;
    ListView todoListView;
    Dialog createDialog;
    Gson gson = new Gson();
    Button closeDiaglogButton;
    Button createDiaglogButton;

    AutoCompleteTextView cdNameET;
    Spinner cdCategorySpinner;
    EditText cdAmountET;

    Button closeUpdateDialogButton;
    Button applyUpdateDialogButton;

    AutoCompleteTextView udNameET;
    Spinner udCategorySpinner;
    EditText udAmountET;
    Dialog updateDialog;
    RequestQueue requestQueue;
    ArrayAdapter<CharSequence> spinnerAdapter;
    ArrayAdapter<String> itemSuggestionListAdapter;

    public TodoEntry currentTodoEntry;
    ArrayList<String> allItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_activity);

        requestQueue = RequestQueueProvider.getRequestQueue(this.getApplicationContext());

        clickButton = (Button) findViewById(R.id.placeholder);
        addButton = (ImageButton) findViewById(R.id.addEntryButton);
        todoListView = (ListView) findViewById(R.id.todoList);

        adapter = new ListViewArrayAdapter(this.getApplicationContext(), android.R.layout.simple_list_item_1, this);
        todoListView.setAdapter(adapter);

        createDialog = new Dialog(this);
        createDialog.setContentView(R.layout.add_item_dialog);
        createDialog.setTitle("Add entry");

        // Create Dialog
        closeDiaglogButton = (Button) createDialog.findViewById(R.id.create_dialog_cancel_button);
        createDiaglogButton = (Button) createDialog.findViewById(R.id.create_dialog_create_button);
        cdNameET = (AutoCompleteTextView) createDialog.findViewById(R.id.create_dialog_et_1);
        cdCategorySpinner = (Spinner) createDialog.findViewById(R.id.create_dialog_et_3);
        cdAmountET = (EditText) createDialog.findViewById(R.id.create_dialog_et_4);

        // Array Adapter for Spinner
        spinnerAdapter =
                ArrayAdapter.createFromResource(this, R.array.category_array, R.layout.spinner_item);
        cdCategorySpinner.setAdapter(spinnerAdapter);


        // Update Dialog
        updateDialog = new Dialog(this);
        updateDialog.setContentView(R.layout.update_dialog);
        updateDialog.setTitle("Change Entry");

        allItemList = new ArrayList<>();
        updateItemListRequest();

        closeUpdateDialogButton = (Button) updateDialog.findViewById(R.id.update_dialog_cancel_button);
        applyUpdateDialogButton = (Button) updateDialog.findViewById(R.id.update_dialog_update_button);
        udNameET = (AutoCompleteTextView) updateDialog.findViewById(R.id.update_dialog_et_1);
        udCategorySpinner = (Spinner) updateDialog.findViewById(R.id.update_dialog_et_2);
        udAmountET = (EditText) updateDialog.findViewById(R.id.update_dialog_et_3);

        udCategorySpinner.setAdapter(spinnerAdapter);


        itemSuggestionListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, allItemList);
        cdNameET.setAdapter(itemSuggestionListAdapter);
        udNameET.setAdapter(itemSuggestionListAdapter);

        closeDiaglogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog.dismiss();
            }
        });

        applyUpdateDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemFromListRequest(currentTodoEntry);
                updateDialog.dismiss();
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

        closeDiaglogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog.dismiss();
            }
        });

        closeUpdateDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }
        });
    }

    public void getList() {
        StringRequest request = new HttpRequestImpl(Request.Method.GET, addUrl,"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                updateList(response);
            }
        });
        requestQueue.add(request);
    }

    public void updateList(String response) {
        TodoEntry[] entryList = gson.fromJson(response, TodoEntry[].class);
        adapter.clear();
        adapter.addAll(entryList);
        adapter.notifyDataSetChanged();
    }

    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

    public void openCreateDialog() {
        cdAmountET.setText("1");
        cdCategorySpinner.setSelection(0);
        createDialog.show();
    }

    public void openUpdateDialog(TodoEntry currentEntry) {
        this.currentTodoEntry = currentEntry;
        udNameET.setText(currentEntry.getValue());
        udAmountET.setText(currentEntry.getAmount().toString());
        udCategorySpinner.setSelection(spinnerAdapter.getPosition(currentEntry.getKeywordCategory()));
        updateDialog.show();
    }

    public void addToListRequest() {
        String itemName = cdNameET.getText().toString();
        String itemCategory = cdCategorySpinner.getSelectedItem().toString();
        Long itemAmount = Long.valueOf(cdAmountET.getText().toString());
        TodoEntry entry = new TodoEntry(itemName, System.currentTimeMillis(), language, itemCategory, itemAmount);
        //TODO :Fix this abomination
        String jsonBody = "["+gson.toJson(entry)+"]";
        adapter.add(entry);
        StringRequest request = new HttpRequestImpl(Request.Method.POST, postUrl, jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Response :" + response);
                updateAdapter();
            }
        });
        requestQueue.add(request);
    }

    public void updateItemListRequest() {
        StringRequest request = new HttpRequestImpl(Request.Method.POST, allItemUrl, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Response :" + response);
                updateItemList(response);
            }
        });
        requestQueue.add(request);
    }

    public void updateItemList(String response) {
        allItemList = new ArrayList<String>(Arrays.asList(response.split(";")));
    }

    public void deleteFromListRequest(TodoEntry entry) {
        String jsonBody = "["+gson.toJson(entry)+"]";
        Log.d("DELETE", jsonBody);
        adapter.remove(entry);
        StringRequest request = new HttpRequestImpl(Request.Method.POST, deleteUrl, jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Response :" + response);
                updateAdapter();
            }
        });
        requestQueue.add(request);
    }

    public void updateItemFromListRequest(TodoEntry oldEntry) {
        TodoEntry newEntry =
                new TodoEntry(udNameET.getText().toString(), oldEntry.getDate(), oldEntry.getLanguage(), udAmountET.getText().toString(), Long.valueOf(udAmountET.getText().toString()));
        String jsonBody = "["+gson.toJson(oldEntry)+","+gson.toJson(newEntry)+"]";
        StringRequest request  = new HttpRequestImpl(Request.Method.POST, updateUrl, jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Response :" + response);
                getList();
            }
        });
        requestQueue.add(request);
    }

}
