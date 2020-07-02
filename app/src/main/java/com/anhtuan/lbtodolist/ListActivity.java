package com.anhtuan.lbtodolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.anhtuan.custom.ModifyItemDialog;
import com.anhtuan.pojo.TodoEntry;

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

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.pullToRefresh);
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
        dataHolder = new DataHolder(this, requestErrorListener);
        addButton = (ImageButton) findViewById(R.id.addEntryButton);
        todoListView = (ListView) findViewById(R.id.todoList);
        todoListView.setAdapter(dataHolder.getListViewArrayAdapter());

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
                    dataHolder.addToListRequestDAO(createDialog);
                    createDialog.dismiss();
                }
            });
            dataHolder.updateItemListRequestDAO();
        }
        createDialog.setDefault();
        createDialog.show();
    }

    public void openUpdateDialog(TodoEntry currentEntry) {
        this.currentTodoEntry = currentEntry;
        updateDialog.setEntry(currentEntry);
    }

}
