package com.example.gp_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoursePage extends AppCompatActivity {
    Button b1,b2,b3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);
        BottomNavigationView bottomNavigationView = findViewById(R.id.btn_navg);
        bottomNavigationView.setSelectedItemId(R.id.course);
        b1 = (Button) findViewById(R.id.lessons);
        b2 = (Button) findViewById(R.id.quiz);
        b3 = (Button) findViewById(R.id.Exercise);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoursePage.this, QuizPage.class);
                startActivity(intent);
            }
        });



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homepahe:
                        startActivity(new Intent(getApplicationContext(), HomePage.class));
                        overridePendingTransition(0, 0);
                        return true;


                    case R.id.course:

                        return true;
                    case R.id.profile:
                        return true;
                    case R.id.library:
                        return true;
                    case R.id.more:
                        return true;
                }
                return false;
            }
        });
    }
}