package com.gp.abcpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminLibrary extends AppCompatActivity {
    CardView add, edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_library);
        add = findViewById(R.id.addBook);
        edit = findViewById(R.id.editBook);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addBook:
                startActivity(new Intent(getApplicationContext(), AdminAddBook.class));
                break;

            case R.id.editBook:
                startActivity(new Intent(getApplicationContext(), AdminEditBook.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}