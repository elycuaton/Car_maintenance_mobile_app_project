package com.example.carmaintenanceapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;


public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button buttonSignUp = findViewById(R.id.btnSignUp);

        buttonSignUp.setOnClickListener(v -> {

            Intent intent = new Intent(SignUp.this, HomePage.class);
            startActivity(intent);
        });
    }
}