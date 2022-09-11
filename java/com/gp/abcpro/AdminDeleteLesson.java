package com.gp.abcpro;

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

public class AdminDeleteLesson extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button deleteLessonsButton;
    RequestQueue requestQueue;
    static String selectedCh;
    Spinner spinner, spinner2;
    ArrayList<Integer> chaptersList = new ArrayList<Integer>();
    ArrayAdapter<Integer> chaptersAdapter;
    ArrayList<String> lessonsList = new ArrayList<String>();
    ArrayAdapter<String> lessonsAdapter;
    String lessonsName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_lessons);
        spinner = findViewById(R.id.chapterNum);
        spinner2 = findViewById(R.id.lessonsTitle);
        deleteLessonsButton = findViewById(R.id.btnDeleteLessons);
        deleteLessonsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lessonsList.isEmpty()) {
                    Toast.makeText(AdminDeleteLesson.this, "The selected chapter doesn't contain any lesson yet", Toast.LENGTH_SHORT).show();
                } else {
                    deleteQuestion();
                }
            }
        });
        GetChapterFromDB();
    }

    //نسحب الشباتر من الداتا بيس
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

                        chaptersList.add(chapter_id);
                        chaptersAdapter = new ArrayAdapter<Integer>(AdminDeleteLesson.this, android.R.layout.simple_spinner_item, chaptersList);
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
                Toast.makeText(AdminDeleteLesson.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");

            }
        }
        );
        requestQueue.add(jsonObjectRequest);
        spinner.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.chapterNum) {
            //نسوي كلير للاراي عشان نضيف القيم الجديده للشابتر الختار من المستخدم
            upDateLesssons();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void deleteQuestion() {
        String url = "https://abcprogproject.000webhostapp.com/deleteLessons.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(AdminDeleteLesson.this, "Lesson deleted successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(AdminDeleteLesson.this, "Error, could not delete lesson", Toast.LENGTH_SHORT).show()) {

            //Add parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("lesson_name", lessonsName);

                return parms;
            }
        };
        requestQueue = Volley.newRequestQueue(AdminDeleteLesson.this);
        requestQueue.add(stringRequest);
        upDateLesssons();
        spinner2.setAdapter(lessonsAdapter);
    }


    public void upDateLesssons() {
        lessonsList.clear();
        selectedCh = String.valueOf((Integer) spinner.getSelectedItem());

        String url = "https://abcprogproject.000webhostapp.com/adminLessons.php?chapter_num=" + selectedCh;
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("allLessons");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject respons = jsonArray.getJSONObject(i);
                        String lesson_name = respons.getString("lesson_name");

                        lessonsList.add(lesson_name);
                        lessonsAdapter = new ArrayAdapter<String>(AdminDeleteLesson.this, android.R.layout.simple_spinner_item, lessonsList);
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
                Toast.makeText(AdminDeleteLesson.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
