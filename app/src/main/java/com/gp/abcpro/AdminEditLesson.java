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

public class AdminEditLesson extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button btnEditLessons;
    RequestQueue requestQueue;
    static Integer selectedCh;
    Spinner spinner, spinner2;
    ArrayList<String> chaptersList = new ArrayList<String>();
    ArrayAdapter<String> chaptersAdapter;
    ArrayList<String> lessonsList = new ArrayList<String>();
    ArrayAdapter<String> lessonsAdapter;
    String lessonsName;
    EditText editLessonNum ,editContent,editTitle;
    TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_lessons);
        getWindow().setBackgroundDrawableResource(R.drawable.img);

        spinner = findViewById(R.id.chapterNum);
        spinner2 = findViewById(R.id.lessonsTitle);
        editContent=findViewById(R.id.editContent);
        editTitle=findViewById(R.id.editTitle);
        editLessonNum = findViewById(R.id.editTextLessonNum);
        btnEditLessons = findViewById(R.id.btnEditLessons);
        warning = findViewById(R.id.warning);

        btnEditLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTitle.getText().toString().equals("") || editContent.getText().toString().equals("") || editLessonNum.getText().toString().equals("") ) {
                    warning.setText( "You must fill all fields to proceed");
                } else if (editTitle.getText().length() < 4 || editContent.getText().length() <= 40) {
                    warning.setText("Lesson title is invalid");
                } else if (lessonsList.contains(editTitle.getText().toString())) {
                    warning.setText("A lesson with the same name already exists");
                } else {
                    editLesson();
                }
            }
        });
        GetChapterFromDB();
    }

    public void GetChapterFromDB() {
        String url = "https://abcprogproject.000webhostapp.com/chapters.php";
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

                        chaptersList.add("Chapter "+chapter_id);
                        chaptersAdapter = new ArrayAdapter<String>(AdminEditLesson.this, android.R.layout.simple_spinner_item, chaptersList);
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
                Toast.makeText(AdminEditLesson.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        });
        requestQueue.add(jsonObjectRequest);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.chapterNum) {
            lessonsList.clear();
            selectedCh = i+1;
            String url = "https://abcprogproject.000webhostapp.com/adminLessons.php?chapter_num=" + selectedCh;
            requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray jsonArray = response.getJSONArray("allLessons");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject respons = jsonArray.getJSONObject(i);
                            String lesson_name = respons.getString("lesson_name");

                            lessonsList.add(lesson_name);
                            lessonsAdapter = new ArrayAdapter<String>(AdminEditLesson.this, android.R.layout.simple_spinner_item, lessonsList);
                            lessonsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner2.setAdapter(lessonsAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AdminEditLesson.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("VOLLEY", "ERROR");
                }
            });
            requestQueue.add(jsonObjectRequest);

            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getId() == R.id.lessonsTitle) {
                        lessonsName = adapterView.getSelectedItem().toString();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void editLesson() {
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        Integer getLessonNum = Integer.parseInt(editLessonNum.getText().toString().trim());
        String getTitle = editTitle.getText().toString().trim().replaceAll(" +", " ");
        String getContent = editContent.getText().toString();
        String data_url = "https://abcprogproject.000webhostapp.com/modifyLessons.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, data_url,
                response -> Toast.makeText(AdminEditLesson.this, "Lesson edited successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(AdminEditLesson.this, "Error", Toast.LENGTH_SHORT).show()) {

                //Add parameters to the request
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parms = new HashMap<>();
                    parms.put("chapter", String.valueOf(selectedCh));
                    parms.put("newLessonNum", String.valueOf(getLessonNum));
                    parms.put("lessonTilte", lessonsName);
                    parms.put("newLessonTitle", getTitle);
                    parms.put("content", getContent);
                    return parms;
                }
            };
        requestQueue = Volley.newRequestQueue(AdminEditLesson.this);
        requestQueue.add(stringRequest);
        warning.setText("");
        editTitle.setText("");
        editContent.setText("");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}