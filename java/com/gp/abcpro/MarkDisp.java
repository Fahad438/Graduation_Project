package com.gp.abcpro;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MarkDisp extends AppCompatActivity {
    private TextView markText;
    private Button markBtn;
    private int mark, maxMark;
    private ProgressBar markProgressBar;
    private ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_disp);
        getWindow().setBackgroundDrawableResource(R.drawable.img);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            mark = (Integer) extras.get("mark");
            maxMark = (Integer) extras.get("max");
        }

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
                Intent intent = new Intent(MarkDisp.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed() {
        finish();
    }

}