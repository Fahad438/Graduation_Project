package com.gp.abcpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;


public class Lessons extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    private RequestQueue requestQueue;
    private BottomNavigationView bottomNavigationView;

    private LessonsAdapter lessonsAdapter;
    private ArrayAdapter<String> chapterAdapter;

    private ArrayList<Lesson> lessonsList = new ArrayList<Lesson>();
    private ArrayList<Lesson> temp = new ArrayList<Lesson>();
    private ArrayList<String> chaptersList = new ArrayList<>();

    private LessonsAdapter.RecyclerViewClickListener listener;
    private RecyclerView recyclerView;
    private Spinner chapterNumSpinner;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView user_name;
    private CircleImageView profile_pic;

    private AlertDialog.Builder builder;

    private String uName, profileIMG;
    private SharedPreferences shared_getData;
    private SharedPreferences.Editor editor;
    private static final String KEY_PREF_NAME = "userData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);
        getWindow().setBackgroundDrawableResource(R.drawable.img);
        String url = "https://abcprogproject.000webhostapp.com/lessons.php";

        shared_getData = getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
        editor = shared_getData.edit();
        profileIMG = (shared_getData.getString("profile_picture", ""));
        uName = (shared_getData.getString("_username", ""));

        builder = new AlertDialog.Builder(this);

        chapterNumSpinner = findViewById(R.id.chapterNum);
        recyclerView = findViewById(R.id.lessonsListRV);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        user_name = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        profile_pic = navigationView.getHeaderView(0).findViewById(R.id.profile_pic);

        user_name.setText(uName);
        Glide.with(this).load(profileIMG).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.default_profile).into(profile_pic);

        navigationView.setNavigationItemSelectedListener(this);


        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("lessons");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int chapter_num = jsonObject.optInt("chapter_num");
                        int lesson_num = jsonObject.optInt("lesson_num");
                        String lesson_name = jsonObject.optString("lesson_name");
                        String lessonURL = jsonObject.optString("content");

                        if (!chaptersList.contains("Chapter " + chapter_num))
                            chaptersList.add("Chapter " + chapter_num);
                        chapterAdapter = new ArrayAdapter<>(Lessons.this, android.R.layout.simple_spinner_item, chaptersList);
                        chapterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        chapterNumSpinner.setAdapter(chapterAdapter);
                        lessonsList.add(new Lesson(chapter_num, lesson_num, lesson_name, lessonURL));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Lessons.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        });

        requestQueue.add(jsonObjectRequest);
        chapterNumSpinner.setOnItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.navBtn);
        bottomNavigationView.setSelectedItemId(R.id.course);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homepage:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        return true;

                    case R.id.course:
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
                        return false;
                }
                return false;
            }
        });
    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAdapter(int selected) {
        temp.clear();
        for (int i = 0; i < lessonsList.size(); i++) {
            if (lessonsList.get(i).getChapterNum() == (selected+1)) {
                temp.add(lessonsList.get(i));
            }
        }

        Set<Object> set = new HashSet<>();
        temp.removeIf(e -> !set.add(e.getLessonNum()));

        setOnClickListener();
        lessonsAdapter = new LessonsAdapter(this, temp, listener);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(lessonsAdapter);
    }

    private void setOnClickListener() {
        listener = new LessonsAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), StartLesson.class);
                intent.putExtra("lessonName", temp.get(position).getLessonName());
                intent.putExtra("lessonURL", temp.get(position).getLessonURL());
                startActivity(intent);
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.chapterNum) {
            setAdapter(position);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.closeDrawer(GravityCompat.START);
            finish();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_IELTS:
                startActivity(new Intent(getApplicationContext(), IELTS.class));
                finish();
                return true;

            case R.id.nav_account:
                startActivity(new Intent(getApplicationContext(), AccountSettings.class));
                finish();
                return true;

            case R.id.nav_about:
                startActivity(new Intent(getApplicationContext(), AboutUs.class));
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
        }
        return false;
    }

    public void logout(){
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}