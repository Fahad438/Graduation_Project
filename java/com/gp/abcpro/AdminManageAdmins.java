package com.gp.abcpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminManageAdmins extends AppCompatActivity {
Button addAdmin,deleteAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_account);
        addAdmin=findViewById(R.id.addAccount);
        deleteAdmin=findViewById(R.id.deleteAccount);
        addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminAddAdmin.class));
            }
        });
        deleteAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminDeleteAdmin.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}