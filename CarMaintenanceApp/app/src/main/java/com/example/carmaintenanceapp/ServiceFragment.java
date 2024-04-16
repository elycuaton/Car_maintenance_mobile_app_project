package com.example.carmaintenanceapp;
import com.example.carmaintenanceapp.data.VolleyService;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ServiceFragment extends Fragment {

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
        Spinner spinnerServiceType = view.findViewById(R.id.spinnerServiceType);
        List<String> serviceTypes = new ArrayList<>();

        // Fetch service types from MongoDB
        fetchServiceTypesFromMongoDB(serviceTypes, spinnerServiceType);

        //Service Maintenance List
        serviceTypes.add(getString(R.string.spinner_hint)); // Hint-like item
        serviceTypes.add("Change Motor Oil");
        serviceTypes.add("Change Transmission Fluid");
        serviceTypes.add("Change Power Steering Fluid");
        serviceTypes.add("Change Gear Oil");
        serviceTypes.add("Brake Flush");
        serviceTypes.add("Coolant Flush");
        serviceTypes.add("Replace Sparkplugs");
        serviceTypes.add("Tire Rotation");

        // Set up the adapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                requireActivity(), android.R.layout.simple_spinner_item, serviceTypes);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServiceType.setAdapter(spinnerArrayAdapter);

        // Handle spinner item selection
        spinnerServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected service type
                String selectedServiceType = parent.getItemAtPosition(position).toString();
                // Perform any action based on the selected service type if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected (optional)
            }
        });

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

            // Check if the date is empty
            if (date.isEmpty()) {
                // Show a toast message indicating that the date is required
                Toast.makeText(requireContext(), "Please select a date.", Toast.LENGTH_SHORT).show();
            } else {
                // Call the method to save service data to the server
                saveServiceDataToServer(serviceType, date, notes);
            }
        });

        return view;
    }

    private void fetchServiceTypesFromMongoDB(List<String> serviceTypes, Spinner spinnerServiceType) {
        VolleyService.getInstance(requireContext()).getRequest("http://10.0.2.2:5000/getServiceTypes",
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        serviceTypes.clear(); // Clear existing items
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String serviceType = jsonObject.getString("serviceType");
                            serviceTypes.add(serviceType); // Add fetched service type
                        }
                        // Notify the adapter of the data set change
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(requireContext(),
                                android.R.layout.simple_spinner_item, serviceTypes);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerServiceType.setAdapter(spinnerArrayAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error

                });
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
}