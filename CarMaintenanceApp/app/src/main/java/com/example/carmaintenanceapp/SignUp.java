package com.example.carmaintenanceapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;


public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Find the button by its ID
        Button buttonSignUp = findViewById(R.id.btnSignUp);

        // Set an OnClickListener for the button
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the HomePage activity
                Intent intent = new Intent(SignUp.this, HomePage.class);
                startActivity(intent);  // Start the HomePage activity
            }
        });
    }
}