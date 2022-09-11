package com.example.gp_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MarkPlacement extends AppCompatActivity {
    TextView show;
    Button btn;
    public void onBackPressed() {//نوقف زر الباك بس هنا
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_placement);
        int Mar = PlacementTest.mark;
        show = (TextView) findViewById(R.id.ShowMarkP);
        btn=(Button) findViewById(R.id.GoToHomePageFromTest);

        show.setText(String.valueOf(Mar+"/10"));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MarkPlacement.this, HomePage.class);
                startActivity(intent);
            }
        });
    }
}