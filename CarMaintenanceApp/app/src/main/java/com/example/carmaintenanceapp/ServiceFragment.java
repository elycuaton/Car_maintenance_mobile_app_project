package com.example.carmaintenanceapp;

import static com.example.carmaintenanceapp.R.id.spinnerServiceType;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.carmaintenanceapp.data.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ServiceFragment extends Fragment {

    private Spinner spinnerServiceType;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private TextView textViewSelectDate;
    private EditText editTextNotes;

    public ServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        // Initialize the Spinner with service types
        spinnerServiceType = view.findViewById(R.id.spinnerServiceType);
        List<String> serviceList = new ArrayList<>();
        serviceList.add(getString(R.string.spinner_hint)); // Hint-like item

        // Create the ArrayAdapter
        spinnerArrayAdapter = new ArrayAdapter<>(
                requireActivity(), android.R.layout.simple_spinner_item, serviceList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServiceType.setAdapter(spinnerArrayAdapter);

        // Fetch service types from MongoDB and update the spinner adapter
        fetchServiceTypes();

        // Initialize the TextView for date picking
        textViewSelectDate = view.findViewById(R.id.textViewSelectDate);
        textViewSelectDate.setOnClickListener(v -> showDatePickerDialog());

        // Initialize the EditText for notes
        editTextNotes = view.findViewById(R.id.editTextNotes);

        // Initialize the Save button and set click listener
        Button buttonSave = view.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> {
            // Get the selected service type, date, and notes
            String serviceType = spinnerServiceType.getSelectedItem().toString();
            String date = textViewSelectDate.getText().toString();
            String notes = editTextNotes.getText().toString();

            // Call the method to save service data to the server
            saveServiceDataToServer(serviceType, date, notes);
        });

        Button buttonAddService = view.findViewById(R.id.buttonAddService);
        EditText editTextAddServices = view.findViewById(R.id.editTextAddServices);
        buttonAddService.setOnClickListener(v -> {
            String serviceType = editTextAddServices.getText().toString().trim();
            if (!serviceType.isEmpty()) {
                addServiceToList(serviceType);
            } else {
                Toast.makeText(requireContext(), "Please enter a service type.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d",
                            selectedYear, selectedMonth + 1, selectedDay);
                    textViewSelectDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveServiceDataToServer(String serviceType, String date, String notes) {
        // Prepare the request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("serviceType", serviceType);
            requestBody.put("date", date);
            requestBody.put("notes", notes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make the API call
        VolleyService.getInstance(requireContext()).postRequest("http://10.0.2.2:5000/postService", requestBody,
                response -> {
                    // Handle success response
                    Toast.makeText(requireContext(), "Service data saved successfully.", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle error
                    Toast.makeText(requireContext(), "Failed to save service data. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

    private void addServiceToList(String serviceType) {
        // Prepare the request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("serviceType", serviceType);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Make the API call
        VolleyService.getInstance(requireContext()).postRequest("http://10.0.2.2:5000/addServiceToList", requestBody,
                response -> {
                    // Handle success response
                    Toast.makeText(requireContext(), "Service added to list successfully.", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle error
                    Toast.makeText(requireContext(), "Service added to list successfully.", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchServiceTypes() {
        // Make the API call
        VolleyService.getInstance(requireContext()).getRequest("http://10.0.2.2:5000/getServiceTypes",
                new VolleyService.VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            List<String> serviceTypes = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String serviceType = jsonObject.getString("serviceType"); // Corrected field name
                                serviceTypes.add(serviceType);
                            }
                            // Update the spinner adapter with fetched service types
                            spinnerArrayAdapter.clear();
                            spinnerArrayAdapter.addAll(serviceTypes);
                            spinnerArrayAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(String error) {
                        // Handle error
                        Toast.makeText(requireContext(), "Failed to fetch service types. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
