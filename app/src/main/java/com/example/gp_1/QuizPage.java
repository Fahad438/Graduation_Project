package com.example.gp_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuizPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner chpterSpinner;
    ArrayList<String> chapterList = new ArrayList<>();
    ArrayAdapter<String> chapterAdapter;
    RequestQueue requestQueue;
    TextView textView;
    static String select;
    Button btn;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String url = "http://192.168.8.140/myprojct/droplist.php";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);
        chpterSpinner = findViewById(R.id.chapter);
        textView = (TextView) findViewById(R.id.name);
        btn=(Button)findViewById(R.id.startQuiz) ;
        builder = new AlertDialog.Builder(this);
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    JSONArray jsonArray = response.getJSONArray("chapter");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String chapterName = jsonObject.optString("chapterNumber");
                        chapterList.add(chapterName);
                        chapterAdapter = new ArrayAdapter<>(QuizPage.this, android.R.layout.simple_spinner_item, chapterList);
                        chapterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        chpterSpinner.setAdapter(chapterAdapter);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", "ERROR");
            }
        }

        );
        requestQueue.add(jsonObjectRequest);
        chpterSpinner.setOnItemSelectedListener(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Alert")
                        .setMessage("Do you want take quiz test?")
                        .setCancelable(true)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(QuizPage.this, QustionPage.class);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        })
                        .show();


            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.chapter) {
            select = adapterView.getSelectedItem().toString();
            textView.setText("Quiz " + select);

            //نكمل هنا بنسوي بوست لاسم الشبتر ونسحب اسئلة الخاصة بس بنفس الشبتر ايزي بوي
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}