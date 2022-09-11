package com.gp.abcpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminLessons extends AppCompatActivity {
CardView addLessons,deleteLessons,modifyLessons,viewLessons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lessons);

        deleteLessons=findViewById(R.id.deleteCard);


        addLessons=findViewById(R.id.lessonsCard);

        modifyLessons=findViewById(R.id.modCard);

        viewLessons=findViewById(R.id.viewCard);

    }

    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.lessonsCard:
                startActivity(new Intent(getApplicationContext(), AdminAddLesson.class));
                break;

            case R.id.deleteCard:
                startActivity(new Intent(getApplicationContext(), AdminDeleteLesson.class));
                break;

            case R.id.modCard:
                startActivity(new Intent(getApplicationContext(), AdminEditLesson.class));
                break;
            case R.id.viewCard:
                startActivity(new Intent(getApplicationContext(), AdminViewLessons.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}