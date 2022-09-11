package com.gp.abcpro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AdminAddChapter extends AppCompatActivity implements View.OnClickListener {
    Button addChapterDB;
    TextView warning;
    EditText chapterNum;
    RequestQueue requestQueue;
    ArrayList<String> chapterList = new ArrayList<String>();
    String url = "https://abcprogproject.000webhostapp.com/chapters.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_addchapter);

        chapterNum = findViewById(R.id.chapterNum);
        warning = findViewById(R.id.warning);

        addChapterDB = findViewById(R.id.addChapterDB);
        addChapterDB.setOnClickListener(this);

        data();
    }

    @Override
    public void onClick(View view) {
        String chapter_num = chapterNum.getText().toString().trim().replaceAll(" +", " ");
        int chNum = Integer.parseInt(chapter_num);
        if (view.getId() == R.id.addChapterDB) {
            String data_url = "https://abcprogproject.000webhostapp.com/addChapter.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, data_url,
                    response -> Toast.makeText(AdminAddChapter.this, "Chapter added successfully", Toast.LENGTH_SHORT).show(),
                    error -> Toast.makeText(AdminAddChapter.this, "Error: Failed to add a new chapter", Toast.LENGTH_SHORT).show()) {

                //Add parameters to the request
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parms = new HashMap<>();
                    parms.put("chapter_num", "Chapter "+chapter_num);
                    return parms;
                }
            };
            if (chapter_num.isEmpty()) {
                warning.setText("Enter the chapter number you want to add");
            } else if (chNum <= 0 || chNum > 30) {
                warning.setText("Invalid chapter number");
                chapterNum.setError("Chapter number must be between 1 and 30");

            } else if (chapterList.contains(chapter_num)) {
                warning.setText("Chapter already exists");
            } else {
                requestQueue = Volley.newRequestQueue(AdminAddChapter.this);
                requestQueue.add(stringRequest);
                data();
                chapterNum.setText("");
                warning.setText("");
            }
        }
    }

    public void data() {
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("allChapters");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject respons = jsonArray.getJSONObject(i);
                        String chapterNum = respons.getString("id");
                        chapterList.add(chapterNum);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminAddChapter.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}