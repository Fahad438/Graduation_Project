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

public class AdminEditQuizQuestion extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RequestQueue requestQueue;
    String chapter = AdminSelectChapter.selectedCh;
    String url = "https://abcprogproject.000webhostapp.com/questions.php?chapter_num=" + chapter;
    Button modifyBtn;
    Spinner spinner404;
    ArrayList<String> quistionList = new ArrayList<String>();
    ArrayAdapter<String> quistionAdapter;
    String selectedQuestion;
    EditText newQuestion;
    EditText ans1;
    EditText ans2;
    EditText ans3;
    EditText ans4;
    EditText correct_answer;
    TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz_modify_ques);
        getWindow().setBackgroundDrawableResource(R.drawable.img);

        spinner404 = findViewById(R.id.spinner404);
        newQuestion = findViewById(R.id.newQuestion);
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);
        correct_answer = findViewById(R.id.correct_answer);
        modifyBtn = findViewById(R.id.modifyBtn);
        warning = findViewById(R.id.warning);

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newQuestion.getText().toString().equals("")|| ans1.getText().toString().equals("")|| ans2.getText().toString().equals("")|| ans3.getText().toString().equals("")|| ans4.getText().toString().equals("")|| correct_answer.getText().toString().equals("")) {
                    warning.setText("You must fill all fields to proceed");
                } else if (newQuestion.getText().length() < 10 ){
                    warning.setText("Question is too short");
                }else if(quistionList.contains(newQuestion.getText().toString())){
                    warning.setText("Error: duplicated question");
                }else {
                    updateQuestion();
                }
            }
        });
        getQuestion();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedQuestion = String.valueOf(spinner404.getSelectedItem());
        Toast.makeText(this, selectedQuestion, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void updateQuestion() {
        String getQuestion = newQuestion.getText().toString().trim().replaceAll(" +", " ");
        String getA1 = ans1.getText().toString();
        String getA2 = ans2.getText().toString();
        String getA3 = ans3.getText().toString();
        String getA4 = ans4.getText().toString();
        String getCorrect_answer = correct_answer.getText().toString();

        String data_url = "https://abcprogproject.000webhostapp.com/modifyQuestion.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, data_url,
                response -> Toast.makeText(AdminEditQuizQuestion.this, "Question edited successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(AdminEditQuizQuestion.this, "Error: failed to edit question", Toast.LENGTH_SHORT).show()) {

            //Add parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();

                parms.put("Poldquistion", selectedQuestion);
                parms.put("question", getQuestion);
                parms.put("answer1", getA1);
                parms.put("answer2", getA2);
                parms.put("answer3", getA3);
                parms.put("answer4", getA4);
                parms.put("correct_answer", getCorrect_answer);
                return parms;
            }
        };
        requestQueue = Volley.newRequestQueue(AdminEditQuizQuestion.this);
        requestQueue.add(stringRequest);
        quistionList.clear();
        quistionAdapter.clear();
        getQuestion();
        newQuestion.setText("");
        ans1.setText("");
        ans2.setText("");
        ans3.setText("");
        ans4.setText("");
        correct_answer.setText("");
    }

    public void getQuestion() {
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
                        quistionAdapter = new ArrayAdapter<String>(AdminEditQuizQuestion.this, android.R.layout.simple_spinner_item, quistionList);
                        quistionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner404.setAdapter(quistionAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminEditQuizQuestion.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        });
        requestQueue.add(jsonObjectRequest);
        spinner404.setOnItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
