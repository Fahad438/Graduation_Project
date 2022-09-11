package com.example.gp_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizMarkPage extends AppCompatActivity {
    TextView show;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_mark_page);
        int Mar = QustionPage.markQuiz;
        int a=0;

        show = (TextView) findViewById(R.id.ShowMarkQ);
        btn = (Button) findViewById(R.id.GoToHomePageFromQuiz);

        show.setText(String.valueOf(Mar + "/10"));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizMarkPage.this, HomePage.class);
                startActivity(intent);


            }
        });
    }
}