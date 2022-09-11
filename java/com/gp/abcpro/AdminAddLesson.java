package com.gp.abcpro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class AdminAddLesson extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RequestQueue requestQueue;
    String url = "https://abcprogproject.000webhostapp.com/chapters.php";

    TextView warning;
    Spinner spinner;
    ArrayList<Integer> chaptersList = new ArrayList<Integer>();
    ArrayAdapter<Integer> chaptersAdapter;
    int selectedCh;
    EditText lessonNum, lessonTitle, content;
    Button addLessons;
    ArrayList<String> lessonsList = new ArrayList<String>();
    String urll = "https://abcprogproject.000webhostapp.com/adminLessons.php?chapter_num=" + selectedCh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_lessons);
        getWindow().setBackgroundDrawableResource(R.drawable.img);

        spinner = findViewById(R.id.chapterNum);
        lessonTitle = findViewById(R.id.editTextTextLessonTitle);
        content = findViewById(R.id.editTextTextContent);
        lessonNum = findViewById(R.id.editTextLessonNum);
        warning = findViewById(R.id.warning);

        addLessons = findViewById(R.id.btnAddLessons);
        addLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lessonTitle.getText().toString().equals("") || content.getText().toString().equals("") || lessonNum.getText().toString().equals("")) {
                    warning.setText("You must fill all fields to proceed");
                } else if (lessonTitle.getText().length() < 4 || content.getText().length() <= 40) {
                    warning.setText("Invalid lesson title or content link");
                } else if (lessonsList.contains(lessonTitle.getText())) {
                    warning.setText("Lesson already exists");
                } else {
                    addLesson();
                }
            }
        });
        GetChapter();
        getLessons();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedCh = i+1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void GetChapter() {
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

                        chaptersList.add(chapter_id);
                        chaptersAdapter = new ArrayAdapter<Integer>(AdminAddLesson.this, android.R.layout.simple_spinner_item, chaptersList);
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
                Toast.makeText(AdminAddLesson.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        });
        requestQueue.add(jsonObjectRequest);
        spinner.setOnItemSelectedListener(this);
    }

    public void addLesson() {
        Integer getLessonNum = Integer.parseInt(lessonNum.getText().toString().trim());
        String getLessonTitle = lessonTitle.getText().toString().trim();
        String getContent = content.getText().toString().trim();
        String data_url = "https://abcprogproject.000webhostapp.com/addLessons.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, data_url,
                response -> Toast.makeText(AdminAddLesson.this, "Lesson added successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(AdminAddLesson.this, "Error: failed to add lesson", Toast.LENGTH_SHORT).show()) {

                //Add parameters to the request
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parms = new HashMap<>();
                    parms.put("chapter_num", String.valueOf(selectedCh));
                    parms.put("lesson_num", String.valueOf(getLessonNum));
                    parms.put("lesson_name", getLessonTitle);
                    parms.put("content", getContent);
                    return parms;
                }
            };
        requestQueue = Volley.newRequestQueue(AdminAddLesson.this);
        requestQueue.add(stringRequest);
        warning.setText("");
        lessonNum.setText("");
        lessonTitle.setText("");
        content.setText("");
    }

    public void getLessons() {
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urll,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("allLessons");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject respons = jsonArray.getJSONObject(i);
                        String lesson_name = respons.getString("lesson_name");
                        lessonsList.add(lesson_name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminAddLesson.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
