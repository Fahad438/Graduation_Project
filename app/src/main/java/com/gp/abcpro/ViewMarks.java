package com.gp.abcpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewMarks extends AppCompatActivity {
    private TextView markView, dateView, nameView, textDisp;
    private Spinner chapterSpinner;
    private CardView markCV;
    private int selectedChapter;
    private String profilePic, userName, id, selected;
    private RequestQueue requestQueue;

    ArrayList<String> list = new ArrayList<>();
    ArrayList<Integer> chNumList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<Integer> markList = new ArrayList<>();

    ArrayList<String> chaptersList = new ArrayList<>();
    private ArrayAdapter<String> chapterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_marks);
        getWindow().setBackgroundDrawableResource(R.drawable.img);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            profilePic = (String) extras.get("userPic");
            userName = (String) extras.get("userName");
            id = (String) extras.get("id");
        }

        String url = "https://abcprogproject.000webhostapp.com/show.php?user_id=" + id;

        nameView = findViewById(R.id.userName);
        markView = findViewById(R.id.markDisp);
        dateView = findViewById(R.id.dateDisp);
        chapterSpinner = findViewById(R.id.testNum);
        markCV = findViewById(R.id.markCV);
        textDisp = findViewById(R.id.selectCh);

        Glide.with(this).load(profilePic).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.default_profile).into((CircleImageView)findViewById(R.id.profile_image));

        nameView.setText(userName);


        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("allMarks");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int chapter_num = jsonObject.getInt("chapter_num");
                        String date = jsonObject.getString("date");
                        int mark1 = jsonObject.getInt("mark");
                        chNumList.add(chapter_num);
                        dateList.add(date);
                        markList.add(mark1);
                    }
                    processMarks(chNumList);
                    for (int i = 0; i < list.size(); i++) {
                        chaptersList.add("Chapter " + list.get(i).split(",")[0]);
                        //chaptersList.add("Chapter " + list2.get(i).getChapter_num());
                    }
                    chapterAdapter = new ArrayAdapter<>(ViewMarks.this, android.R.layout.simple_spinner_item, chaptersList);
                    chapterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    chapterSpinner.setAdapter(chapterAdapter);
                    hide(chaptersList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewMarks.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        });

        requestQueue.add(jsonObjectRequest);

        chapterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedChapter = position;
                showMark(selectedChapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showMark(Integer chapter) {
        String[] string = list.get(chapter).toString().split(",");
        Integer chNum = Integer.parseInt(string[0]);
        String date = string[1];
        String mark = string[2];

        markView.setText(mark + " / 10");
        dateView.setText(date);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewMarks.this, Profile.class);
        startActivity(intent);
        finish();
    }

    public void hide(ArrayList al) {
        if (al.size() > 0) {
            chapterSpinner.setVisibility(View.VISIBLE);
            markCV.setVisibility(View.VISIBLE);
            textDisp.setText("Select chapter");
        } else {
            chapterSpinner.setVisibility(View.GONE);
            markCV.setVisibility(View.GONE);
            textDisp.setText("You have not taken any quiz\ngo to quizzes section to take\nyour first quiz");
            textDisp.setGravity(Gravity.CENTER);
        }
    }

    public void processMarks(ArrayList<Integer> ls) {
        ArrayList<Integer> lsNoDuplicate = new ArrayList();
        for (int i = 0; i < ls.size(); i++) {
            if (!lsNoDuplicate.contains(ls.get(i))) {
                lsNoDuplicate.add(ls.get(i));
            }
        }

        for (int i = 0; i < lsNoDuplicate.size(); i++) {
            ArrayList<Integer> al = new ArrayList();
            int max = 0, maxCh = 0;
            String maxDt = "";
            for (int j = 0; j < ls.size(); j++) {
                if (lsNoDuplicate.get(i).equals(chNumList.get(j)) && markList.get(j) > max) {
                    max = markList.get(j);
                    maxCh = chNumList.get(j);
                    maxDt = dateList.get(j);
                }
            }
            al.add(markList.indexOf(max));
            list.add(maxCh + "," + maxDt + "," + max);
        }
    }
}