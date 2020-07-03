package com.anhtuan.lbtodolist;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.anhtuan.command.IntentCommand;
import com.anhtuan.custom.ListViewArrayAdapter;
import com.anhtuan.custom.ModifyItemDialog;
import com.anhtuan.global.dataholder.UserDataHolder;
import com.anhtuan.pojo.TodoEntry;
import com.google.gson.Gson;

public class ListActivity extends Activity {
    // Pi : 26 Local : 21
    public static String LANGUAGE = "Deutsch";
    ImageButton addButton;
    ListView todoListView;
    ModifyItemDialog createDialog;
    ModifyItemDialog updateDialog;

    Response.ErrorListener requestErrorListener;
    public TodoEntry currentTodoEntry;
    private DataHolder dataHolder;
    private ListViewArrayAdapter listViewArrayAdapter;
    private BroadcastReceiver broadcastReceiver;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Gson gson;

    private void handleUnauthorized() {
        Intent moveToMainActivity = new Intent(this, MainActivity.class);
        moveToMainActivity.putExtra("Intention", "Error");
        startActivity(moveToMainActivity);
        Toast unauthorizedToast = Toast.makeText(ListActivity.this, "Unauthorized. Login again", Toast.LENGTH_LONG);
        unauthorizedToast.show();
    }

    public DataHolder getDataHolder() {
        return dataHolder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_activity);
        gson = new Gson();
        // initialize broadcastReceiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleBroadcastMessage(intent);
            }
        };
        IntentFilter intentFilter = new IntentFilter(DataHolder.listActivityIntentFilter);
        this.registerReceiver(broadcastReceiver, intentFilter);

        listViewArrayAdapter = new ListViewArrayAdapter(android.R.layout.simple_list_item_1, this);
        swipeRefreshLayout = findViewById(R.id.pullToRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTodoList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        requestErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Request-Error", error.getLocalizedMessage());
                if (error.networkResponse == null) {
                    return;
                }
                if (error.networkResponse.statusCode == 401) {
                    handleUnauthorized();
                }
            }
        };

        addButton = (ImageButton) findViewById(R.id.addEntryButton);
        todoListView = (ListView) findViewById(R.id.todoList);
        todoListView.setAdapter(listViewArrayAdapter);

        updateDialog = new ModifyItemDialog(ListActivity.this, "Update Item",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDialog.dismiss();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataHolder.updateItemFromList(currentTodoEntry, updateDialog);
                        updateDialog.dismiss();
                    }
                });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateDialog();
            }
        });
        dataHolder = new DataHolder(this, requestErrorListener);

    }

    public void refreshTodoList() {
        dataHolder.getListDAO(dataHolder.getCurrentGroupId());
    }

    @Override
    public void onBackPressed() { }

    public void openCreateDialog() {
        if (createDialog == null) {
            createDialog = new ModifyItemDialog(ListActivity.this, "Add Entry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog.dismiss();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItemToTodoList(createDialog);
                    createDialog.dismiss();
                }
            });
            dataHolder.updateItemListRequestDAO();
        }
        createDialog.setDefault();
        createDialog.show();
    }

    public void addItemToTodoList(ModifyItemDialog createDialog) {
        TodoEntry entry = personifiedRawTodoListEntry(createDialog.getRawEntry());
        int index = listViewArrayAdapter.findEntry(entry);
        if ( index >= 0) {
            dataHolder.updateExistingEntry(listViewArrayAdapter.getItem(index), entry);
            return;
        }
        listViewArrayAdapter.add(entry);
        dataHolder.addToListRequestDAO(entry);

    }

    public void handleBroadcastMessage(Intent intent) {
        String command = intent.getExtras().getString("command");
        switch(command) {
            case IntentCommand.UPDATE_LIST_DATASET:
                updateTodoList(intent.getExtras().getString("content"));
                break;
            case IntentCommand.NOTIFY_DATASET_ADAPTER:
                listViewArrayAdapter.notifyDataSetChanged();
                break;
            default:
                break;

        }
    }

    public void updateTodoList(String content) {
        TodoEntry[] entryList = gson.fromJson(content, TodoEntry[].class);
        listViewArrayAdapter.clear();
        listViewArrayAdapter.addAll(entryList);
        listViewArrayAdapter.notifyDataSetChanged();
    }

    public void openUpdateDialog(TodoEntry currentEntry) {
        this.currentTodoEntry = currentEntry;
        updateDialog.setEntry(currentEntry);
    }

    public TodoEntry personifiedRawTodoListEntry(TodoEntry rawEntry) {
        return new TodoEntry(rawEntry.getValue(),
                System.currentTimeMillis(),
                ListActivity.LANGUAGE,
                rawEntry.getKeywordCategory(),
                rawEntry.getAmount(),
                UserDataHolder.getUser().getTodoListGroups().get(0),
                UserDataHolder.getUser().getUserId());
    }

}
