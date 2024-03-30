package com.example.carmaintenanceapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ServiceFragment extends Fragment {

    private TextView textViewSelectDate;
    private EditText editTextNotes;
    private Spinner spinnerServiceType;

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
        List<String> serviceTypes = new ArrayList<>();
        serviceTypes.add(getString(R.string.spinner_hint)); // Hint-like item
        serviceTypes.add("Transmission");
        serviceTypes.add("Wheels");
        // Add other service types...

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, serviceTypes);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServiceType.setAdapter(spinnerArrayAdapter);

        // Initialize the TextView for date picking
        textViewSelectDate = view.findViewById(R.id.textViewSelectDate);
        textViewSelectDate.setOnClickListener(v -> showDatePickerDialog());

        // Initialize the EditText for notes
        editTextNotes = view.findViewById(R.id.editTextNotes);

        // Inflate the layout for this fragment
        return view;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d",
                            selectedYear, selectedMonth + 1, selectedDay);
                    textViewSelectDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}
