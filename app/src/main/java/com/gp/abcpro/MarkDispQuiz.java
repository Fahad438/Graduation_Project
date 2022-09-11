package com.gp.abcpro;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MarkDispQuiz extends AppCompatActivity {
    private TextView markText;
    private Button markBtn;
    private int mark, maxMark ;
    private String chapter, id;
    private ProgressBar markProgressBar;
    private ObjectAnimator animator;
    private SharedPreferences shared_getData;
    private SharedPreferences.Editor editor;
    private static final String KEY_PREF_NAME = "userData";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_disp);

        getWindow().setBackgroundDrawableResource(R.drawable.img);
        shared_getData = getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
        id = (shared_getData.getString("id", ""));

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            mark = (Integer) extras.get("mark");
            maxMark = (Integer) extras.get("max");
            chapter = (String) extras.get("chapterNum");
        }

        SendMark();

        markProgressBar = findViewById(R.id.markProgressBar);
        markProgressBar.setMax(maxMark*1000);
        markText = findViewById(R.id.markText);
        markBtn = findViewById(R.id.markBtn);

        markText.setText("You got "+String.valueOf(mark)+" correct answers out of "+String.valueOf(maxMark));
        animator = ObjectAnimator.ofInt(markProgressBar, "progress", 0,mark*1000);
        animator.setDuration(2500).start();
        animator.setAutoCancel(true);
        animator.setInterpolator(new DecelerateInterpolator());
        markBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MarkDispQuiz.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed() {
        finish();
    }

    public void SendMark(){

        String data_url = "https://abcprogproject.000webhostapp.com/sendMark.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, data_url,
                response -> Toast.makeText(MarkDispQuiz.this, "Your mark has been saved successfully", Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(MarkDispQuiz.this, "Error: could not save you mark", Toast.LENGTH_SHORT).show()) {

            //Add parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();

                parms.put("Puser_id", id);
                parms.put("Pchapter_num", chapter);
                parms.put("Pmark", String.valueOf(mark));
                return parms;
            }
        };
        requestQueue = Volley.newRequestQueue(MarkDispQuiz.this);
        requestQueue.add(stringRequest);
    }
}
