package com.gp.abcpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

public class AdminViewQuestions extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private int chapterNum;
    private ArrayList<Exercise> list = new ArrayList<>();
    private AdminViewQuestionsAdapter adminViewQuestionsAdapter1, adminViewQuestionsAdapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_questions);

        recyclerView = (RecyclerView) findViewById(R.id.exReV);

        requestQueue = Volley.newRequestQueue(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chapterNum = Integer.parseInt(String.valueOf(extras.get("chapterNum")));
        }


        if (AdminQuizzes.x == 4) {
            setAdapter(1);
        } else {
            setAdapter(2);
        }

    }

    private void setAdapter(int i) {
        if (i == 1) {
            String url = "http://abcprogproject.000webhostapp.com/questions.php?chapter_num=" + chapterNum;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("questions");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String question = jsonObject.optString("question");
                            String ans1 = jsonObject.getString("answer1");
                            String ans2 = jsonObject.getString("answer2");
                            String ans3 = jsonObject.getString("answer3");
                            String ans4 = jsonObject.getString("answer4");
                            String correctAns = jsonObject.getString("correct_answer");

                            list.add(new Exercise(question,ans1,ans2,ans3, ans4, correctAns));
                        }
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AdminViewQuestions.this));
                        adminViewQuestionsAdapter1 = new AdminViewQuestionsAdapter(AdminViewQuestions.this, list);
                        recyclerView.setAdapter(adminViewQuestionsAdapter1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AdminViewQuestions.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("VOLLEY", "ERROR");
                }
            });
            requestQueue.add(jsonObjectRequest);
        } else {
            String url = "http://abcprogproject.000webhostapp.com/getEx.php?chapter_num=" + chapterNum;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("questions");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int lesson_num = jsonObject.getInt("lesson_num");
                            String question = jsonObject.optString("question");
                            String ans1 = jsonObject.getString("answer1");
                            String ans2 = jsonObject.getString("answer2");
                            String ans3 = jsonObject.getString("answer3");
                            String ans4 = jsonObject.getString("answer4");
                            String correctAns = jsonObject.getString("correct_answer");

                            list.add(new Exercise(lesson_num,question,ans1,ans2,ans3, ans4, correctAns));
                        }
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AdminViewQuestions.this));
                        adminViewQuestionsAdapter2 = new AdminViewQuestionsAdapter(AdminViewQuestions.this, list);
                        recyclerView.setAdapter(adminViewQuestionsAdapter2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AdminViewQuestions.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("VOLLEY", "ERROR");
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    }

    @Override
    public void onBackPressed() {
        list.clear();
        super.onBackPressed();
    }
}