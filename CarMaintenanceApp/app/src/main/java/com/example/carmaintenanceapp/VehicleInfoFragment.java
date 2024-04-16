package com.example.carmaintenanceapp;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class VehicleInfoFragment extends Fragment {

    private EditText vehicleNameEditText;
    private EditText plateNumberEditText;
    private EditText modelEditText;
    private EditText yearEditText;
    private EditText vinEditText;
    private EditText mileageEditText;

    public VehicleInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_info, container, false);

        vehicleNameEditText = view.findViewById(R.id.vehicleName);
        plateNumberEditText = view.findViewById(R.id.plateNumber);
        modelEditText = view.findViewById(R.id.model);
        yearEditText = view.findViewById(R.id.year);
        vinEditText = view.findViewById(R.id.vin);
        mileageEditText = view.findViewById(R.id.mileage);
        Button submitButton = view.findViewById(R.id.submitVehicleInfo);

        submitButton.setOnClickListener(v -> {
            String vehicleName = vehicleNameEditText.getText().toString();
            String plateNumber = plateNumberEditText.getText().toString();
            String model = modelEditText.getText().toString();
            String year = yearEditText.getText().toString();
            String vin = vinEditText.getText().toString();
            String mileage = mileageEditText.getText().toString();

            JSONObject vehicleData = new JSONObject();
            try {
                vehicleData.put("VehicleName", vehicleName);
                vehicleData.put("PlateNumber", plateNumber);
                vehicleData.put("Model", model);
                vehicleData.put("Year", year);
                vehicleData.put("Vin", vin);
                vehicleData.put("Mileage", mileage);

                saveVehicleData(vehicleData);

                Toast.makeText(getActivity(), "Vehicle information saved!", Toast.LENGTH_SHORT).show();
                getActivity().finish();

            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Failed to create JSON data.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        return view;
    }



    private void saveVehicleData(JSONObject vehicleData) {
        FileOutputStream fos = null;
        try {
            String existingData = readVehicleDataFromFile();
            JSONArray vehiclesArray;
            if (!existingData.isEmpty()) {
                try {
                    vehiclesArray = new JSONArray(existingData);
                } catch (JSONException e) {
                    // If existingData is not a valid JSON array, start a new one
                    vehiclesArray = new JSONArray();
                }
            } else {
                vehiclesArray = new JSONArray(); // Start with a new array if no data exists
            }
            vehiclesArray.put(vehicleData);

            fos = getActivity().openFileOutput("vehicleInfo.json", Context.MODE_PRIVATE);
            fos.write(vehiclesArray.toString().getBytes());
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Failed to save vehicle information.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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

    private String readVehicleDataFromFile() {
        StringBuilder jsonData = new StringBuilder();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = getActivity().openFileInput("vehicleInfo.json");
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
}
