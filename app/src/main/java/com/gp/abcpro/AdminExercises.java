package com.gp.abcpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminExercises extends AppCompatActivity {
    CardView addEx,delEx,editEx,viewEx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_exercises);

        delEx=findViewById(R.id.delEx);
        addEx=findViewById(R.id.addEx);
        editEx=findViewById(R.id.editEx);
        viewEx=findViewById(R.id.viewEx);
    }

    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.addEx:
                startActivity(new Intent(getApplicationContext(), AdminAddExerciseQuestions.class));
                break;

            case R.id.delEx:
                startActivity(new Intent(getApplicationContext(), adminDeleteExerciseQuestions.class));
                break;

            case R.id.editEx:
                startActivity(new Intent(getApplicationContext(), AdminEditExerciseQuestion.class));
                break;
            case R.id.viewEx:
                startActivity(new Intent(getApplicationContext(), AdminSelectChapter.class));
                AdminQuizzes.x = 5;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}