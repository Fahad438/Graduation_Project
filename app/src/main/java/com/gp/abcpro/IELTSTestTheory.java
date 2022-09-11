package com.gp.abcpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class IELTSTestTheory extends AppCompatActivity {
    TextView question, questionNum;
    Button a1, a2, a3, a4;
    ArrayList<IELTSQustion> quizArrayList;
    Random random;
    int questionsAtt = 1, currentPos;
    static int currentScore = 0;
    private ArrayList<Integer> s = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ielts_test_listening_qus);
        question = findViewById(R.id.question);
        questionNum = findViewById(R.id.questionNum);
        a1 = findViewById(R.id.idBtnOption1);
        a2 = findViewById(R.id.idBtnOption2);
        a3 = findViewById(R.id.idBtnOption3);
        a4 = findViewById(R.id.idBtnOption4);
        quizArrayList = new ArrayList<>();
        random = new Random();
        getQuizQuestion(quizArrayList);
        currentPos = random.nextInt(quizArrayList.size());
        s.add(currentPos);
        setDataToViews(currentPos);
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quizArrayList.get(currentPos).getAnswer().trim().equalsIgnoreCase(a1.getText().toString().trim())) {
                    currentScore++;
                }
                questionsAtt++;
                currentPos = random.nextInt(quizArrayList.size());
                setDataToViews(currentPos);
            }
        });

        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quizArrayList.get(currentPos).getAnswer().trim().equalsIgnoreCase(a2.getText().toString().trim())) {
                    currentScore++;
                }
                questionsAtt++;
                currentPos = random.nextInt(quizArrayList.size());
                setDataToViews(currentPos);
            }
        });


        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quizArrayList.get(currentPos).getAnswer().trim().equalsIgnoreCase(a3.getText().toString().trim())) {
                    currentScore++;
                }
                questionsAtt++;
                currentPos = random.nextInt(quizArrayList.size());
                setDataToViews(currentPos);
            }
        });

        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quizArrayList.get(currentPos).getAnswer().trim().equalsIgnoreCase(a4.getText().toString().trim())) {
                    currentScore++;
                }
                questionsAtt++;
                currentPos = random.nextInt(quizArrayList.size());
                setDataToViews(currentPos);
            }
        });

    }


    private void setDataToViews(int currentPos) {
        if (questionsAtt == 11) {
            Intent intent = new Intent(IELTSTestTheory.this, IELTSMarkDisp.class);
            intent.putExtra("max", 20);
            startActivity(intent);
            finish();
        } else {
            while (true) {
                currentPos = random.nextInt(quizArrayList.size());
                if (!s.contains(currentPos)) {
                    questionNum.setText("Question: " + questionsAtt + "/10");
                    question.setText(quizArrayList.get(currentPos).getQuestion());
                    a1.setText(quizArrayList.get(currentPos).getOption1());
                    a2.setText(quizArrayList.get(currentPos).getOption2());
                    a3.setText(quizArrayList.get(currentPos).getOption3());
                    a4.setText(quizArrayList.get(currentPos).getOption4());
                    s.add(currentPos);
                    break;
                }
            }
        }
    }

    private void getQuizQuestion(ArrayList<IELTSQustion> quizArrayList) {
        quizArrayList.add(new IELTSQustion("Express train leaves at __", "a.12:00 pm ", "9:30 am", "9:30 pm", "9:13 am", "9:30 am"));
        quizArrayList.add(new IELTSQustion("Nearest station is __", "Helensvale", "Helensale", "Helendale", "Helengale", "Helendale"));
        quizArrayList.add(new IELTSQustion("Number 706 bus goes to __", "Helendale Station", "The Train Station", "Central Street", "Bayswater Shopping Centre", "Central Street"));
        quizArrayList.add(new IELTSQustion("Number __ bus goes to station", "792", "706", "702", "729", "792"));
        quizArrayList.add(new IELTSQustion("Earlier bus leaves at __", "8:55 pm", "9:05 am", "9:05 pm", "8:55 am", "8:55 am"));
        quizArrayList.add(new IELTSQustion("how much cash fare for the bus", "$1.80 ", "$1.50", "$1.18", "$1.88", "$1.80 "));
        quizArrayList.add(new IELTSQustion("What are the off-peak hours? ", "before 5 am or after 7:30 am", "before 5 pm or after 7:30 pm", "before 5 pm or after 7:30 am", "before 5 am or after 7:30 pm", "before 5 pm or after 7:30 pm"));
        quizArrayList.add(new IELTSQustion("how much card fare for the train at off-peak hours?", "$7.15 ", "$1.50", "$10", "$3.55", "$7.15"));
        quizArrayList.add(new IELTSQustion("how much will I have to pay if I used the ferry?", "$4.50 in cash fare and $3.55 in card fare", "$20  in cash fare and $3.55 in card fare", "$4.50 in cash fare and $3.15 in card fare", "$4.15 in cash fare and $3.55 in card fare", "$4.50 in cash fare and $3.55 in card fare"));
        quizArrayList.add(new IELTSQustion("how much card fare for the train at off-peak hours?", "$7.15 ", "$1.50", "$10", "$3.55", "$7.15"));
        quizArrayList.add(new IELTSQustion("how much will I have to pay if I used the ferry?", "$4.50 in cash fare and $3.55 in card fare", "$20  in cash fare and $3.55 in card fare", "$4.50 in cash fare and $3.15 in card fare", "$4.15 in cash fare and $3.55 in card fare", "$4.50 in cash fare and $3.55 in card fare"));
        quizArrayList.add(new IELTSQustion("what is the cost for a tour using the tourist ferries for the whole day?", "$3.55", "$20", "$35", "$65", "$65"));
        quizArrayList.add(new IELTSQustion("what is the cost for a tour using the tourist ferries for the whole day?", "$3.55", "$20", "$35", "$65", "$65"));
        quizArrayList.add(new IELTSQustion("Number 706 bus goes to __", "Helendale Station", "The Train Station", "Central Street", "Bayswater Shopping Centre", "Central Street"));
        quizArrayList.add(new IELTSQustion("Number __ bus goes to station", "792", "706", "702", "729", "792"));
        quizArrayList.add(new IELTSQustion("Earlier bus leaves at __", "8:55 pm", "9:05 am", "9:05 pm", "8:55 am", "8:55 am"));
    }
    @Override
    public void onBackPressed() {

    }
}