package com.example.carmaintenanceapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.rpc.context.AttributeContext;

import org.conscrypt.Conscrypt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.Security;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    TextView textViewSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textViewSignUp = findViewById(R.id.textViewSignUp);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        textViewSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUp.class);
            startActivity(intent);
        });
    }

    public void verifyUser(View view){
        if(editTextEmail.getText().toString().isBlank())
        {
            Toast.makeText(this, "Please enter a valid username.", Toast.LENGTH_SHORT).show();
        } else if (editTextPassword.getText().toString().isBlank()) {
            Toast.makeText(this, "Please enter a valid password.", Toast.LENGTH_SHORT).show();
        }
        else {
            Security.insertProviderAt(Conscrypt.newProvider(), 1);
            new FetchDataTask().execute("http://10.0.2.2:5000/Users");
        }
    }

    //The code below was from the API check code from class with some modification to fit behaviour of the page
    private class FetchDataTask extends AsyncTask<String, Void, String> {

        @Override
        //Required method for doing an Async Task.
        protected String doInBackground(String... urls) {
            //Use HttpURLConnection object for handling the connection.
            HttpURLConnection urlConnection = null;
            //Use BufferedReader to handle the information got online.
            BufferedReader reader = null;
            //Use String to hold the information.
            String data = null;

            try {
                //New url used for holding the URL address.
                URL url = new URL(urls[0]);
                //Join the URL with the urlConnection to open the connection.
                urlConnection = (HttpURLConnection) url.openConnection();
                //Set method for request, as per API calls.
                urlConnection.setRequestMethod("GET");
                //Connect.
                urlConnection.connect();

                //Once connected, our InputStream is used to read whatever is on the other side.
                InputStream inputStream = urlConnection.getInputStream();
                //From that, we use a StringBuilder to effectively build our data.
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) { //We declare some nulls along the way to inform the onPostExecute in case there is nothing to be read.
                    // Nothing to do.
                    return null;
                }
                //Instantiate our BufferedReader.
                reader = new BufferedReader(new InputStreamReader(inputStream));

                //Declare a line to hold information temporarily.
                String line;
                //Read each line and do something until we get a null line.
                while ((line = reader.readLine()) != null) {
                    //Append the line read into our Builder, alongside a line break.
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                //At the end, we throw in the data to our String.
                data = buffer.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                //Finally, we break our connections if they weren't null at first.
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        //And close our reader.
                        reader.close();
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return data;
        }


        //The following method will execute immediately after the previous task.
        @Override
        public void onPostExecute(String jsonData) {
            if (jsonData != null) {
                try {
                    //Create a json object to store the data.
                    JSONArray jsonArray = new JSONArray(jsonData);
                    //Verify if username and password is in the array
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        if(obj.getString("username").equals(editTextEmail.getText().toString()) && obj.getString("password").equals(editTextPassword.getText().toString())){

                            Intent intent = new Intent(MainActivity.this, HomePage.class);
                            startActivity(intent);
                        }
                        Toast.makeText(MainActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}