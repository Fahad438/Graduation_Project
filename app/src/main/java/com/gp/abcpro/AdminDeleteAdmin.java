package com.gp.abcpro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdminDeleteAdmin extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RequestQueue requestQueue;

    String url = "https://abcprogproject.000webhostapp.com/showAdmin.php";
    Button deleteBtn;
    Spinner spinner1;
    ArrayList<String> adminList = new ArrayList<String>();
    ArrayList<String> temp = new ArrayList<String>();
    ArrayAdapter<String> adminAdapter;
    private AlertDialog.Builder builder;
    String selectedAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_admin);
        builder = new AlertDialog.Builder(this);
        spinner1 = findViewById(R.id.selectAdmin);
        deleteBtn = findViewById(R.id.deleteBtnAdmin);

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("allAdmin");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject respons = jsonArray.getJSONObject(i);
                        String username = respons.getString("username");
                        adminList.add(username);
                    }
                    setAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminDeleteAdmin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");

            }
        });

        requestQueue.add(jsonObjectRequest);
        spinner1.setOnItemSelectedListener(this);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Are you sure you want to delete this admin's account?").setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String url = "https://abcprogproject.000webhostapp.com/deleteAdmin.php";
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                        response -> Toast.makeText(AdminDeleteAdmin.this, "Admin account deleted successfully", Toast.LENGTH_SHORT).show(),
                                        error -> Toast.makeText(AdminDeleteAdmin.this, "Error: Failed to delete account", Toast.LENGTH_SHORT).show()) {

                                    //Add parameters to the request
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> parms = new HashMap<>();
                                        parms.put("username", selectedAdmin);
                                        return parms;
                                    }
                                };
                                requestQueue = Volley.newRequestQueue(AdminDeleteAdmin.this);
                                requestQueue.add(stringRequest);
                                adminList.remove(selectedAdmin);
                                adminAdapter.clear();
                                temp.clear();
                                setAdapter();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        });

    }

    private void setAdapter() {
        for (int i = 0; i < adminList.size(); i++) {
            temp.add(adminList.get(i));
        }
        adminAdapter = new ArrayAdapter<String>(AdminDeleteAdmin.this, android.R.layout.simple_spinner_item, temp);
        adminAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adminAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedAdmin = String.valueOf(spinner1.getSelectedItem());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}