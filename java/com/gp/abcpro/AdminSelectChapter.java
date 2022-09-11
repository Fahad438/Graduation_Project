package com.gp.abcpro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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

public class AdminSelectChapter extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private RequestQueue requestQueue;
    private String url = "https://abcprogproject.000webhostapp.com/chapters.php";
    static String selectedCh;
    Spinner spinner;
    ArrayList<String> chaptersList = new ArrayList<String>();
    ArrayAdapter<String> chaptersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz_select_chapter);
        spinner = findViewById(R.id.spinner1);
        findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (AdminQuizzes.x == 1) {
                    startActivity(new Intent(AdminSelectChapter.this, AdminAddQuizQuestion.class));
                } else if (AdminQuizzes.x == 2) {
                    startActivity(new Intent(AdminSelectChapter.this, AdminDeleteQuizQuestion.class));
                } else if (AdminQuizzes.x == 3) {
                    startActivity(new Intent(AdminSelectChapter.this, AdminEditQuizQuestion.class));
                } else {
                    Intent intent = new Intent(AdminSelectChapter.this, AdminViewQuestions.class);
                    intent.putExtra("chapterNum",String.valueOf(selectedCh).substring(selectedCh.length()-1));
                    startActivity(intent);
                }
            }
        });

        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("allChapters");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject respons = jsonArray.getJSONObject(i);
                        int chapter_id = respons.getInt("id");

                        chaptersList.add("Chapter " +chapter_id);
                        chaptersAdapter = new ArrayAdapter<String>(AdminSelectChapter.this, android.R.layout.simple_spinner_item, chaptersList);
                        chaptersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(chaptersAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminSelectChapter.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
        spinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedCh = String.valueOf(i+1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}