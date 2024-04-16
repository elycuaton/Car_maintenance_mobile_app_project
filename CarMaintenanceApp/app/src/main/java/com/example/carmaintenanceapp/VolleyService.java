package com.example.carmaintenanceapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VolleyService {

    private static VolleyService instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private VolleyService(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyService getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyService(context);
        }
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public void postRequest(String url, JSONObject requestBody, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, successListener, errorListener);
        addToRequestQueue(jsonObjectRequest);
    }

    public void getRequest(String url, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, successListener, errorListener);
        addToRequestQueue(jsonArrayRequest);
    }

    public void getRequest(String url, VolleyCallback volleyCallback) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.length() == 0) {
                            // Handle empty response here
                            volleyCallback.onSuccessResponse(new JSONArray()); // Pass an empty JSON array
                        } else {
                            // Pass the response to the callback
                            volleyCallback.onSuccessResponse(response);
                        }
                    } catch (Exception e) {
                        // Log the error for debugging purposes
                        Log.e("VolleyService", "Error: " + e.getMessage());
                        // Pass the error message to the callback
                        volleyCallback.onErrorResponse("Error: " + e.getMessage());
                    }
                },
                error -> {
                    // Log the error for debugging purposes
                    Log.e("VolleyService", "Error: " + error.toString());
                    // Pass the error message to the callback
                    volleyCallback.onErrorResponse("Error: " + error.toString());
                });
        addToRequestQueue(jsonArrayRequest);
    }

//    public void getRequest(String url, com.example.carmaintenanceapp.VolleyCallback serviceType) {
//    }

    public interface VolleyCallback {
        void onSuccessResponse(JSONArray response);

        void onErrorResponse(String error);
    }
}
