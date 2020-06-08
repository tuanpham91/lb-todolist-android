package com.anhtuan.http;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class TodoListDAO {
    public static String baseUrl = "http://192.168.178.26:8080";
    public static String addUrl = baseUrl + "/todolist";
    public static String postUrl = baseUrl + "/addtodolist";
    public static String deleteUrl = baseUrl + "/deletetodolist";
    public static String updateUrl = baseUrl + "/updatetodolist";
    public static String allItemUrl = baseUrl + "/allitemlist";

    private static TodoListDAO todoListDAO;
    private RequestQueue requestQueue;

    private TodoListDAO(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public static TodoListDAO getInstance(Context context) {
        if (todoListDAO == null) {
            todoListDAO = new TodoListDAO(RequestQueueProvider.getRequestQueue(context));
        }
        return todoListDAO;
    }

    public void getList(Response.Listener repsonseListener, Response.ErrorListener errorListener, String auth) {
        StringRequest request = new HttpRequestImpl(Request.Method.GET, TodoListDAO.addUrl, "", repsonseListener, errorListener, auth);
        requestQueue.add(request);
    }

    public void addToListRequest(String jsonBody, Response.Listener responseListner, Response.ErrorListener errorListener, String auth) {
        StringRequest request = new HttpRequestImpl(Request.Method.POST, postUrl, jsonBody, responseListner, errorListener, auth);
        requestQueue.add(request);
    }

    public void updateItemListRequest(Response.Listener responseListener, Response.ErrorListener errorListener, String auth) {
        StringRequest request = new HttpRequestImpl(Request.Method.GET, TodoListDAO.allItemUrl, "", responseListener, errorListener, auth);
        requestQueue.add(request);
    }

    public void deleteFromListRequest(String jsonBody, Response.Listener responseListener, Response.ErrorListener errorListener, String auth) {
        StringRequest request = new HttpRequestImpl(Request.Method.POST, TodoListDAO.deleteUrl, jsonBody, responseListener, errorListener, auth);
        requestQueue.add(request);
    }

    public void updateItemFromListRequest(String jsonBody, Response.Listener responseListener, Response.ErrorListener errorListener, String auth) {
        StringRequest request = new HttpRequestImpl(Request.Method.POST, TodoListDAO.updateUrl, jsonBody, responseListener, errorListener,  auth);
        requestQueue.add(request);
    }

}