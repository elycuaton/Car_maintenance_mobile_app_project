package com.example.carmaintenanceapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScheduleFragment extends Fragment {
    private TextView textViewEventList;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        textViewEventList = view.findViewById(R.id.textViewEventList);

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            fetchServiceData(selectedDate);
        });

        return view;
    }

    private void fetchServiceData(String date) {
        // Modify the date format to match "yyyy-MM-dd"
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        String formattedDate;
        try {
            Date parsedDate = inputFormat.parse(date);
            formattedDate = outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        String url = "http://10.0.2.2:5000/getServices?date=" + formattedDate;

        VolleyService.getInstance(getContext()).getRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONArray response) {
                try {
                    StringBuilder events = new StringBuilder();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String serviceType = jsonObject.getString("serviceType");
                        String notes = jsonObject.getString("notes");
                        String event = serviceType + " - " + formattedDate + " - " + notes;
                        events.append(event).append("\n");
                    }
                    textViewEventList.setText(events.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(String error) {
                // Handle error response
            }
        });
    }

}
