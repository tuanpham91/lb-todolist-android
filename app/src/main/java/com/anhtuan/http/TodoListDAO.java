package com.anhtuan.http;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;

public class TodoListDAO {
    public static String baseUrl = "http://192.168.178.26:8080";
    public static String addUrl = baseUrl + "/todolist/todolist";
    public static String postUrl = baseUrl + "/todolist/addtodolist";
    public static String deleteUrl = baseUrl + "/todolist/deletetodolist";
    public static String updateUrl = baseUrl + "/todolist/updatetodolist";
    public static String allItemUrl = baseUrl + "/todolist/allitemlist";
    public static String authentication = baseUrl+ "/group/authenticateUser";

    private static TodoListDAO todoListDAO;
    private RequestQueueProvider requestQueueProvider;

    private TodoListDAO(RequestQueueProvider requestQueue) {
        this.requestQueueProvider = requestQueue;
    }

    public static TodoListDAO getInstance(Context context) {
        if (todoListDAO == null) {
            todoListDAO = new TodoListDAO(RequestQueueProvider.getRequestQueueProvider(context));
        }
        return todoListDAO;
    }

    public void getUser(Response.Listener responseListener, Response.ErrorListener errorListener, String auth) {
        HttpRequestImpl request = new HttpRequestImpl(Request.Method.GET, TodoListDAO.authentication, "", responseListener, errorListener, auth);
        requestQueueProvider.addToQueue(request);
    }

    public void getList(Response.Listener repsonseListener, Response.ErrorListener errorListener, String auth) {
        HttpRequestImpl request = new HttpRequestImpl(Request.Method.GET, TodoListDAO.addUrl, "", repsonseListener, errorListener, auth);
        requestQueueProvider.addToQueue(request);
    }

    public void addToListRequest(String jsonBody, Response.Listener responseListner, Response.ErrorListener errorListener, String auth) {
        HttpRequestImpl request = new HttpRequestImpl(Request.Method.POST, postUrl, jsonBody, responseListner, errorListener, auth);
        requestQueueProvider.addToQueue(request);
    }

    public void updateItemListRequest(Response.Listener responseListener, Response.ErrorListener errorListener, String auth) {
        HttpRequestImpl request = new HttpRequestImpl(Request.Method.GET, TodoListDAO.allItemUrl, "", responseListener, errorListener, auth);
        requestQueueProvider.addToQueue(request);
    }

    public void deleteFromListRequest(String jsonBody, Response.Listener responseListener, Response.ErrorListener errorListener, String auth) {
        HttpRequestImpl request = new HttpRequestImpl(Request.Method.POST, TodoListDAO.deleteUrl, jsonBody, responseListener, errorListener, auth);
        requestQueueProvider.addToQueue(request);
    }

    public void updateItemFromListRequest(String jsonBody, Response.Listener responseListener, Response.ErrorListener errorListener, String auth) {
        HttpRequestImpl request = new HttpRequestImpl(Request.Method.POST, TodoListDAO.updateUrl, jsonBody, responseListener, errorListener,  auth);
        requestQueueProvider.addToQueue(request);
    }

}