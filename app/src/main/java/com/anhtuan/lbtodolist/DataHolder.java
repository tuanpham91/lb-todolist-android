package com.anhtuan.lbtodolist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Response;
import com.anhtuan.command.IntentCommand;
import com.anhtuan.custom.ListViewArrayAdapter;
import com.anhtuan.custom.ModifyItemDialog;
import com.anhtuan.global.dataholder.AllItemListDataHolder;
import com.anhtuan.global.dataholder.UserDataHolder;
import com.anhtuan.http.UserDataDAO;
import com.anhtuan.pojo.TodoEntry;
import com.anhtuan.pojo.User;
import com.anhtuan.pojo.UserGroup;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * An instance of this class is created to handle data access for the activity
 */
public class DataHolder {

    private UserDataDAO todoListDAO;
    private Gson gson;
    private String basicAuth;
    private DataCacher cacher;
    private Response.ErrorListener requestErrorListener;
    private Context context;
    public static final String listActivityIntentFilter = "ListActivity";

    // Assume user only have one todolist Id
    private String currentGroupId;

    public DataHolder(Context context, Response.ErrorListener requestErrorListener) {
        this.requestErrorListener = requestErrorListener;
        this.context = context;
        appStartPreparation();
    }

    private void appStartPreparation() {
        gson = new Gson();
        todoListDAO = UserDataDAO.getInstance(context);
        cacher = DataCacher.getCacher(context);
        basicAuth = cacher.readStringFromFile(cacher.basicAuthFile);
        updateTodoList(cacher.readStringFromFile(cacher.localListFile));
        updateAllItemListFromString( cacher.readStringFromFile(cacher.localAllItemsFile));
        updateItemListRequestDAO();
        getUserPersonalInfo();
        getUserGroup();
    }

    private void getUserPersonalInfo() {
        todoListDAO.getUserRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                User user = gson.fromJson(response, User.class);
                UserDataHolder.updateUser(user);
            }
        }, requestErrorListener, basicAuth);
    }

    private void getUserGroup() {
        todoListDAO.getUserGroupRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                UserGroup[] groups = gson.fromJson(response, UserGroup[].class);
                // Update currentGroupId
                updateCacheUserGroup(groups);
                // TODO : This assume the user only have one todo list group
                currentGroupId = UserDataHolder.getUser().getTodoListGroups().get(0);
            }
        }, requestErrorListener, basicAuth);
    }

    private void updateCacheUserGroup(UserGroup[] groups) {
        for (int i = 0; i< groups.length ; i ++) {
            if (groups[i].getGroupType().equals(UserGroup.todoGroupType)) {
                UserDataHolder.getUser().getTodoListGroups().add(groups[i].getGroupId());
                Log.d("Group-Id", "Update Todo Group ID : " + groups[i].getGroupId());
            } else {

                Log.d("Group-Id", "Update Expense Group ID : " + groups[i].getGroupId());
                UserDataHolder.getUser().getExpenseGroups().add(groups[i].getGroupId());
            }
        }

        cacher.saveStringToFile(cacher.userInfoFile, gson.toJson(UserDataHolder.getUser()));
    }

    public void updateAllItemListFromString(String response) {
        AllItemListDataHolder.setAllUniqueItemList(new ArrayList<>(Arrays.asList(response.split(";"))));
        // cache the item too
        cacher.saveStringToFile(cacher.localAllItemsFile, response);
    }

    public void deleteFromListRequestDAO(TodoEntry entry) {
        String jsonBody = "["+gson.toJson(entry)+"]";
        todoListDAO.deleteFromListRequest(jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                updateAdapter();
            }
        }, requestErrorListener, basicAuth);
    }

    public void updateItemFromList(TodoEntry oldEntry, ModifyItemDialog updateDialog) {
        TodoEntry newEntry = personifiedRawTodoListEntry(updateDialog.getRawEntry());
        updateItemFromListRequestDAO(oldEntry, newEntry);
    }

    public void updateItemFromListRequestDAO(TodoEntry oldEntry, TodoEntry newEntry) {
        String jsonBody = "["+gson.toJson(oldEntry)+","+gson.toJson(newEntry)+"]";
        todoListDAO.updateItemFromListRequest(jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getListDAO(currentGroupId);
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

    public void addToListRequestDAO(TodoEntry entry) {
        // good enough for now
        String jsonBody = "["+gson.toJson(entry)+"]";
        todoListDAO.addToListRequest(jsonBody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getListDAO(currentGroupId);
                updateAdapter();
            }
        }, requestErrorListener, basicAuth);
    }

    public void updateExistingEntry(TodoEntry oldEntry, TodoEntry entry) {
        entry.setAmount(entry.getAmount() + oldEntry.getAmount());
        updateItemFromListRequestDAO(oldEntry, entry );
        Toast alreadyExistToast = Toast.makeText(context, "Item already exists, merged amount..", Toast.LENGTH_LONG);
        alreadyExistToast.show();
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

    public void getListDAO(String groupId) {
        todoListDAO.getTodoListRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cacher.cacheTodoListContent(response);
                updateTodoList(response);
            }
        }, requestErrorListener, basicAuth, groupId);
    }

    public void updateTodoList(String response) {
        if (response.equals("[]")) return;
        sendIntentToActivity(listActivityIntentFilter, IntentCommand.UPDATE_LIST_DATASET, response );
    }

    public void updateAdapter() {
        sendIntentToActivity(listActivityIntentFilter, IntentCommand.NOTIFY_DATASET_ADAPTER, "");
    }

    public String getCurrentGroupId() {
        return currentGroupId;
    }

    public void sendIntentToActivity(String destinationActivity, String key, String content) {
        Intent intent = new Intent(destinationActivity);
        intent.putExtra("command", key);
        intent.putExtra("content", content);
        context.sendBroadcast(intent);
    }
}
