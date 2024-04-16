package com.example.carmaintenanceapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileOutputStream;
import java.io.IOException;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class VehicleDetailActivity extends AppCompatActivity {

    private TextView vehicleNameTextView;
    private TextView plateNumberTextView;
    private TextView modelTextView;
    private TextView yearTextView;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        vehicleNameTextView = findViewById(R.id.tvVehicleName);
        plateNumberTextView = findViewById(R.id.tvVehiclePlateNumber);
        modelTextView = findViewById(R.id.tvVehicleModel);
        yearTextView = findViewById(R.id.tvVehicleYear);
        deleteButton = findViewById(R.id.btnDeleteVehicle);

        // Get the data from the intent
        Intent intent = getIntent();
        String vehicleName = intent.getStringExtra("vehicleName");
        String plateNumber = intent.getStringExtra("plateNumber");
        String model = intent.getStringExtra("model");
        String year = intent.getStringExtra("year");

        // Set the text in the TextViews
        vehicleNameTextView.setText(vehicleName != null ? vehicleName : "N/A");
        plateNumberTextView.setText(plateNumber != null ? plateNumber : "N/A");
        modelTextView.setText(model != null ? model : "N/A");
        yearTextView.setText(year != null ? year : "N/A");

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the name of the vehicle to be deleted
                String vehicleNameToDelete = vehicleNameTextView.getText().toString();
                // Call a method to delete the vehicle from storage
                deleteVehicleFromStorage(vehicleNameToDelete);
                // Finish the activity to return to the previous screen
                finish();
            }
        });

        Button backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This will simply finish the current activity and return to the previous activity in the stack
                finish();
            }
        });
    }

    // Method to delete the vehicle from storage
    private void deleteVehicleFromStorage(String vehicleName) {
        // Read the existing vehicle data from storage
        String jsonData = readVehicleDataFromFile();

        // Check if the data is not empty
        if (!jsonData.isEmpty()) {
            try {
                // Create a JSONArray from the existing data
                JSONArray vehiclesArray = new JSONArray(jsonData);

                // Iterate through the array to find and remove the vehicle with the matching name
                for (int i = 0; i < vehiclesArray.length(); i++) {
                    JSONObject vehicleObject = vehiclesArray.getJSONObject(i);
                    if (vehicleObject.getString("VehicleName").equals(vehicleName)) {
                        vehiclesArray.remove(i);
                        break; // Break out of the loop once the vehicle is removed
                    }
                }

                // Save the updated vehicle data back to storage
                saveVehicleData(vehiclesArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to save the updated vehicle data to storage
    private void saveVehicleData(JSONArray vehiclesArray) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("vehicleInfo.json", Context.MODE_PRIVATE);
            fos.write(vehiclesArray.toString().getBytes());
            // Display a toast message to indicate successful deletion
            showToast("Vehicle deleted successfully");
        } catch (IOException e) {
            e.printStackTrace();
            // Display a toast message to indicate deletion failure
            showToast("Failed to delete vehicle");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to read vehicle data from storage
    private String readVehicleDataFromFile() {
        StringBuilder jsonData = new StringBuilder();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = openFileInput("vehicleInfo.json");
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                jsonData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
                if (isr != null) isr.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonData.toString();
    }

    // Method to display a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
