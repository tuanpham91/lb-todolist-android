package com.anhtuan.lbtodolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.anhtuan.custom.ChangeItemDialog;
import com.anhtuan.custom.ListViewArrayAdapter;
import com.anhtuan.custom.UpdateItemDialog;
import com.anhtuan.http.TodoListDAO;
import com.anhtuan.pojo.TodoEntry;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;

public class ListActivity extends Activity {
    // Pi : 26 Local : 21

    // TODO : WHat happens with losing internet connection ?
    // TODO : How to avoid logging everytime ?

    private static String language = "Deutsch";

    private ListViewArrayAdapter adapter;
    Button clickButton;
    ImageButton addButton;
    ListView todoListView;
    ChangeItemDialog createDialog;
    Gson gson = new Gson();

    UpdateItemDialog updateDialog;
    TodoListDAO todoListDAO;
    ArrayAdapter<CharSequence> spinnerAdapter;
    ArrayAdapter<String> itemSuggestionListAdapter;
    DataCacher cacher;
    Response.ErrorListener requestErrorListener;
    public TodoEntry currentTodoEntry;
    ArrayList<String> allItemList;
    private String basicAuth;

    @Override
    protected void onStart() {
        super.onStart();
        cacher = DataCacher.getCacher(this.getApplicationContext());
        // Get TodoList Local File ()
        basicAuth = cacher.readStringFromFile(cacher.basicAuthFile);
        requestErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 401) {
                    handleUnauthorized();
                    //TODO : For another errors like no network found or smth
                }
            }
        };

        String todoListLocal = cacher.readStringFromFile(cacher.localListFile);
        String allItemsLocal = cacher.readStringFromFile(cacher.localAllItemsFile);
        updateList(todoListLocal);
        updateAllItemListFromString(allItemsLocal);
    }

    private void handleUnauthorized() {
        Toast unauthorizedToast = Toast.makeText(ListActivity.this, "Unauthorized. Login again", Toast.LENGTH_LONG);
        unauthorizedToast.show();
        Intent moveToMainActivity = new Intent(this, MainActivity.class);
        startActivity(moveToMainActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_activity);
        todoListDAO = TodoListDAO.getInstance(this.getApplicationContext());

        clickButton = (Button) findViewById(R.id.placeholder);
        addButton = (ImageButton) findViewById(R.id.addEntryButton);
        todoListView = (ListView) findViewById(R.id.todoList);

        adapter = new ListViewArrayAdapter(this.getApplicationContext(), android.R.layout.simple_list_item_1, this);
        todoListView.setAdapter(adapter);

        // Array Adapter for
        spinnerAdapter =
                ArrayAdapter.createFromResource(this, R.array.category_array, R.layout.spinner_item);

        //updateDialog
        allItemList = new ArrayList<>();
        itemSuggestionListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, allItemList);

        updateDialog = new UpdateItemDialog(ListActivity.this, spinnerAdapter, itemSuggestionListAdapter,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDialog.dismiss();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateItemFromListRequestDAO(currentTodoEntry);
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
                getListDAO(basicAuth);
            }
        });

    }

    public void getListDAO(String auth) {
        todoListDAO.getList(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cacher.cacheTodoListContent(response);
                updateList(response);
            }
        }, requestErrorListener, auth);
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

    @Override
    public void onBackPressed() { }

    public void openCreateDialog() {
        if (createDialog == null) {
            createDialog = new ChangeItemDialog("Add Entry", ListActivity.this, spinnerAdapter, itemSuggestionListAdapter, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog.dismiss();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToListRequestDAO();
                    createDialog.dismiss();
                }
            });
            updateItemListRequestDAO();
        }
        createDialog.setDefault();
        createDialog.show();
    }

    public void openUpdateDialog(TodoEntry currentEntry) {
        this.currentTodoEntry = currentEntry;
        updateDialog.getUdNameET().setText(currentEntry.getValue());
        updateDialog.getUdAmountET().setText(currentEntry.getAmount().toString());
        updateDialog.getUdCategorySpinner().setSelection(spinnerAdapter.getPosition(currentEntry.getKeywordCategory()));
        updateDialog.show();
    }

    public void addToListRequestDAO() {
        String itemName = createDialog.getCdNameET().getText().toString();
        String itemCategory = createDialog.getCdCategorySpinner().getSelectedItem().toString();
        Long itemAmount = Long.valueOf(createDialog.getCdAmountET().getText().toString());
        TodoEntry entry = new TodoEntry(itemName, System.currentTimeMillis(), language, itemCategory, itemAmount);

        //TODO :Fix this abomination
        String jsonBody = "["+gson.toJson(entry)+"]";
        adapter.add(entry);
        todoListDAO.addToListRequest(jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Response :" + response);
                updateAdapter();
            }
        }, requestErrorListener, basicAuth);
    }

    public void updateItemListRequestDAO() {
        todoListDAO.updateItemListRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                updateAllItemListFromString(response);
            }
        }, requestErrorListener, basicAuth);
    }

    public void updateAllItemListFromString(String response) {
        allItemList.clear();
        allItemList.addAll(Arrays.asList(response.split(";")));
        itemSuggestionListAdapter.clear();
        itemSuggestionListAdapter.addAll(allItemList);
        itemSuggestionListAdapter.notifyDataSetChanged();
    }

    public void deleteFromListRequestDAO(TodoEntry entry) {
        String jsonBody = "["+gson.toJson(entry)+"]";
        adapter.remove(entry);
        todoListDAO.deleteFromListRequest(jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                updateAdapter();
            }
        }, requestErrorListener, basicAuth);
    }

    public void updateItemFromListRequestDAO(TodoEntry oldEntry) {
        TodoEntry newEntry =
                new TodoEntry(updateDialog.getUdNameET().getText().toString(),
                        oldEntry.getDate(), oldEntry.getLanguage(),
                        updateDialog.getUdAmountET().getText().toString(),
                        Long.valueOf(updateDialog.getUdAmountET().getText().toString()));

        String jsonBody = "["+gson.toJson(oldEntry)+","+gson.toJson(newEntry)+"]";
        todoListDAO.updateItemFromListRequest(jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getListDAO(basicAuth);
            }
        }, requestErrorListener, basicAuth);
    }
}
