package com.gp.abcpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class IELTS extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private CardView IELTSInro, IELTSTest;
    private AlertDialog.Builder builder;

    private boolean doubleBackToExitPressedOnce = false;
    private Toast toast;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView user_name;
    private CircleImageView profile_pic;

    private String uName, profileIMG;
    private SharedPreferences shared_getData;
    private SharedPreferences.Editor editor;
    private static final String KEY_PREF_NAME = "userData";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ielts);

        shared_getData = getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
        editor = shared_getData.edit();
        profileIMG = (shared_getData.getString("profile_picture", ""));
        uName = (shared_getData.getString("_username", ""));

        IELTSInro = findViewById(R.id.intro);
        IELTSTest = findViewById(R.id.test);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        user_name = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        profile_pic = navigationView.getHeaderView(0).findViewById(R.id.profile_pic);

        user_name.setText(uName);
        Glide.with(this).load(profileIMG).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.default_profile).into(profile_pic);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.navBtn);
        bottomNavigationView.setSelectedItemId(R.id.more);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homepage:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        return true;

                    case R.id.course:
                        startActivity(new Intent(getApplicationContext(), Course.class));
                        finish();
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        finish();
                        return true;

                    case R.id.library:
                        startActivity(new Intent(getApplicationContext(), Library.class));
                        finish();
                        return true;

                    case R.id.more:
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else {
                            drawerLayout.openDrawer(GravityCompat.START);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    public void onClick(View view) {
        builder = new AlertDialog.Builder(this);
        switch (view.getId()) {
            case R.id.intro:
                Intent intent = new Intent(getApplicationContext(), StartLesson.class);
                intent.putExtra("lessonName", "Introduction to IELTS");
                intent.putExtra("lessonURL", "https://www.ielts.org/-/media/publications/information-for-candidates/ielts-information-for-candidates-english-uk.ashx");
                startActivity(intent);
                break;

            case R.id.test:
                builder.setMessage("Do you want to start mock ielts test")
                        .setCancelable(true)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(getApplicationContext(), IELTSTest1.class));
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
                break;
        }
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                drawerLayout.closeDrawer(GravityCompat.START);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_IELTS:
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_account:
                startActivity(new Intent(getApplicationContext(), AccountSettings.class));
                finish();
                return true;

            case R.id.nav_about:
                startActivity(new Intent(getApplicationContext(), Profile.class));
                finish();
                return true;

            case R.id.nav_logout:
                builder.setMessage("Are you sure you want to logout of your account?").setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                logout();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
                return true;
        }
        return false;
    }

    public void logout() {
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}