package com.gp.abcpro;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Exercises extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    private RequestQueue requestQueue;
    private BottomNavigationView bottomNavigationView;

    private ExercisesAdapter exercisesAdapter;
    private ArrayAdapter<String> chapterAdapter;

    private ArrayList<Exercise> exerciseList = new ArrayList<>();
    private ArrayList<Exercise> temp = new ArrayList<>();
    private ArrayList<String> chaptersList = new ArrayList<>();

    private ArrayList<Exercise> quAnsList = new ArrayList<>();
    public static ArrayList<Exercise> selectedChQuAnsList = new ArrayList<>();

    private ArrayList<Exercise> correctAnsList = new ArrayList<>();
    public static ArrayList<Exercise> selectedChCorrectAnsList = new ArrayList<>();

    private ExercisesAdapter.RecyclerViewClickListener listener;
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
        setContentView(R.layout.activity_exercises);
        getWindow().setBackgroundDrawableResource(R.drawable.img);

        String url = "https://abcprogproject.000webhostapp.com/exercises.php";

        recyclerView = (RecyclerView) findViewById(R.id.exListRV);

        requestQueue = Volley.newRequestQueue(this);
        chapterNumSpinner = findViewById(R.id.chapterNum);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        user_name = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        profile_pic = navigationView.getHeaderView(0).findViewById(R.id.profile_pic);

        shared_getData = getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
        editor = shared_getData.edit();
        profileIMG = (shared_getData.getString("profile_picture", ""));
        uName = (shared_getData.getString("_username", ""));

        builder = new AlertDialog.Builder(this);

        user_name.setText(uName);
        Glide.with(this).load(profileIMG).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.default_profile).into(profile_pic);
        navigationView.setNavigationItemSelectedListener(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("chQue");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int chapter_num = jsonObject.optInt("chapter_num");
                        int lesson_num = jsonObject.getInt("lesson_num");
                        String question = jsonObject.getString("question");
                        String ans1 = jsonObject.getString("answer1");
                        String ans2 = jsonObject.getString("answer2");
                        String ans3 = jsonObject.getString("answer3");
                        String ans4 = jsonObject.getString("answer4");
                        String correctAns = jsonObject.getString("correct_answer");

                        if (!chaptersList.contains("Chapter " + chapter_num))
                            chaptersList.add("Chapter " + chapter_num);
                        chapterAdapter = new ArrayAdapter<>(Exercises.this, android.R.layout.simple_spinner_item, chaptersList);
                        chapterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        chapterNumSpinner.setAdapter(chapterAdapter);

                        Exercise exercise = new Exercise(chapter_num, lesson_num);
                        exerciseList.add(exercise);

                        Exercise questionAnswers = new Exercise(lesson_num, question, ans1, ans2, ans3, ans4);
                        quAnsList.add(questionAnswers);

                        Exercise correctAnswer = new Exercise(lesson_num, correctAns);
                        correctAnsList.add(correctAnswer);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Exercises.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAdapter(int selected) {
        selectedChQuAnsList.clear();
        selectedChCorrectAnsList.clear();
        temp.clear();
        for (int i = 0; i < exerciseList.size(); i++) {
            if (exerciseList.get(i).getChapterNum() == (selected+1)) {
                temp.add(exerciseList.get(i));
                selectedChQuAnsList.add(quAnsList.get(i));
                selectedChCorrectAnsList.add(correctAnsList.get(i));
            }
        }
        Set<Object> set = new HashSet<>();
        temp.removeIf(e -> !set.add(e.getLessonNum()));
        setOnClickListener();

        exercisesAdapter = new ExercisesAdapter(this, temp, listener);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(exercisesAdapter);
    }


    private void setOnClickListener() {
        listener = new ExercisesAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), StartExercise.class);
                intent.putExtra("lessonNum", temp.get(position).getLessonNum());
                startActivity(intent);
                finish();
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
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
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