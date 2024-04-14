package com.example.carmaintenanceapp;

public interface VolleyResponseListener {
    void onResponse(String response);

    void onError(String message);

    void onSuccessResponse(String string);

    void onErrorResponse(String string);
}
