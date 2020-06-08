package com.anhtuan.http;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestImpl extends StringRequest {
    private String jsonBody;
    private String basicAuth = "";

    public HttpRequestImpl(int method, String url, String jsonBody, Response.Listener<String> listener, String basicAuth) {
        super(method, url, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });
        this.basicAuth = basicAuth;
        this.jsonBody = jsonBody;
    }

    public HttpRequestImpl(int method, String url, String jsonBody, Response.Listener<String> listener, Response.ErrorListener errListener, String basicAuth) {
        super(method, url, listener, errListener);
        this.basicAuth = basicAuth;
        this.jsonBody = jsonBody;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization","Basic "+ basicAuth);
        headers.put("Content-Type","application/json");
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return jsonBody == null ? null : jsonBody.getBytes("utf-8");
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody, "utf-8");
            return null;
        }
    }
}
