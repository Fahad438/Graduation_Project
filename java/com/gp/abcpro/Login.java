package com.gp.abcpro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private RequestQueue requestQueue;

    private EditText username, password;
    private Button login;
    private ProgressBar loginPB;
    private TextView loginWarning, createAcc;
    private static final String URL = "https://abcprogproject.000webhostapp.com/login.php";
    private SharedPreferences shared_getData;
    private SharedPreferences.Editor editor;
    private static final String KEY_PREF_NAME = "userData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setBackgroundDrawableResource(R.drawable.img);


        username = findViewById(R.id.usernameLoginInput);
        password = findViewById(R.id.passLoginInput);
        loginPB = findViewById(R.id.loginBtnPB);
        login = findViewById(R.id.loginBtn1);
        loginWarning = findViewById(R.id.loginWarning);
        createAcc = findViewById(R.id.createAcc);

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        requestQueue = Volley.newRequestQueue(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUserInfo(username.getText().toString().trim(), password.getText().toString().trim());
                login(username.getText().toString().trim(), password.getText().toString().trim());
            }
        });
        AutoFill();
    }
    private void AutoFill() {
        shared_getData = getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);// اسم الملف الذي يحتوي المعلومات (KEY_PREF_NAME)
        username.setText(shared_getData.getString("username", "")); // طريقة استدعاء القيمة عن طريقة المفتاح
        password.setText(shared_getData.getString("password", ""));
    }

    private void login(String username, String password) {
        loginPB.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.optString("success");
                            if (success.equals("1")) {
                                loginSuccess(username, password);
                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                            } else {
                                loginWarning.setText("Account info are incorrect");
                                loginPB.setVisibility(View.GONE);
                                login.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            loginPB.setVisibility(View.GONE);
                            login.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginWarning.setText("Make sure your device is connected to internet");
                loginPB.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loginSuccess(String username, String password) {
        shared_getData = getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
        int isAdmin =  Integer.parseInt(shared_getData.getString("isAdmin", ""));
        openMainActivity(username, isAdmin);
    }


    void GetUserInfo(String username ,String password) {

        String accountInfoUrl = "https://abcprogproject.000webhostapp.com/getInfo.php?username=" + username;//سوي ال php
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, accountInfoUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            shared_getData = getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
                            editor = shared_getData.edit();
                            JSONArray jsonArray = response.getJSONArray("allaccounts");
                            JSONObject resp = jsonArray.getJSONObject(0);
                            if (resp.getString("username").equals(username) || resp.getString("email").equals(username) ) {
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.putString("id", resp.getString("id"));
                                editor.putString("_username", resp.getString("username"));
                                editor.putString("profile_picture", resp.getString("profile_picture"));
                                editor.putString("email", resp.getString("email"));
                                editor.putString("about", resp.getString("about"));
                                editor.putString("isAdmin", resp.getString("isAdmin"));
                                editor.apply();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> Log.e("VOLLEY", "ERROR"));
        requestQueue.add(jsonObjectRequest);
    }

    private void openMainActivity(String str, int isAdmin) {
        if (isAdmin == 0) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(Login.this, AdminHomePage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Login.this, PreActivity.class);
        startActivity(intent);
        finish();
    }
}