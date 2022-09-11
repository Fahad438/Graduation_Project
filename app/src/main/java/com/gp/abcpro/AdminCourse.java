package com.gp.abcpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminCourse extends AppCompatActivity {

    CardView addChapter, lessons,quiz,exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course);
        addChapter = findViewById(R.id.chapterCard);
        lessons = findViewById(R.id.lessonsCard);
        quiz=findViewById(R.id.quizCard);
        exercises = findViewById(R.id.exercisesCard);

    }

    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.chapterCard:
                startActivity(new Intent(getApplicationContext(), AdminAddChapter.class));
                break;

            case R.id.exercisesCard:
                startActivity(new Intent(getApplicationContext(), AdminExercises.class));
                break;

            case R.id.lessonsCard:
                startActivity(new Intent(getApplicationContext(), AdminLessons.class));
                break;

            case R.id.quizCard:
                startActivity(new Intent(getApplicationContext(), AdminQuizzes.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}