package com.example.gp_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        BottomNavigationView bottomNavigationView = findViewById(R.id.btn_navg);
        bottomNavigationView.setSelectedItemId(R.id.homepahe);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homepahe:
                        return true;

                    case R.id.course:

                        startActivity(new Intent(getApplicationContext()
                                , CoursePage.class));
                        overridePendingTransition(0, 0);

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