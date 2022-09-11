package com.gp.abcpro;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.HashMap;
import java.util.Map;

public class AdminDeleteQuizQuestion extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    RequestQueue requestQueue;
    String chapter = AdminSelectChapter.selectedCh;
    String url = "https://abcprogproject.000webhostapp.com/questions.php?chapter_num=" + chapter;
    Button deleteBtn;
    Spinner spinner;
    ArrayList<String> quistionList = new ArrayList<String>();
    ArrayAdapter<String> quistionAdapter;

    private AlertDialog.Builder builder;
    String selectedQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz_delete_ques);
        spinner = findViewById(R.id.selectQues);
        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(this);
        getQuestion();
        builder = new AlertDialog.Builder(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedQuestion = String.valueOf(spinner.getSelectedItem());
        Toast.makeText(this, selectedQuestion, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.deleteBtn) {
            String url = "https://abcprogproject.000webhostapp.com/deleteQuestion.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> Toast.makeText(AdminDeleteQuizQuestion.this, "Question deleted successfully", Toast.LENGTH_SHORT).show(),
                    error -> Toast.makeText(AdminDeleteQuizQuestion.this, "Error: failed to delete question", Toast.LENGTH_SHORT).show()) {

                //Add parameters to the request
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parms = new HashMap<>();

                    parms.put("Pquistion", selectedQuestion);

                    return parms;
                }
            };
            if (quistionList.isEmpty()) {
            } else {
                builder.setTitle("Alert")
                        .setMessage("Do you want take the quiz?")
                        .setCancelable(true)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestQueue = Volley.newRequestQueue(AdminDeleteQuizQuestion.this);
                                requestQueue.add(stringRequest);
                                quistionList.clear();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
                getQuestion();
            }
        }
    }

    public void getQuestion() {
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("questions");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject respons = jsonArray.getJSONObject(i);
                        String quistion = respons.getString("question");
                        quistionList.add(quistion);
                        quistionAdapter = new ArrayAdapter<String>(AdminDeleteQuizQuestion.this, android.R.layout.simple_spinner_item, quistionList);
                        quistionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(quistionAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminDeleteQuizQuestion.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        });
        requestQueue.add(jsonObjectRequest);
        spinner.setOnItemSelectedListener(this);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
