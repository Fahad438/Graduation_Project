package com.gp.abcpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StartExercise extends AppCompatActivity {
    private QuestionsAnswersAdapter questionsAnswersAdapter;
    private ArrayList<Exercise> questionAnswersList;
    public static ArrayList<String> userAnswersList = new ArrayList<>();
    private ArrayList<Exercise> tempQA;

    private ArrayList<String> finalUserAnswersList = QuestionsAnswersAdapter.finalUserAnswersList;
    private ArrayList<Exercise> correctAnswersList;
    public static ArrayList<String> tempCorrectA;

    private AlertDialog.Builder builder;

    private RecyclerView recyclerView;
    private Button submitExercise;
    private TextView exWarning;

    private int lessonNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise);
        getWindow().setBackgroundDrawableResource(R.drawable.img);
        questionAnswersList = Exercises.selectedChQuAnsList;
        correctAnswersList = Exercises.selectedChCorrectAnsList;
        userAnswersList.clear();

        builder = new AlertDialog.Builder(this);


        recyclerView = (RecyclerView) findViewById(R.id.exRV);
        submitExercise = findViewById(R.id.submitExercise);
        exWarning = findViewById(R.id.exerciseWarning);

        submitExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalUserAnswersList.size() == tempQA.size()) {
                    calculateMark();
                } else {
                    exWarning.setText("answer all questions to submit");
                }
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lessonNum = (Integer) extras.get("lessonNum");
        }
        setAdapter();
    }

    private void setAdapter() {
        addQuestions();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionsAnswersAdapter = new QuestionsAnswersAdapter(this, tempQA);
        recyclerView.setAdapter(questionsAnswersAdapter);
    }

    private void addQuestions() {
        tempQA = new ArrayList<>();
        tempCorrectA = new ArrayList<>();
        int selectedLesson = lessonNum;
        for (int i = 0; i < questionAnswersList.size(); i++) {
            if (questionAnswersList.get(i).getLessonNum() == selectedLesson) {
                tempQA.add(questionAnswersList.get(i));
                tempCorrectA.add(correctAnswersList.get(i).getCorrectAns());
            }
        }
    }

    private void calculateMark() {
        int mark = 0;
        for (int i = 0; i < tempCorrectA.size(); i++) {
            if (finalUserAnswersList.contains(tempCorrectA.get(i)))
                mark++;
        }
        Intent intent = new Intent(StartExercise.this, MarkDisp.class);
        intent.putExtra("mark", mark);
        intent.putExtra("max", tempQA.size());
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        builder.setMessage("Are you sure you want to leave?").setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(StartExercise.this, Exercises.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }
}