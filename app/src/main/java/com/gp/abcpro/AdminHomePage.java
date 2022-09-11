package com.gp.abcpro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminHomePage extends AppCompatActivity {
    CardView courseCard, libraryCard,accountCard;
    boolean doubleBackToExitPressedOnce = false;
    Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        courseCard=findViewById(R.id.courseCard);
        libraryCard=findViewById(R.id.libraryCard);
        accountCard=findViewById(R.id.accountCard);
    }

    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.courseCard:
                startActivity(new Intent(getApplicationContext(), AdminCourse.class));
                break;

            case R.id.libraryCard:
                startActivity(new Intent(getApplicationContext(), AdminLibrary.class));
                break;

            case R.id.accountCard:
                startActivity(new Intent(getApplicationContext(), AccountSettings.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            toast.cancel();
            super.onBackPressed();
        }
        this.doubleBackToExitPressedOnce = true;
        toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}