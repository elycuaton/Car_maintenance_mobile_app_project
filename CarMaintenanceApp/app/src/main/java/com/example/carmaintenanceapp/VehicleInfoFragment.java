package com.example.carmaintenanceapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import org.json.JSONObject;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_info, container, false);

        vehicleNameEditText = view.findViewById(R.id.vehicleName);
        plateNumberEditText = view.findViewById(R.id.plateNumber);
        modelEditText = view.findViewById(R.id.model);
        yearEditText = view.findViewById(R.id.year);
        vinEditText = view.findViewById(R.id.vin);
        mileageEditText = view.findViewById(R.id.mileage);
        Button submitButton = view.findViewById(R.id.submitVehicleInfo);

        submitButton.setOnClickListener(v -> {
            // Gather data from the EditTexts
            String vehicleName = vehicleNameEditText.getText().toString();
            String plateNumber = plateNumberEditText.getText().toString();
            String model = modelEditText.getText().toString();
            long year = Long.parseLong(yearEditText.getText().toString()); // Assuming user enters a valid number
            String vin = vinEditText.getText().toString();
            long mileage = Long.parseLong(mileageEditText.getText().toString()); // Assuming user enters a valid number

            // Create a JSONObject to store the data
            JSONObject vehicleData = new JSONObject();
            try {
                vehicleData.put("VehicleName", vehicleName);
                vehicleData.put("PlateNumber", plateNumber);
                vehicleData.put("Model", model);
                vehicleData.put("Year", year);
                vehicleData.put("Vin", vin);
                vehicleData.put("Mileage", mileage);

                // Here, you can convert vehicleData to string and store it or send it to your backend, etc.
                // For example: vehicleData.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return view;
    }
}
