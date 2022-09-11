package com.example.gp_1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

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

public class QustionPage extends AppCompatActivity {
    RequestQueue requestQueue;
    Button ans1, ans2, ans3, ans4, next;
    TextView qustion, mark;
    String chapter = QuizPage.select;
    String url = "http://192.168.8.140/myprojct/Quiz.php?ChapterID=" + chapter;
    int i = (int) (Math.random() * 1);
    int z;
    int c = 0;
    String TrueAnswer = "";
    String selectAns = "";
    static int markQuiz;
    int count;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qustion_page);
        ArrayList<Integer> s = new ArrayList<>();
        ans1 = (Button) findViewById(R.id.buttonAns1);
        ans2 = (Button) findViewById(R.id.buttonAns2);
        ans3 = (Button) findViewById(R.id.buttonAns3);
        ans4 = (Button) findViewById(R.id.buttonAns4);
        qustion = (TextView) findViewById(R.id.textViewQ2);
        mark = (TextView) findViewById(R.id.showmark);
        next = (Button) findViewById(R.id.NextQus);

        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    JSONArray jsonArray = response.getJSONArray("quiz");

                    z = jsonArray.length();


                    JSONObject respons = jsonArray.getJSONObject(i);

                    String question = respons.getString("Qustion");
                    String answer1 = respons.getString("Answer1");
                    String answer2 = respons.getString("Answer2");
                    String answer3 = respons.getString("Answer3");
                    String answer4 = respons.getString("Answer4");
                    TrueAnswer = respons.getString("TrueAnswer");

                    qustion.append(question);
                    ans1.setText(answer1);
                    ans2.setText(answer2);
                    ans3.setText(answer3);
                    ans4.setText(answer4);

                    next.setText("Next");

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
        }

        );

        requestQueue.add(jsonObjectRequest);
        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectAns = (String) ans1.getText();

                ans1.setBackgroundResource(R.drawable.btn_selected_ans);
                ans2.setBackgroundResource(R.drawable.btn_answer);
                ans3.setBackgroundResource(R.drawable.btn_answer);
                ans4.setBackgroundResource(R.drawable.btn_answer);


            }
        });

        ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectAns = (String) ans2.getText();
                ans1.setBackgroundResource(R.drawable.btn_answer);
                ans2.setBackgroundResource(R.drawable.btn_selected_ans);
                ans3.setBackgroundResource(R.drawable.btn_answer);
                ans4.setBackgroundResource(R.drawable.btn_answer);

            }
        });

        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectAns = (String) ans3.getText();
                ans1.setBackgroundResource(R.drawable.btn_answer);
                ans2.setBackgroundResource(R.drawable.btn_answer);
                ans3.setBackgroundResource(R.drawable.btn_selected_ans);
                ans4.setBackgroundResource(R.drawable.btn_answer);

            }
        });

        ans4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectAns = (String) ans4.getText();
                ans1.setBackgroundResource(R.drawable.btn_answer);
                ans2.setBackgroundResource(R.drawable.btn_answer);
                ans3.setBackgroundResource(R.drawable.btn_answer);
                ans4.setBackgroundResource(R.drawable.btn_selected_ans);

            }

        });
        //==================================================================================
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectAns.equals(TrueAnswer)) {
                    count = count + 1;
                } else {
                    count = count + 0;
                }
                mark.setText(String.valueOf(count));


                c++;
                if (c <= 9) {
                    //for (y = 0; y < 1000; y++)
                    while (true) {

                        if (s.contains(i)) {
                            i = (int) (Math.random() * z);

                            if (!s.contains(i)) {

                                qustion.setText("");
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
                    Intent intent = new Intent(QustionPage.this, QuizMarkPage.class);
                    startActivity(intent);


                }
                ans1.setBackgroundResource(R.drawable.btn_answer);
                ans2.setBackgroundResource(R.drawable.btn_answer);
                ans3.setBackgroundResource(R.drawable.btn_answer);
                ans4.setBackgroundResource(R.drawable.btn_answer);
                markQuiz=count;
            }
        });
    }




}