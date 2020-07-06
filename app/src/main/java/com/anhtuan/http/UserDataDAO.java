package com.anhtuan.http;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Collections;
import java.util.Map;

public class UserDataDAO {
    public static String baseUrl = "http://192.168.178.26:8080";
    public static String getTodoListUrl = baseUrl + "/todolist/todolist";
    public static String postUrl = baseUrl + "/todolist/addtodolist";
    public static String deleteUrl = baseUrl + "/todolist/deletetodolist";
    public static String updateUrl = baseUrl + "/todolist/updatetodolist";
    public static String allItemUrl = baseUrl + "/todolist/allitemlist";
    public static String authentication = baseUrl+ "/group/authenticateUser";
    public static String getUserGroupListUrl = baseUrl + "/group/getUserGroup";
    public static int GET_METHOD = Request.Method.GET;
    public static int POST_METHOD = Request.Method.POST;

    private static UserDataDAO userDataDAO;
    private RequestQueueProvider requestQueueProvider;

    private UserDataDAO(RequestQueueProvider requestQueue) {
        this.requestQueueProvider = requestQueue;
    }

    public static UserDataDAO getInstance(Context context) {
        if (userDataDAO == null) {
            userDataDAO = new UserDataDAO(RequestQueueProvider.getRequestQueueProvider(context));
        }
        return userDataDAO;
    }

    public void getUserRequest(Response.Listener responseListener, Response.ErrorListener errorListener, String auth) {
        buildAndAddRequestToQueue(GET_METHOD, UserDataDAO.authentication, "", responseListener, errorListener, auth);
    }

    public void getTodoListRequest(Response.Listener repsonseListener, Response.ErrorListener errorListener, String auth, String groupId) {
        buildAndAddRequestToQueueWithParams(GET_METHOD, UserDataDAO.getTodoListUrl, "", repsonseListener, errorListener, auth, Collections.singletonMap("groupId", groupId));
    }

    public void addToListRequest(String jsonBody, Response.Listener responseListner, Response.ErrorListener errorListener, String auth) {
        buildAndAddRequestToQueue(POST_METHOD, postUrl, jsonBody, responseListner, errorListener, auth);
    }

    public void updateItemListRequest(Response.Listener responseListener, Response.ErrorListener errorListener, String auth) {
        buildAndAddRequestToQueue(GET_METHOD, UserDataDAO.allItemUrl, "", responseListener, errorListener, auth);
    }

    public void deleteFromListRequest(String jsonBody, Response.Listener responseListener, Response.ErrorListener errorListener, String auth) {
        buildAndAddRequestToQueue(POST_METHOD, UserDataDAO.deleteUrl, jsonBody, responseListener, errorListener, auth);
    }

    public void updateItemFromListRequest(String jsonBody, Response.Listener responseListener, Response.ErrorListener errorListener, String auth) {
        buildAndAddRequestToQueue(POST_METHOD, UserDataDAO.updateUrl, jsonBody, responseListener, errorListener,  auth);
    }

    public void getUserGroupRequest(Response.Listener responseListener, Response.ErrorListener errorListener, String auth) {
        buildAndAddRequestToQueue(GET_METHOD, getUserGroupListUrl, "", responseListener, errorListener, auth);
    }

    public void buildAndAddRequestToQueue(int method, String url, String body, Response.Listener responseListener, Response.ErrorListener errorListener, String auth) {
        HttpRequestImpl request = new HttpRequestImpl(method, url, body, responseListener, errorListener, auth);
        requestQueueProvider.addToQueue(request);
    }

    public void buildAndAddRequestToQueueWithParams(int method, String url, String body, Response.Listener responseListener, Response.ErrorListener errorListener, String auth, Map<String,String> params) {
        HttpRequestImpl request = new HttpRequestImpl(method, buildUrlWithParam(url, params), body, responseListener, errorListener, auth);
        requestQueueProvider.addToQueue(request);
    }

    public String buildUrlWithParam(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(url);
        for (String key: params.keySet()) {
            sb.append("?").append(key).append("=").append(params.get(key));
        }
        return sb.toString();
    }
}