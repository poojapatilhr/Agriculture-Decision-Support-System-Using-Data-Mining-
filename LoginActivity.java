package com.cropprediction;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 1;
    EditText etUser, etPass;
    TextView tvReg;
    Button btnLogin;
    boolean result = true;
    MySharedPreference sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        etUser = (EditText) findViewById(R.id.etName);
        etPass = (EditText) findViewById(R.id.etPassword);
        tvReg = (TextView) findViewById(R.id.tvSignUp);
        btnLogin = (Button) findViewById(R.id.btLogin);

        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {

                    loadData();
                }
            }
        });


        if (!LocationController.checkPermission(this)) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE},
                    PERMISSION_LOCATION_REQUEST_CODE);
        }


    }

    private boolean validateFields() {
        boolean result = true;

        if (!MyValidator.isValidEmail(etUser)) {
            result = false;
        }
        if (!MyValidator.isValidPassword(etPass)) {
            result = false;
        }

        return result;
    }

    private void init() {
        sharedPref = new MySharedPreference(this);
        //check user already login or not
        if (sharedPref.checkSharedPrefs("user_id")) {
            Intent intent = new Intent(this, DrawerActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void loadData() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Proccess", "Please Wait..", true);
        progressDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JSON_URL + "login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("mytag", "response" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("ack")) {
                        progressDialog.dismiss();
                        sharedPref.saveSharedPrefs(jsonObject.getString("id"), jsonObject.getString("name"), jsonObject.getString("email"), jsonObject.getString("mobile"));
                        Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("mytag", "error" + error);
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", etUser.getText().toString());
                params.put("password", etPass.getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        requestQueue.add(stringRequest);
    }

}
