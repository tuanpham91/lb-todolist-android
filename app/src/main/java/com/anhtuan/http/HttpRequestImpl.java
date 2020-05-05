package com.anhtuan.http;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestImpl extends StringRequest {
    public HttpRequestImpl(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization","Basic dHVhbjoxOTkx");
        return headers;
    }


}
