package com.gp.abcpro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.HashSet;
import java.util.Set;

public class AdminViewLessons extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RequestQueue requestQueue;
    Spinner spinnerCh;
    ArrayList<String> chapterList = new ArrayList<>();
    ArrayAdapter<String> chapterAdapter;
    ArrayList<Lesson> lessonArray = new ArrayList<Lesson>();
    ArrayList<Lesson> temp;
    LessonsAdapter lessonsAdapter;
    LessonsAdapter.RecyclerViewClickListener listener;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_lessons);
        String url = "https://abcprogproject.000webhostapp.com/lessons.php";

        spinnerCh = findViewById(R.id.chapterNum);
        recyclerView = findViewById(R.id.lessonRV);
        temp = new ArrayList<Lesson>();

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("lessons");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int chapter_num = jsonObject.optInt("chapter_num");
                        int lesson_num = jsonObject.optInt("lesson_num");
                        String lesson_name = jsonObject.optString("lesson_name");
                        String lessonURL = jsonObject.optString("content");


                        if (!chapterList.contains("Chapter " + chapter_num))
                            chapterList.add("Chapter " +chapter_num);
                        chapterAdapter = new ArrayAdapter<>(AdminViewLessons.this, android.R.layout.simple_spinner_item, chapterList);
                        chapterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCh.setAdapter(chapterAdapter);
                        lessonArray.add(new Lesson(chapter_num, lesson_num, lesson_name, lessonURL));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminViewLessons.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        });

        requestQueue.add(jsonObjectRequest);
        spinnerCh.setOnItemSelectedListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAdapter(int selected) {
        temp.clear();
        for (int i = 0; i < lessonArray.size(); i++) {
            if (lessonArray.get(i).getChapterNum() == (selected+1)) {
                temp.add(lessonArray.get(i));
            }
        }

        Set<Object> set = new HashSet<>();
        temp.removeIf(e -> !set.add(e.getLessonNum()));

        setOnClickListener();
        lessonsAdapter = new LessonsAdapter(this, temp, listener);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(lessonsAdapter);
    }


    private void setOnClickListener() {
        listener = new LessonsAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), StartLesson.class);
                intent.putExtra("lessonName", temp.get(position).getLessonName().toString());
                intent.putExtra("lessonURL", temp.get(position).getLessonURL().toString());
                startActivity(intent);
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.chapterNum) {
            setAdapter(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}