package com.anhtuan.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import java.util.LinkedList;

public class RequestQueueProvider {
    private static RequestQueue requestQueue;

    private LinkedList<Request> impendingRequests;
    private static RequestQueueProvider queueProvider;
    private Context context;

    private RequestQueueProvider (Context ctx) {
        this.context = ctx;
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
            impendingRequests = new LinkedList<>();
        }
    }
    public static RequestQueueProvider getRequestQueueProvider(Context ctx) {
        if (queueProvider == null) {
            queueProvider = new RequestQueueProvider(ctx);
        }
        return queueProvider;
    }

    public void addToQueue(HttpRequestImpl request) {
        impendingRequests.add(request);
        if (isNetworkAvailable()) {
            while (isNetworkAvailable() && !impendingRequests.isEmpty()) {
                Log.d("Late Request Service", "Sending Delayed Requests : " + request);
                Request nextRequest = impendingRequests.pop();
                requestQueue.add(nextRequest);
            }
        } else {
            Log.d("Late Request Service", "No Internet! Adding Request to Queue : " + request);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
