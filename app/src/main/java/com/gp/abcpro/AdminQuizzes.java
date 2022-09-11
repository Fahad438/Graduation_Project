package com.gp.abcpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminQuizzes extends AppCompatActivity {
    CardView add, delete, modify, view;
    static int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz);

        add = findViewById(R.id.viewQuiz);
        delete = findViewById(R.id.deleteQuiz);
        modify = findViewById(R.id.modifyQuiz);
        view = findViewById(R.id.viewQuiz);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addQuiz:
                startActivity(new Intent(getApplicationContext(), AdminSelectChapter.class));
                x=1;
                break;

            case R.id.deleteQuiz:
                startActivity(new Intent(getApplicationContext(), AdminSelectChapter.class));
                x=2;
                break;

            case R.id.modifyQuiz:
                startActivity(new Intent(getApplicationContext(), AdminSelectChapter.class));
                x=3;
                break;

            case R.id.viewQuiz:
                startActivity(new Intent(getApplicationContext(), AdminSelectChapter.class));
                x=4;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}