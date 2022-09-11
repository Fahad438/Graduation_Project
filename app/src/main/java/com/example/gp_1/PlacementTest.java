package com.example.gp_1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class PlacementTest extends AppCompatActivity {
    TextView textView;
    TextView aa;
    Button a1, a2, a3, a4;
    RequestQueue requestQueue;
    Button Next;
    String TrueAnswer = "";
    String selectAns = "";
    String url = "http://192.168.8.140/myprojct/Qplacement.php";
    int z;
    int i = (int) (Math.random() * 5);
    int c = 0;
    static int mark;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement_test);
        ArrayList<Integer> s = new ArrayList<>();
        Next = (Button) findViewById(R.id.Next);
        textView = (TextView) findViewById(R.id.textViewQ);
        aa = (TextView) findViewById(R.id.textView6);

        a1 = (Button) findViewById(R.id.button3);
        a2 = (Button) findViewById(R.id.button4);
        a3 = (Button) findViewById(R.id.button5);
        a4 = (Button) findViewById(R.id.button6);
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    JSONArray jsonArray = response.getJSONArray("allq");

                    z = jsonArray.length();


                    JSONObject respons = jsonArray.getJSONObject(i);

                    String question = respons.getString("question");
                    String answer1 = respons.getString("answer1");
                    String answer2 = respons.getString("answer2");
                    String answer3 = respons.getString("answer3");
                    String answer4 = respons.getString("answer4");
                    TrueAnswer = respons.getString("TrueAnswer");

                    textView.append(question);
                    a1.setText(answer1);
                    a2.setText(answer2);
                    a3.setText(answer3);
                    a4.setText(answer4);
                    //aa.setText(TrueAnswer);
                    Next.setText("Next");

                    s.add(i);// جرب بعدين تشيلة


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", "ERROR");
            }
        }

        );

        requestQueue.add(jsonObjectRequest);//هنا نستعدي الاوبجكت الي سوينا فيه عملية سحب البيانات من الداتا بيس
        //==========================================================================================
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectAns = (String) a1.getText();

                a1.setBackgroundResource(R.drawable.btn_selected_ans);
                a2.setBackgroundResource(R.drawable.btn_answer);
                a3.setBackgroundResource(R.drawable.btn_answer);
                a4.setBackgroundResource(R.drawable.btn_answer);


            }
        });

        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectAns = (String) a2.getText();
                a1.setBackgroundResource(R.drawable.btn_answer);
                a2.setBackgroundResource(R.drawable.btn_selected_ans);
                a3.setBackgroundResource(R.drawable.btn_answer);
                a4.setBackgroundResource(R.drawable.btn_answer);

            }
        });

        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectAns = (String) a3.getText();
                a1.setBackgroundResource(R.drawable.btn_answer);
                a2.setBackgroundResource(R.drawable.btn_answer);
                a3.setBackgroundResource(R.drawable.btn_selected_ans);
                a4.setBackgroundResource(R.drawable.btn_answer);

            }
        });

        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectAns = (String) a4.getText();
                a1.setBackgroundResource(R.drawable.btn_answer);
                a2.setBackgroundResource(R.drawable.btn_answer);
                a3.setBackgroundResource(R.drawable.btn_answer);
                a4.setBackgroundResource(R.drawable.btn_selected_ans);

            }

        });
        //==========================================================================================

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectAns.equals(TrueAnswer)) {
                    mark = mark + 1;
                } else {
                    mark = mark + 0;
                }
                aa.setText(String.valueOf(mark));


                c++;
                if (c <= 9) {
                    //for (y = 0; y < 1000; y++)
                    while (true) {

                        if (s.contains(i)) {
                            i = (int) (Math.random() * z);

                            if (!s.contains(i)) {

                                textView.setText("");
                                a1.setText("");
                                a2.setText("");
                                a3.setText("");
                                a4.setText("");


                                requestQueue.add(jsonObjectRequest);
                                break;
                            }
                        }
                    }
                } else {
                    Intent intent = new Intent(PlacementTest.this, MarkPlacement.class);
                    startActivity(intent);
                    ;
                }

                a1.setBackgroundResource(R.drawable.btn_answer);
                a2.setBackgroundResource(R.drawable.btn_answer);
                a3.setBackgroundResource(R.drawable.btn_answer);
                a4.setBackgroundResource(R.drawable.btn_answer);
            }


        });


    }


}

