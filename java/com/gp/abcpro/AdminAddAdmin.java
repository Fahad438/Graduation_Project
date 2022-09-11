package com.gp.abcpro;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminAddAdmin extends AppCompatActivity {
    TextView warning;
    EditText editUsername, editEmail, editPass, editConPassl;
    Button btnAddAdmin;
    RequestQueue requestQueue;
    String txtUsername, txtEmail, txtPassword, txtConfirmPassword;
    ArrayList<String> adminList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_admin);
        warning = findViewById(R.id.warning);
        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editPass = findViewById(R.id.editPass);
        editConPassl = findViewById(R.id.editConPass);
        btnAddAdmin = findViewById(R.id.btnAddAccount);
        requestQueue = Volley.newRequestQueue(this);

        btnAddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warning.setText("");
                addAdmin();
            }
        });
    }


    public void addAdmin() {
        String getUser = editUsername.getText().toString().trim().replaceAll(" +", " ");
        String getEmail = editEmail.getText().toString().trim().replaceAll(" +", " ");
        String getPass = editPass.getText().toString().trim().replaceAll(" +", " ");
        String getConPass = editConPassl.getText().toString().trim().replaceAll(" +", " ");

        if (validate()) {
            String data_url = "https://abcprogproject.000webhostapp.com/register.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, data_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.optString("success");

                                if (success.equals("1")) {
                                    Toast.makeText(AdminAddAdmin.this, "Account registered successfully", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(AdminAddAdmin.this, success, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(AdminAddAdmin.this, "Failed to register!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AdminAddAdmin.this, "Failed to register!", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", getUser);
                    params.put("email", getEmail);
                    params.put("password", getPass);
                    params.put("isAdmin", "1");
                    return params;
                }
            };
            requestQueue.add(stringRequest);
            editUsername.setText("");
            editEmail.setText("");
            editPass.setText("");
            editConPassl.setText("");
            adminList.clear();
        } else {
            warning.setText("Some fields doesn't meet the requirements");
        }
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
        txtUsername = editUsername.getText().toString().trim();
        txtEmail = editEmail.getText().toString().trim();
        txtPassword = editPass.getText().toString().trim();
        txtConfirmPassword = editConPassl.getText().toString().trim();

        if (isEmpty(txtUsername)) {
            editUsername.setError("Username field is mandatory");
            isValid = false;
        }

        if (isEmpty(txtEmail)) {
            editEmail.setError("Email field is mandatory");
            isValid = false;
        } else {
            if (!isEmailValid(txtEmail)) {
                editEmail.setError("Invalid email address");
                isValid = false;
            }
        }

        if (isEmpty(txtPassword)) {
            editPass.setError("Password field is mandatory");
            isValid = false;
        } else {
            if (!isPasswordValid(txtPassword)) {
                editPass.setError("Password must contain the following:\n" +
                        "- at least one digit.\n" +
                        "- at least one lowercase Letter.\n" +
                        "- at least one uppercase Letter.\n" +
                        "- at least one special character like ! @ # & ( ).\n" +
                        "- a length of at least 8 characters.");
                isValid = false;
            }
        }

        if (isEmpty(txtConfirmPassword)) {
            editConPassl.setError("Confirm password field is mandatory");
            isValid = false;
        } else {
            if (!isConfirmCorrect(txtPassword, txtConfirmPassword)) {
                editConPassl.setError("Passwords do not match");
                isValid = false;
            }
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}


