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
import com.anhtuan.http.TodoListDAO;
import com.anhtuan.pojo.TodoEntry;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;

import static com.anhtuan.http.TodoListDAO.postUrl;

public class ListActivity extends Activity {
    // Pi : 26 Local : 21

    // TODO : WHat happens with losing internet connection ?
    // TODO : How to avoid logging everytime ?

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
    DataCacher cacher;
    public TodoEntry currentTodoEntry;
    ArrayList<String> allItemList;
    private String basicAuth;
    @Override
    protected void onStart() {
        super.onStart();
        cacher = DataCacher.getCacher(this.getApplicationContext());
        // Get TodoList Local File ()
        basicAuth = cacher.readStringFromFile(cacher.basicAuthFile);
        String todoListLocal = cacher.readStringFromFile(cacher.localListFile);
        String allItemsLocal = cacher.readStringFromFile(cacher.localAllItemsFile);
        updateList(todoListLocal);
        updateAllItemListFromString(allItemsLocal);
    }

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

        // Array Adapter for
        spinnerAdapter =
                ArrayAdapter.createFromResource(this, R.array.category_array, R.layout.spinner_item);

        //updateDialog
        updateDialog = new Dialog(this);
        updateDialog.setContentView(R.layout.update_dialog);
        updateDialog.setTitle("Change Entry");

        allItemList = new ArrayList<>();

        closeUpdateDialogButton = (Button) updateDialog.findViewById(R.id.update_dialog_cancel_button);
        applyUpdateDialogButton = (Button) updateDialog.findViewById(R.id.update_dialog_update_button);
        udNameET = (AutoCompleteTextView) updateDialog.findViewById(R.id.update_dialog_et_1);
        udCategorySpinner = (Spinner) updateDialog.findViewById(R.id.update_dialog_et_2);
        udAmountET = (EditText) updateDialog.findViewById(R.id.update_dialog_et_3);

        udCategorySpinner.setAdapter(spinnerAdapter);

        itemSuggestionListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, allItemList);
        udNameET.setAdapter(itemSuggestionListAdapter);

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

        closeUpdateDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }
        });
    }

    public void getList() {
        StringRequest request = new HttpRequestImpl(Request.Method.GET, TodoListDAO.addUrl,"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cacher.cacheTodoListContent(response);
                updateList(response);
            }
        }, basicAuth);
        requestQueue.add(request);
    }


    public void updateList(String response) {
        TodoEntry[] entryList = gson.fromJson(response, TodoEntry[].class);
        if (entryList == null) return;
        adapter.clear();
        adapter.addAll(entryList);
        adapter.notifyDataSetChanged();
    }

    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

    public void initializeDialog () {
        createDialog = new Dialog(this);
        createDialog.setContentView(R.layout.add_item_dialog);
        createDialog.setTitle("Add entry");

        createDiaglogButton = (Button) createDialog.findViewById(R.id.create_dialog_create_button);
        closeDiaglogButton = (Button) createDialog.findViewById(R.id.create_dialog_cancel_button);
        cdNameET = (AutoCompleteTextView) createDialog.findViewById(R.id.create_dialog_et_1);
        cdCategorySpinner = (Spinner) createDialog.findViewById(R.id.create_dialog_et_3);
        cdAmountET = (EditText) createDialog.findViewById(R.id.create_dialog_et_4);
        cdCategorySpinner.setAdapter(spinnerAdapter);

        cdNameET.setAdapter(itemSuggestionListAdapter);


        closeDiaglogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog.dismiss();
            }
        });

        createDiaglogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToListRequest();
                createDialog.dismiss();
            }
        });

        updateItemListRequest();
    }

    @Override
    public void onBackPressed() { }
    
    public void openCreateDialog() {
        if (createDialog == null) {
            initializeDialog();
        }
        cdNameET.setText("");
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
        }, basicAuth);

        requestQueue.add(request);
    }

    public void updateItemListRequest() {
        StringRequest request = new HttpRequestImpl(Request.Method.GET, TodoListDAO.allItemUrl, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                updateAllItemListFromString(response);
            }
        }, basicAuth);
        requestQueue.add(request);
    }

    public void updateAllItemListFromString(String response) {
        Log.d("UPDATE", "updateItemListFromString was called");

        allItemList.clear();
        allItemList.addAll(Arrays.asList(response.split(";")));
        itemSuggestionListAdapter.clear();
        itemSuggestionListAdapter.addAll(allItemList);
        itemSuggestionListAdapter.notifyDataSetChanged();
    }

    public void deleteFromListRequest(TodoEntry entry) {
        String jsonBody = "["+gson.toJson(entry)+"]";
        adapter.remove(entry);
        StringRequest request = new HttpRequestImpl(Request.Method.POST, TodoListDAO.deleteUrl, jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Response :" + response);
                updateAdapter();
            }
        }, basicAuth);
        requestQueue.add(request);
    }

    public void updateItemFromListRequest(TodoEntry oldEntry) {
        TodoEntry newEntry =
                new TodoEntry(udNameET.getText().toString(), oldEntry.getDate(), oldEntry.getLanguage(), udAmountET.getText().toString(), Long.valueOf(udAmountET.getText().toString()));
        String jsonBody = "["+gson.toJson(oldEntry)+","+gson.toJson(newEntry)+"]";
        StringRequest request  = new HttpRequestImpl(Request.Method.POST, TodoListDAO.updateUrl, jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Response :" + response);
                getList();
            }
        }, basicAuth);
        requestQueue.add(request);
    }

}
