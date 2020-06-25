package com.anhtuan.lbtodolist;

import android.util.Log;
import android.widget.Toast;
import com.android.volley.Response;
import com.anhtuan.custom.ChangeItemDialog;
import com.anhtuan.custom.ListViewArrayAdapter;
import com.anhtuan.custom.UpdateItemDialog;
import com.anhtuan.global.dataholder.AllItemListDataHolder;
import com.anhtuan.global.dataholder.UserDataHolder;
import com.anhtuan.http.TodoListDAO;
import com.anhtuan.pojo.TodoEntry;
import com.anhtuan.pojo.User;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * An instance of this class is created to handle data access for the activity
 */
public class ListActivityDataHolder {

    private TodoListDAO todoListDAO;
    private Gson gson;
    private ListViewArrayAdapter listViewArrayAdapter;
    private String basicAuth;
    private ListActivity listActivity;
    private DataCacher cacher;
    private Response.ErrorListener requestErrorListener;

    public ListActivityDataHolder(ListActivity listActivity, Response.ErrorListener requestErrorListener) {
        this.listActivity = listActivity;
        this.requestErrorListener = requestErrorListener;
        appStartPreparation();
    }

    private void appStartPreparation() {
        gson = new Gson();
        todoListDAO = TodoListDAO.getInstance(listActivity.getApplicationContext());
        cacher = DataCacher.getCacher(listActivity.getApplicationContext());
        listViewArrayAdapter = new ListViewArrayAdapter(android.R.layout.simple_list_item_1, listActivity);
        this.basicAuth = cacher.readStringFromFile(cacher.basicAuthFile);
        getUserPersonalInfo();
    }

    private void getUserPersonalInfo() {
        todoListDAO.getUser(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                User user = gson.fromJson(response, User.class);
                UserDataHolder.setUser(user);
            }
        }, requestErrorListener, basicAuth);
    }

    public void updateAllItemListFromString(String response) {
        AllItemListDataHolder.setAllUniqueItemList(new ArrayList<>(Arrays.asList(response.split(";"))));
        // Still needs to notify the adapter
    }

    public void deleteFromListRequestDAO(TodoEntry entry) {
        String jsonBody = "["+gson.toJson(entry)+"]";
        listViewArrayAdapter.remove(entry);
        todoListDAO.deleteFromListRequest(jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                updateAdapter();
            }
        }, requestErrorListener, basicAuth);
    }

    public void updateItemFromList(TodoEntry oldEntry, UpdateItemDialog updateDialog) {
        TodoEntry newEntry =
                new TodoEntry(updateDialog.getUdNameET().getText().toString(),
                        oldEntry.getDate(), oldEntry.getLanguage(),
                        updateDialog.getUdAmountET().getText().toString(),
                        Long.valueOf(updateDialog.getUdAmountET().getText().toString()),
                        );
        updateItemFromListRequestDAO(oldEntry, newEntry);
    }

    public void updateItemFromListRequestDAO(TodoEntry oldEntry, TodoEntry newEntry) {
        String jsonBody = "["+gson.toJson(oldEntry)+","+gson.toJson(newEntry)+"]";
        todoListDAO.updateItemFromListRequest(jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getListDAO();
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

    public void addToListRequestDAO(ChangeItemDialog createDialog) {
        String itemName = createDialog.getCdNameET().getText().toString();
        String itemCategory = createDialog.getCdCategorySpinner().getSelectedItem().toString();
        Long itemAmount = Long.valueOf(createDialog.getCdAmountET().getText().toString());
        TodoEntry entry = new TodoEntry(itemName, System.currentTimeMillis(), ListActivity.LANGUAGE, itemCategory, itemAmount);

        int index = listViewArrayAdapter.findEntry(entry);
        if ( index >= 0) {
            TodoEntry oldEntry = listViewArrayAdapter.getItem(index);
            entry.setAmount(entry.getAmount() + oldEntry.getAmount());
            updateItemFromListRequestDAO(oldEntry, entry );
            Toast alreadyExistToast = Toast.makeText(listActivity.getApplicationContext(), "Item already exists, merged amount..", Toast.LENGTH_LONG);
            alreadyExistToast.show();
            return;
        }
        // good enough for now
        String jsonBody = "["+gson.toJson(entry)+"]";
        listViewArrayAdapter.add(entry);
        Log.d("DEBUG", jsonBody);
        todoListDAO.addToListRequest(jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Response :" + response);
                getListDAO();
                updateAdapter();
            }
        }, requestErrorListener, basicAuth);
    }

    public void getListDAO() {
        todoListDAO.getList(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cacher.cacheTodoListContent(response);
                updateTodoList(response);
            }
        }, requestErrorListener, basicAuth);
    }

    public void updateTodoList(String response) {
        TodoEntry[] entryList = gson.fromJson(response, TodoEntry[].class);
        if (entryList == null) return;
        listViewArrayAdapter.clear();
        listViewArrayAdapter.addAll(entryList);
        listViewArrayAdapter.notifyDataSetChanged();
    }

    public void updateAdapter() {
        listViewArrayAdapter.notifyDataSetChanged();
    }

    public DataCacher getCacher() {
        return cacher;
    }

    public ListViewArrayAdapter getListViewArrayAdapter() {
        return listViewArrayAdapter;
    }

    public String getBasicAuth() {
        return basicAuth;
    }

    public TodoListDAO getTodoListDAO() {
        return todoListDAO;
    }

    public void setBasicAuth(String basicAuth) {
        this.basicAuth = basicAuth;
    }

}
