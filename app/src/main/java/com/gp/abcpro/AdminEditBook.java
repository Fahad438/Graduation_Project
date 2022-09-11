package com.gp.abcpro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AdminEditBook extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner10;
    EditText newBname, newBlink,newBookCover;
    TextView warning;
    Button modifyNewBookBtn;
    String selectedBook;
    RequestQueue requestQueue;
    ArrayList<String> bookList;
    ArrayList<String> temp;
    ArrayAdapter<String> bookAdapter;
    String url = "https://abcprogproject.000webhostapp.com/library.php";
    int selectedPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_library_modify);
        bookList = new ArrayList<String>();
        temp = new ArrayList<String>();
        spinner10 = findViewById(R.id.selectBook);
        newBname = findViewById(R.id.newBookName);
        newBlink = findViewById(R.id.newBookLink);
        newBookCover = findViewById(R.id.newBookCover);
        modifyNewBookBtn = findViewById(R.id.modifyNewBookBtn);
        warning = findViewById(R.id.warning);
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
                    setAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminEditBook.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", "ERROR");
            }
        });
        requestQueue.add(jsonObjectRequest);
        spinner10.setOnItemSelectedListener(this);

        modifyNewBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newBname.getText().toString().equals("") || newBlink.getText().toString().equals("") || newBookCover.getText().toString().equals("")) {
                    warning.setText("You must fill required info to proceed");
                } else if (bookList.contains(newBname.getText().toString())) {
                    warning.setText("Book already exists in library");
                } else {
                    String getName = newBname.getText().toString().trim().replaceAll(" +", " ");
                    String getLink = newBlink.getText().toString().trim();
                    String getBookCover=newBookCover.getText().toString().trim().replaceAll(" +", " ");
                    String data_url = "https://abcprogproject.000webhostapp.com/modifyBook.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, data_url,
                            response -> Toast.makeText(AdminEditBook.this, "Book info edited successfully", Toast.LENGTH_SHORT).show(),
                            error -> Toast.makeText(AdminEditBook.this, "Error: Failed to edit selected book info", Toast.LENGTH_SHORT).show()) {

                        //Add parameters to the request
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parms = new HashMap<>();
                            parms.put("BoldBook", selectedBook);
                            parms.put("Bname", getName);
                            parms.put("Blink", getLink);
                            parms.put("Bcover", getBookCover);
                            return parms;
                        }
                    };
                    requestQueue.add(stringRequest);
                    warning.setText("");
                    newBname.setText("");
                    newBlink.setText("");
                    newBookCover.setText("");
                    bookList.remove(selectedBook);
                    bookList.add(selectedPos, getName);
                    temp.clear();
                    bookAdapter.clear();
                    setAdapter();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedBook = String.valueOf(spinner10.getSelectedItem().toString());
        selectedPos = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    public void setAdapter() {
        for (int i = 0; i < bookList.size(); i++) {
            temp.add(bookList.get(i));
        }
        bookAdapter = new ArrayAdapter<String>(AdminEditBook.this, android.R.layout.simple_spinner_item, temp);
        bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner10.setAdapter(bookAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

