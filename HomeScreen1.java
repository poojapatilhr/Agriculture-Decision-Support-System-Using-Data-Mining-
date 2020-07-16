package com.cropprediction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeScreen1 extends AppCompatActivity {

    EditText etSoil, etHumidity, etTemp, etBuffer, etWeather, etWater, etPotasium;
    Button btSign, btnClear;
    MySharedPreference mySharedPreference;
    Spinner spCity;
    Button btnGo;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen1);
        mySharedPreference = new MySharedPreference(this);
        init();

    }

    private void init() {
        etSoil = (EditText) findViewById(R.id.etSoil);
        etHumidity = (EditText) findViewById(R.id.etHumidity);
        etTemp = (EditText) findViewById(R.id.etTemp);

        spCity = (Spinner) findViewById(R.id.spCity);
        btnGo = (Button) findViewById(R.id.btnGo);

        etBuffer = (EditText) findViewById(R.id.etBuffer);
        //  etWeather = (EditText) findViewById(R.id.etWeather);
        etWater = (EditText) findViewById(R.id.etWater);
        etPotasium = (EditText) findViewById(R.id.etPotasium);
       /* etSodium = (EditText) findViewById(R.id.etSodium);

        etAcidity = (EditText) findViewById(R.id.etAcidity);*/

        btSign = (Button) findViewById(R.id.btSign);
        btnClear = (Button) findViewById(R.id.btnClear);


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeScreen1.class);
                startActivity(intent);
                finish();
            }
        });

        btSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (validateFields()) {
                    Intent intent = new Intent(HomeScreen1.this, ShowCrupActivity.class);
                    intent.putExtra("temp", etTemp.getText().toString());
                    intent.putExtra("humidity", etHumidity.getText().toString());
                    intent.putExtra("soil", etSoil.getText().toString());
                    //  intent.putExtra("weather", etWeather.getText().toString());

                    intent.putExtra("water_consumption", etWater.getText().toString());

                    intent.putExtra("potasium", etPotasium.getText().toString());

                    //intent.putExtra("acidity", etAcidity.getText().toString());

                    intent.putExtra("buffer", etBuffer.getText().toString());

                    //intent.putExtra("sodium", etSodium.getText().toString());

                    startActivity(intent);
                }

            }
        });


        loadCity();


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendGo();

            }
        });
    }

    private void sendGo() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", "Please Wait..", true);
        progressDialog.setCancelable(true);

        arrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JSON_URL + "loadparam.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("list", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                etHumidity.setText(jsonObject.getString("humidity"));
                                etSoil.setText(jsonObject.getString("soil_moisture"));
                                etTemp.setText(jsonObject.getString("temp"));
                                etBuffer.setText(jsonObject.getString("buffer"));
                                etWater.setText(jsonObject.getString("water"));
                                etPotasium.setText(jsonObject.getString("potasium"));
                            }
                            if (jsonArray.length() == 0) {
                                Toast.makeText(getApplicationContext(), "No data Available", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("list", "" + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("city", spCity.getSelectedItem().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    private void loadCity() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", "Please Wait..", true);
        progressDialog.setCancelable(true);

        arrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JSON_URL + "loadcity.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("list", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                arrayList.add(jsonObject.getString("name"));
                            }
                            if (jsonArray.length() == 0) {
                                Toast.makeText(getApplicationContext(), "No data Available", Toast.LENGTH_LONG).show();
                            }

                            spCity.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.tv, arrayList));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("list", "" + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            mySharedPreference.clearSharedPrefs();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean validateFields() {
        boolean result = true;
      /*  if (!MyValidator.isValidField(etAcidity)) {
            result = false;
        }*/
        if (!MyValidator.isValidField(etBuffer)) {
            result = false;
        }
        if (!MyValidator.isValidField(etHumidity)) {
            result = false;
        }
        if (!MyValidator.isValidField(etPotasium)) {
            result = false;
        }
      /*  if (!MyValidator.isValidField(etSodium)) {
            result = false;
        }*/
        if (!MyValidator.isValidField(etSoil)) {
            result = false;
        }
        if (!MyValidator.isValidField(etTemp)) {
            result = false;
        }
        if (!MyValidator.isValidField(etWater)) {
            result = false;
        }

        return result;
    }
}

