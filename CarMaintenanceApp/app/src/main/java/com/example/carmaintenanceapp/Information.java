package com.example.carmaintenanceapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.navigation_vehicle_info) {
                selectedFragment = new VehicleInfoFragment();
            } else if (id == R.id.navigation_services) {
                selectedFragment = new ServiceFragment();
            } else if (id == R.id.navigation_schedule) {
                selectedFragment = new ScheduleFragment();
            } else if (id == R.id.navigation_logout) {
                logout();// Handle logout
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_vehicle_info);
        }
    }

    //Logout Function
    private void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
