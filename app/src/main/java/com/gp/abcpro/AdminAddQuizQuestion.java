package com.gp.abcpro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AdminAddQuizQuestion extends AppCompatActivity {
    EditText quistion, ans1, ans2, ans3, ans4, correct_answer;
    TextView warning;
    Button addBtn;
    RequestQueue requestQueue;
    ArrayList<String> quistionList = new ArrayList<String>();
    String chapter = AdminSelectChapter.selectedCh;
    String url = "https://abcprogproject.000webhostapp.com/questions.php?chapter_num=" + chapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz_add_ques);
        getWindow().setBackgroundDrawableResource(R.drawable.img);

        quistion = findViewById(R.id.quistion);
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);
        correct_answer = findViewById(R.id.correct_answer);
        warning = findViewById(R.id.warning);


        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quistion.getText().toString().equals("")|| ans1.getText().toString().equals("")|| ans2.getText().toString().equals("")|| ans3.getText().toString().equals("")|| ans4.getText().toString().equals("")|| correct_answer.getText().toString().equals("")) {
                    warning.setText("You must fill all fields to proceed");
                } else if (quistion.getText().length() < 10 ){
                    warning.setText("Question is too short");
                }else if(quistionList.contains(quistion.getText().toString())){
                    warning.setText("Error: duplicated question");
                }else {
                    addQuestion();
                }
            }
        });
        getQuestion();
    }


    public void addQuestion() {
        String getQuestion = quistion.getText().toString().trim().replaceAll(" +", " ");
        String getA1 = ans1.getText().toString();
        String getA2 = ans2.getText().toString();
        String getA3 = ans3.getText().toString();
        String getA4 = ans4.getText().toString();
        String getCorrect_answer = correct_answer.getText().toString();

        String data_url = "https://abcprogproject.000webhostapp.com/addQuestion.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, data_url,
                response -> Toast.makeText(AdminAddQuizQuestion.this, "Question added successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(AdminAddQuizQuestion.this, "Error: Failed to add question", Toast.LENGTH_SHORT).show()) {

            //Add parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("Pchapter", AdminSelectChapter.selectedCh);
                parms.put("Pquistion", getQuestion);
                parms.put("Pans1", getA1);
                parms.put("Pans2", getA2);
                parms.put("Pans3", getA3);
                parms.put("Pans4", getA4);
                parms.put("Pcorrect_answer", getCorrect_answer);
                return parms;
            }
        };
        requestQueue = Volley.newRequestQueue(AdminAddQuizQuestion.this);
        requestQueue.add(stringRequest);
        warning.setText("");
        quistion.setText("");
        ans1.setText("");
        ans2.setText("");
        ans3.setText("");
        ans4.setText("");
        correct_answer.setText("");
        quistionList.clear();
        getQuestion();//نستدعي الدالة بعد نفضي الاري عشان لما يضيف نفس القيمه مره ثانية يجيه ايرور
        }

    public void getQuestion(){
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("questions");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject respons = jsonArray.getJSONObject(i);
                        int QuistionID = respons.getInt("QuestionID");
                        String quistion = respons.getString("question");

                        quistionList.add(quistion);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminAddQuizQuestion.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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