package com.example.carmaintenanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.Toast;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.widget.TextView;




public class HomePage extends AppCompatActivity {

    private RecyclerView recyclerViewCars;
    private VehicleAdapter vehicleAdapter;
    private ArrayList<JSONObject> vehicleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcar); // Replace with your layout file

        recyclerViewCars = findViewById(R.id.recyclerViewCars);
        recyclerViewCars.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fabAddCar = findViewById(R.id.fabAddCar);
        fabAddCar.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, Information.class);
            startActivity(intent);
        });

        loadVehicleData();
    }

    private void loadVehicleData() {
        vehicleList.clear();
        String jsonData = readVehicleDataFromFile();
        if (!jsonData.isEmpty()) {
            try {
                JSONArray vehiclesArray = new JSONArray(jsonData);
                for (int i = 0; i < vehiclesArray.length(); i++) {
                    JSONObject vehicleObject = vehiclesArray.getJSONObject(i);
                    vehicleList.add(vehicleObject);
                }
                vehicleAdapter = new VehicleAdapter(vehicleList, this);
                recyclerViewCars.setAdapter(vehicleAdapter);
            } catch (JSONException e) {
                Toast.makeText(this, "Failed to parse vehicle information.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No vehicle information found.", Toast.LENGTH_SHORT).show();
        }
    }

    private String readVehicleDataFromFile() {
        StringBuilder jsonData = new StringBuilder();
        try (FileInputStream fis = openFileInput("vehicleInfo.json");
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonData.append(line);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Failed to read vehicle information from file.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return jsonData.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVehicleData();
    }
}

class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private final ArrayList<JSONObject> vehicles;
    private final LayoutInflater inflater;
    private final Context context;

    public VehicleAdapter(ArrayList<JSONObject> vehicles, Context context) {
        this.vehicles = vehicles;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public VehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new VehicleViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(VehicleViewHolder holder, int position) {
        JSONObject vehicleObject = vehicles.get(position);
        holder.bind(vehicleObject);
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    static class VehicleViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final Context context;

        public VehicleViewHolder(View itemView, Context context) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
            this.context = context;
        }

        public void bind(JSONObject vehicleObject) {
            try {
                String vehicleName = vehicleObject.getString("VehicleName");
                textView.setText(vehicleName);

                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, VehicleDetailActivity.class);
                    intent.putExtra("vehicleName", vehicleName);
                    try {
                        intent.putExtra("plateNumber", vehicleObject.getString("PlateNumber"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        intent.putExtra("model", vehicleObject.getString("Model"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        intent.putExtra("year", vehicleObject.getString("Year"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    context.startActivity(intent);
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
