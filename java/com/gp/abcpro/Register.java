package com.gp.abcpro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private Button register;
    private EditText username, email, password, confirmPassword;
    private TextView registerWarning, haveAcc;
    private ProgressBar registerPB;
    private AlertDialog.Builder builder;
    private RequestQueue requestQueue;
    private static String url = "https://abcprogproject.000webhostapp.com/register.php";
    private String uname;
    private SharedPreferences shared_getData;
    private SharedPreferences.Editor editor;
    private static final String KEY_PREF_NAME = "userData";
    String txtUsername;
    String txtEmail;
    String txtPassword;
    String txtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setBackgroundDrawableResource(R.drawable.img);

        registerWarning = findViewById(R.id.registerWarning);
        username = findViewById(R.id.usernameRegInput);
        email = findViewById(R.id.emailRegInput);
        password = findViewById(R.id.passwordRegInput);
        confirmPassword = findViewById(R.id.confirmPassRegInput);
        register = findViewById(R.id.registerBtn1);
        registerPB = findViewById(R.id.registerBtnPB);
        haveAcc = findViewById(R.id.alreadyHaveAcc);


        builder = new AlertDialog.Builder(this);
        requestQueue = Volley.newRequestQueue(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    registerWarning.setText("");
                    Register();
                } else {
                    registerWarning.setText("Some fields doesn't meet the requirements");
                }
            }
        });

        haveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean isEmpty(String text) {
        CharSequence str = text;
        return TextUtils.isEmpty(str);
    }

    private boolean isEmailValid(String text) {
        CharSequence email = text;
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isPasswordValid(String text) {
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–_[{}]:;',?/*~$^+=<>]).{8,20}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    private boolean isConfirmCorrect(String pass, String confirmPass) {
        return (pass.equals(confirmPass));
    }

    private Boolean validate() {
        Boolean isValid = true;
        txtUsername = username.getText().toString().trim();
        txtEmail = email.getText().toString().trim();
        txtPassword = password.getText().toString().trim();
        txtConfirmPassword = confirmPassword.getText().toString().trim();

        if (isEmpty(txtUsername)) {
            username.setError("Username field is mandatory");
            isValid = false;
        }

        if (isEmpty(txtEmail)) {
            email.setError("Email field is mandatory");
            isValid = false;
        } else {
            if (!isEmailValid(txtEmail)) {
                email.setError("Invalid email address");
                isValid = false;
            }
        }

        if (isEmpty(txtPassword)) {
            password.setError("Password field is mandatory");
            isValid = false;
        } else {
            if (!isPasswordValid(txtPassword)) {
                password.setError("Password must contain the following:\n" +
                        "- at least one digit.\n" +
                        "- at least one lowercase Letter.\n" +
                        "- at least one uppercase Letter.\n" +
                        "- at least one special character like ! @ # & ( ).\n" +
                        "- a length of at least 8 characters.");
                isValid = false;
            }
        }

        if (isEmpty(txtConfirmPassword)) {
            confirmPassword.setError("Confirm password field is mandatory");
            isValid = false;
        } else {
            if (!isConfirmCorrect(txtPassword, txtConfirmPassword)) {
                confirmPassword.setError("Passwords do not match");
                isValid = false;
            }
        }
        return isValid;
    }

    @SuppressLint("NotConstructor")
    private void Register() {
        registerPB.setVisibility(View.VISIBLE);
        register.setVisibility(View.GONE);

        final String username = this.username.getText().toString().trim();
        uname = username;
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.optString("success");

                            if (success.equals("1")) {
                                Toast.makeText(Register.this, "Account registered successfully", Toast.LENGTH_SHORT).show();
                                placementTest();
                                getUserInfo(username,password);
                            } else {
                                Toast.makeText(Register.this, success, Toast.LENGTH_SHORT).show();
                                clearFields();
                                registerPB.setVisibility(View.GONE);
                                register.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Register.this, "Failed to register!", Toast.LENGTH_SHORT).show();
                            registerPB.setVisibility(View.GONE);
                            register.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, "Failed to register!", Toast.LENGTH_SHORT).show();
                registerPB.setVisibility(View.GONE);
                register.setVisibility(View.VISIBLE);
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("isAdmin","0");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void placementTest() {
        builder.setMessage("Would you like to take the placement test?").setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        openPlacementTest();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openMainActivity(uname);
            }
        }).show();
    }

    private void openMainActivity(String str) {
        Intent intent = new Intent(Register.this, MainActivity.class);
        intent.putExtra("username", str);
        startActivity(intent);
        finish();
    }

    private void openPlacementTest() {
        Intent intent = new Intent(Register.this, PlacementTest.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        builder.setMessage("Are you sure you want to leave?").setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Register.this, PreActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    public void clearFields() {
        username.setText("");
        email.setText("");
        password.setText("");
        confirmPassword.setText("");
    }

    void getUserInfo(String username ,String password) {

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
}