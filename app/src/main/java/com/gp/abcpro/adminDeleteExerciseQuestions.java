package com.gp.abcpro;

import androidx.appcompat.app.AppCompatActivity;

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

public class adminDeleteExerciseQuestions extends AppCompatActivity {
    private Spinner chaptersS, lessonsS, questionsS;
    private Button saveBtn;
    private TextView warning;
    private RequestQueue requestQueue;
    private String selectedLesson, selectedQuestion;

    private ArrayList<Integer> chaptersList = new ArrayList<Integer>();
    private ArrayAdapter<Integer> chaptersAdapter;
    private ArrayList<String> lessonsList = new ArrayList<String>();
    private ArrayAdapter<String> lessonsAdapter , questionsAdapter;
    private ArrayList<String> questionsList = new ArrayList<String>();
    private int selectedChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_exercise_questions);
        getWindow().setBackgroundDrawableResource(R.drawable.img);

        chaptersS = findViewById(R.id.chapterNum);
        lessonsS = findViewById(R.id.lessonTitle);
        questionsS = findViewById(R.id.questionTxt);
        warning = findViewById(R.id.warning);
        saveBtn = findViewById(R.id.saveBtn);

        requestQueue = Volley.newRequestQueue(this);

        String url = "https://abcprogproject.000webhostapp.com/chapters.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("allChapters");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int chapter_id = jsonObject.getInt("id");

                        chaptersList.add(chapter_id);
                    }
                    chaptersAdapter = new ArrayAdapter<Integer>(adminDeleteExerciseQuestions.this, android.R.layout.simple_spinner_item, chaptersList);
                    chaptersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    chaptersS.setAdapter(chaptersAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(adminDeleteExerciseQuestions.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        });
        requestQueue.add(jsonObjectRequest);
        chaptersS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedChapter = Integer.parseInt(parent.getSelectedItem().toString());
                getLessons(selectedChapter);
                questionsList.clear();
                lessonsList.clear();
                warning.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteQuestion();
                questionsAdapter.remove(selectedQuestion);
                if (questionsAdapter.isEmpty()){
                    warning.setText("No questions were found in this lesson, try to add questions from adding section");
                }
            }
        });
    }

    private void getLessons (int selectedChapter){
        String url = "https://abcprogproject.000webhostapp.com/adminLessons.php?chapter_num=" + selectedChapter;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("allLessons");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String lesson_num = jsonObject.getString("lesson_num");
                        String lesson_name = jsonObject.getString("lesson_name");

                        lessonsList.add(lesson_num + ". " + lesson_name);
                    }
                    if(lessonsList.isEmpty()) {
                        warning.setText("No lessons were found in this chapter, please add lessons and try again");
                    }
                    lessonsAdapter = new ArrayAdapter<String>(adminDeleteExerciseQuestions.this, android.R.layout.simple_spinner_item, lessonsList);
                    lessonsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lessonsS.setAdapter(lessonsAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(adminDeleteExerciseQuestions.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        });
        requestQueue.add(jsonObjectRequest);
        lessonsS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLesson = parent.getSelectedItem().toString();
                selectedLesson = selectedLesson.substring(0, 1);
                questionsList.clear();
                getExerciseQuestions(selectedLesson);
                warning.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getExerciseQuestions (String selectedLesson){
        String url = "http://abcprogproject.000webhostapp.com/getExercise.php?chapter_num=" + selectedChapter + "&lesson_num=" + selectedLesson;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("questions");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String question = jsonObject.optString("question");

                        questionsList.add(question);
                    }
                    if(questionsList.isEmpty()) {
                        warning.setText("No questions were found in this lesson, try to add questions from adding section");
                    }
                    questionsAdapter = new ArrayAdapter<String>(adminDeleteExerciseQuestions.this, android.R.layout.simple_spinner_item, questionsList);
                    questionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    questionsS.setAdapter(questionsAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(adminDeleteExerciseQuestions.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        });
        requestQueue.add(jsonObjectRequest);
        questionsS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedQuestion = parent.getSelectedItem().toString();
                Toast.makeText(adminDeleteExerciseQuestions.this,selectedQuestion , Toast.LENGTH_SHORT).show();
                warning.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void deleteQuestion() {
        String data_url = "https://abcprogproject.000webhostapp.com/delExQuestion.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, data_url,
                response -> Toast.makeText(adminDeleteExerciseQuestions.this, "Question deleted successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(adminDeleteExerciseQuestions.this, "Error: Failed to delete question", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("_chapter", String.valueOf(selectedChapter));
                params.put("_lesson", String.valueOf(selectedLesson));
                params.put("_question", selectedQuestion);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}