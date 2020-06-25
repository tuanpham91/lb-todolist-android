package com.anhtuan.lbtodolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.anhtuan.custom.ChangeItemDialog;
import com.anhtuan.custom.UpdateItemDialog;
import com.anhtuan.pojo.TodoEntry;

public class ListActivity extends Activity {
    // Pi : 26 Local : 21

    public static String LANGUAGE = "Deutsch";
    Button clickButton;
    ImageButton addButton;
    ListView todoListView;
    ChangeItemDialog createDialog;
    UpdateItemDialog updateDialog;

    Response.ErrorListener requestErrorListener;
    public TodoEntry currentTodoEntry;
    private ListActivityDataHolder lADH;
    private DataCacher cacher;

    @Override
    protected void onStart() {
        super.onStart();
        // Get TodoList Local File ()
        cacher = lADH.getCacher();
        String todoListLocal = cacher.readStringFromFile(cacher.localListFile);
        String allItemsLocal = cacher.readStringFromFile(cacher.localAllItemsFile);
        lADH.updateTodoList(todoListLocal);
        lADH.updateAllItemListFromString(allItemsLocal);
    }

    private void handleUnauthorized() {
        Intent moveToMainActivity = new Intent(this, MainActivity.class);
        moveToMainActivity.putExtra("Intention", "Error");
        startActivity(moveToMainActivity);
        Toast unauthorizedToast = Toast.makeText(ListActivity.this, "Unauthorized. Login again", Toast.LENGTH_LONG);
        unauthorizedToast.show();
    }

    public ListActivityDataHolder getlADH() {
        return lADH;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_activity);
        requestErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    Log.d("Debug-NoInternet", error.getLocalizedMessage());
                    return;
                }
                if (error.networkResponse.statusCode == 401) {
                    handleUnauthorized();
                }
            }
        };
        lADH = new ListActivityDataHolder(this, requestErrorListener);
        clickButton = (Button) findViewById(R.id.placeholder);
        addButton = (ImageButton) findViewById(R.id.addEntryButton);
        todoListView = (ListView) findViewById(R.id.todoList);
        todoListView.setAdapter(lADH.getListViewArrayAdapter());

        updateDialog = new UpdateItemDialog(ListActivity.this,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDialog.dismiss();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lADH.updateItemFromList(currentTodoEntry, updateDialog);
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
                lADH.getListDAO();
            }
        });

    }

    @Override
    public void onBackPressed() { }

    public void openCreateDialog() {
        if (createDialog == null) {
            createDialog = new ChangeItemDialog("Add Entry", ListActivity.this,  new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog.dismiss();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lADH.addToListRequestDAO(createDialog);
                    createDialog.dismiss();
                }
            });
            lADH.updateItemListRequestDAO();
        }
        createDialog.setDefault();
        createDialog.show();
    }

    public void openUpdateDialog(TodoEntry currentEntry) {
        this.currentTodoEntry = currentEntry;
        updateDialog.showEntry(currentEntry);
    }

}
