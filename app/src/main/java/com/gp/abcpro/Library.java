package com.gp.abcpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.RequiresApi;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Library extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private boolean doubleBackToExitPressedOnce = false;
    private RequestQueue requestQueue;
    private EditText bookName;
    private ArrayList<Book> booksArrayList = new ArrayList<Book>();
    private BooksAdapter booksAdapter;
    private BooksAdapter.RecyclerViewClickListener listener;
    private String url = "https://abcprogproject.000webhostapp.com/library.php";

    private RecyclerView recyclerView;
    private Toast toast;

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
        setContentView(R.layout.activity_library);
        getWindow().setBackgroundDrawableResource(R.drawable.img);

        bookName = findViewById(R.id.bookName);
        recyclerView=findViewById(R.id.booksListRV);

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
        requestQueue = Volley.newRequestQueue(this);

        recycle(url);

        listener = new BooksAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(booksArrayList.get(position).getLink()));
                String title = "Open with";
                Intent chooser = Intent.createChooser(intent, title);
                startActivity(chooser);
            }
        };

        bottomNavigationView = findViewById(R.id.navBtn);
        bottomNavigationView.setSelectedItemId(R.id.library);


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

    public void recycle (String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("books");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.optInt("id");
                        String title = jsonObject.getString("title");
                        String cover_img = jsonObject.getString("cover_img");
                        String link = jsonObject.getString("link");
                        booksArrayList.add(new Book(id, title, cover_img, link));
                    }
                    booksAdapter = new BooksAdapter(Library.this, booksArrayList, listener);
                    recyclerView.setHasFixedSize(true);
                    GridLayoutManager glm = new GridLayoutManager(Library.this, 2, GridLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(glm);
                    recyclerView.setAdapter(booksAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Library.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
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

    public void search(View view) {
        booksArrayList.clear();
        String url = "https://abcprogproject.000webhostapp.com/libsearch.php?searchString=" + bookName.getText();
        recycle(url);
    }
}