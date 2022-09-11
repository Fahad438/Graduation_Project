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

public class AdminAddBook extends AppCompatActivity {
    EditText Bname, Blink, Bcover;
    TextView warning;
    Button addBookBtn;
    RequestQueue requestQueue;
    ArrayList<String> bookList = new ArrayList<String>();
    String url = "https://abcprogproject.000webhostapp.com/library.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_library_add);

        Bname = findViewById(R.id.bookName);
        Bcover = findViewById(R.id.bookCover);
        Blink = findViewById(R.id.bookLink);
        addBookBtn = findViewById(R.id.addBookBtn);
        warning = findViewById(R.id.warning);

        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Bname.getText().toString().equals("") || Bcover.getText().toString().equals("") || Blink.getText().toString().equals("")) {
                    warning.setText("You must fill required info to proceed");
                } else if (Bname.getText().length() < 3) {
                    warning.setText("Book title is too short");
                } else if (bookList.contains(Bname.getText().toString())) {
                    warning.setText("The book you are trying to add is already in the library");
                } else {
                    addBook();
                }
            }
        });
        getBook();
    }

    public void addBook( ) {
        requestQueue = Volley.newRequestQueue(this);
        String getName = Bname.getText().toString().trim().replaceAll(" +", " ");
        String getCover = Bcover.getText().toString();
        String getLink = Blink.getText().toString();
        String data_url = "https://abcprogproject.000webhostapp.com/AddBooks.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, data_url,
                response -> Toast.makeText(AdminAddBook.this, "Book added successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(AdminAddBook.this, "Error: Failed to add book, try again", Toast.LENGTH_SHORT).show()) {

            //Add parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();

                parms.put("Bname", getName);
                parms.put("Bcover", getCover);
                parms.put("Blink", getLink);
                return parms;
            }
        };
        requestQueue = Volley.newRequestQueue(AdminAddBook.this);
        requestQueue.add(stringRequest);
        Bname.setText("");
        Bcover.setText("");
        Blink.setText("");

        bookList.clear();
        getBook();
    }

    public void getBook() {
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("books");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject respons = jsonArray.getJSONObject(i);
                        String title = respons.getString("title");
                        bookList.add(title);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminAddBook.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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