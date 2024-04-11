package com.example.carmaintenanceapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button buttonSignUp = findViewById(R.id.btnSignUp);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonSignUp.setOnClickListener(v -> {
            if(editTextPassword.getText().toString().isBlank()){
                Toast.makeText(this, "Please fill out password.", Toast.LENGTH_SHORT).show();
            }
            else if(editTextName.getText().toString().isBlank()){
                Toast.makeText(this, "Please fill out name.", Toast.LENGTH_SHORT).show();
            }
            else if(editTextEmail.getText().toString().isBlank()
                    || !editTextEmail.getText().toString().contains("@")
                    || !editTextEmail.getText().toString().contains(".")){
                Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
            }
            else{
                sendPostApi();
            }

        });
    }
    private void sendPostApi(){
        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);

        String url = "http://10.0.2.2:5000/postUser";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle the response
                    Toast.makeText(this, "Sign up successful. Please sign in to your account.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    startActivity(intent);
                },
                error -> {
                    // Handle the error
                    Log.d(TAG, "onCreate: "+ error.getMessage());
                    Toast.makeText(this, "An error has occurred. Please try again.", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public byte[] getBody() {
                JSONObject body = new JSONObject();

                try {
                    body.put("name", editTextName.getText().toString());
                    body.put("password", editTextPassword.getText().toString());
                    body.put("email", editTextEmail.getText().toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                return body.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue.add(postRequest);
    }
}
