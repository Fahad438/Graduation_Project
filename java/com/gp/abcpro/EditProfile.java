package com.gp.abcpro;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    private EditText usernameEdit, aboutEdit;
    private Button saveChanges, uploadImage;
    private CircleImageView profileImg;
    private TextView username, usernameTxt, aboutTxt, editWarning;
    private FloatingActionButton editProfilePic;

    private String uId, uName, uProfileIMG, uAbout;
    private SharedPreferences shared_getData;
    private SharedPreferences.Editor editor;
    private static final String KEY_PREF_NAME = "userData";

    private RequestQueue requestQueue;
    private AlertDialog.Builder builder;

    public static final String UPLOAD_URL = "https://abcprogproject.000webhostapp.com/editProfilePic.php";
    public static final String UPLOAD_KEY = "nProfilePic";

    Bitmap bitmap;
    Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImg = findViewById(R.id.profileImg);
        username = findViewById(R.id.user_name);

        editWarning = findViewById(R.id.editWarning);
        usernameTxt = findViewById(R.id.usernameTxt);
        aboutTxt = findViewById(R.id.aboutTxt);

        editProfilePic = findViewById(R.id.editProfilePic);
        usernameEdit = findViewById(R.id.usernameEdit);
        aboutEdit = findViewById(R.id.aboutEdit);

        saveChanges = findViewById(R.id.saveNewProfile);
        uploadImage = findViewById(R.id.uploadImage);


        builder = new AlertDialog.Builder(this);

        shared_getData = getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
        editor = shared_getData.edit();
        uId = (shared_getData.getString("id", ""));
        uProfileIMG = (shared_getData.getString("profile_picture", ""));
        uName = (shared_getData.getString("_username", ""));
        uAbout = (shared_getData.getString("about", ""));

        Glide.with(this).load(uProfileIMG).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.default_profile).into(profileImg);
        username.setText(uName);

        usernameEdit.setText(uName);
        aboutEdit.setText(uAbout);

        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWarning.setText("");
                usernameTxt.setVisibility(View.GONE);
                usernameEdit.setVisibility(View.GONE);
                usernameEdit.setEnabled(false);
                aboutTxt.setVisibility(View.GONE);
                aboutEdit.setVisibility(View.GONE);
                saveChanges.setVisibility(View.GONE);
                uploadImage.setVisibility(View.VISIBLE);

                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Select your new profile picture"), 1);
            }
        });


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nUsername = usernameEdit.getText().toString().trim();
                String nAbout = aboutEdit.getText().toString().trim();

                String url = "https://abcprogproject.000webhostapp.com/editProfile.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> Toast.makeText(EditProfile.this, "Profile edited successfully", Toast.LENGTH_LONG).show(),
                        error -> Toast.makeText(EditProfile.this, "Failed to edit profile", Toast.LENGTH_LONG).show()) {

                    //Add parameters to the request
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parms = new HashMap<>();
                        parms.put("user_id", uId);
                        parms.put("nUsername", nUsername);
                        parms.put("nAbout", nAbout);
                        return parms;
                    }
                };
                if (nUsername.equals(uName) && nAbout.equals(uAbout)) {
                    editWarning.setText("Unable to proceed, No changes were made");

                } else if (nUsername.length() < 5) {
                    editWarning.setText("Username is too short");
                } else {
                    requestQueue = Volley.newRequestQueue(EditProfile.this);
                    requestQueue.add(stringRequest);
                    editor.putString("_username", nUsername);
                    editor.putString("about", nAbout);
                    editor.apply();
                    Intent intent = new Intent(EditProfile.this, Profile.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditProfile.this, "Uploading Image", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getUserInfo(shared_getData.getString("username", ""), shared_getData.getString("password", ""));
                Intent intent = new Intent(EditProfile.this, Profile.class);
                startActivity(intent);
                finish();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                if (params[0] != null){
                    Bitmap bitmap = params[0];
                    String nProfilePic = getStringImage(bitmap);
                    HashMap<String, String> data = new HashMap<>();
                    data.put(UPLOAD_KEY, nProfilePic);
                    data.put("user_id", uId);
                    String result = rh.sendPostRequest(UPLOAD_URL, data);
                    return result;
                }
                return "";
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    @Override
    public void onBackPressed() {
        if (usernameEdit.isEnabled()) {
            builder.setMessage("Leave without saving changes?").setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(EditProfile.this, Profile.class);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
        } else {
            editWarning.setText("");
            usernameTxt.setVisibility(View.VISIBLE);
            usernameEdit.setVisibility(View.VISIBLE);
            usernameEdit.setEnabled(true);
            aboutTxt.setVisibility(View.VISIBLE);
            aboutEdit.setVisibility(View.VISIBLE);
            saveChanges.setVisibility(View.VISIBLE);
            uploadImage.setVisibility(View.GONE);
        }
    }

    void getUserInfo(String username, String password) {

        String accountInfoUrl = "https://abcprogproject.000webhostapp.com/getInfo.php?username=" + username;//سوي ال php
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, accountInfoUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            shared_getData = getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
                            editor = shared_getData.edit();
                            JSONArray jsonArray = response.getJSONArray("allaccounts");
                            JSONObject resp = jsonArray.getJSONObject(0);
                            if (resp.getString("username").equals(username) || resp.getString("email").equals(username)) {
                                editor.putString("profile_picture", resp.getString("profile_picture"));
                                editor.apply();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> Log.e("VOLLEY", "ERROR"));
        requestQueue.add(jsonObjectRequest);
    }
}