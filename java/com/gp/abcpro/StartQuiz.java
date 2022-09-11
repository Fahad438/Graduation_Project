package com.gp.abcpro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class StartQuiz extends AppCompatActivity {
    private TextView question, nextWarning;
    private Button ans1, ans2, ans3, ans4, nextQuestion;
    private RequestQueue requestQueue;
    private String correctAnswer = "";
    private String selectedAns = "";
    private ArrayList<Integer> s = new ArrayList<>();
    private String chapter, url;
    private int z;
    private int i = (int) (Math.random() * 5);
    ;
    private int c = 0;
    private int mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);
        getWindow().setBackgroundDrawableResource(R.drawable.img);

        question = findViewById(R.id.quizQ);

        ans1 = findViewById(R.id.quizAns1);
        ans2 = findViewById(R.id.quizAns2);
        ans3 = findViewById(R.id.quizAns3);
        ans4 = findViewById(R.id.quizAns4);
        nextQuestion = findViewById(R.id.quizNextQ);
        nextWarning = findViewById(R.id.quizNextWarning);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chapter = (String) extras.get("chapterNum");
        }
        url = "https://abcprogproject.000webhostapp.com/questions.php?chapter_num=" + chapter;

        requestQueue = Volley.newRequestQueue(this);
        //builder = new AlertDialog.Builder(this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("questions");
                    z = 10;
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String q = jsonObject.getString("question");
                    String answer1 = jsonObject.getString("answer1");
                    String answer2 = jsonObject.getString("answer2");
                    String answer3 = jsonObject.getString("answer3");
                    String answer4 = jsonObject.getString("answer4");
                    correctAnswer = jsonObject.getString("correct_answer");

                    question.append(q);
                    ans1.setText(answer1);
                    ans2.setText(answer2);
                    ans3.setText(answer3);
                    ans4.setText(answer4);
                    if (c == z - 1) {
                        nextQuestion.setText("Submit");
                    } else {
                        nextQuestion.setText("Next");
                    }

                    s.add(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", "ERROR");
            }
        });

        requestQueue.add(jsonObjectRequest);

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAns.equals("")) {
                    nextWarning.setText("select your answer");
                } else {
                    nextWarning.setText("");
                    if (selectedAns.equals(correctAnswer)) {
                        mark = mark + 1;
                    }
                    selectedAns = "";
                    c++;
                    if (c <= z - 1) {
                        while (true) {
                            if (s.contains(i)) {
                                i = (int) (Math.random() * z);
                                if (!s.contains(i)) {
                                    question.setText("");
                                    ans1.setText("");
                                    ans2.setText("");
                                    ans3.setText("");
                                    ans4.setText("");

                                    requestQueue.add(jsonObjectRequest);
                                    break;
                                }
                            }
                        }
                    } else {
                        Intent intent = new Intent(StartQuiz.this, MarkDispQuiz.class);
                        intent.putExtra("mark", mark);
                        intent.putExtra("max", c);
                        intent.putExtra("chapterNum", String.valueOf(chapter));
                        startActivity(intent);
                        finish();
                    }
                    ans1.setBackgroundResource(R.drawable.answer_btn);
                    ans2.setBackgroundResource(R.drawable.answer_btn);
                    ans3.setBackgroundResource(R.drawable.answer_btn);
                    ans4.setBackgroundResource(R.drawable.answer_btn);
                }
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == ans1.getId()) {
            selectedAns = (String) ans1.getText();
            ans1.setBackgroundResource(R.drawable.selected_ans_btn);
            ans2.setBackgroundResource(R.drawable.answer_btn);
            ans3.setBackgroundResource(R.drawable.answer_btn);
            ans4.setBackgroundResource(R.drawable.answer_btn);
        } else if (view.getId() == ans2.getId()) {
            selectedAns = (String) ans2.getText();
            ans1.setBackgroundResource(R.drawable.answer_btn);
            ans2.setBackgroundResource(R.drawable.selected_ans_btn);
            ans3.setBackgroundResource(R.drawable.answer_btn);
            ans4.setBackgroundResource(R.drawable.answer_btn);
        } else if (view.getId() == ans3.getId()) {
            selectedAns = (String) ans3.getText();
            ans1.setBackgroundResource(R.drawable.answer_btn);
            ans2.setBackgroundResource(R.drawable.answer_btn);
            ans3.setBackgroundResource(R.drawable.selected_ans_btn);
            ans4.setBackgroundResource(R.drawable.answer_btn);
        } else {
            selectedAns = (String) ans4.getText();
            ans1.setBackgroundResource(R.drawable.answer_btn);
            ans2.setBackgroundResource(R.drawable.answer_btn);
            ans3.setBackgroundResource(R.drawable.answer_btn);
            ans4.setBackgroundResource(R.drawable.selected_ans_btn);
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Leaving mid quiz is not allowed, you need to complete solving all questions", Toast.LENGTH_SHORT).show();
    }
}