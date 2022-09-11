package com.gp.abcpro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettings extends AppCompatActivity {
    private EditText email, passConfirm, oldPass, newPass, cPass;
    private Button changePass, saveEmail, savePass, manageAdmins, saveProfilePic, logout;
    private FloatingActionButton editProfilePic;
    private ImageView editEmail;
    private CircleImageView profileImg;
    private TextView username, passTxt, editWarning, emailTxt, oldPassTxt, newPassTxt, cPassTxt;

    private String uId, uPass, uName, uProfileIMG, uEmail, uIsAdmin;
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
        setContentView(R.layout.activity_account_settings);

        email = findViewById(R.id.emailEdit);
        passConfirm = findViewById(R.id.passConfirm);
        oldPass = findViewById(R.id.oldPass);
        oldPass.setEnabled(false);
        newPass = findViewById(R.id.newPass);
        cPass = findViewById(R.id.cPass);

        editProfilePic = findViewById(R.id.editProfilePic);
        changePass = findViewById(R.id.changePass);
        saveEmail = findViewById(R.id.saveEmail);
        saveProfilePic = findViewById(R.id.saveProfilePic);
        savePass = findViewById(R.id.savePass);
        manageAdmins = findViewById(R.id.manageAdmins);
        logout = findViewById(R.id.logout);

        editEmail = findViewById(R.id.btnEdit);

        profileImg = findViewById(R.id.profileImg);
        username = findViewById(R.id.user_name);
        passTxt = findViewById(R.id.passTxt);
        emailTxt = findViewById(R.id.emailTxt);
        oldPassTxt = findViewById(R.id.oldPassTxt);
        newPassTxt = findViewById(R.id.newPassTxt);
        cPassTxt = findViewById(R.id.cPassTxt);

        editWarning = findViewById(R.id.editWarning);

        builder = new AlertDialog.Builder(this);

        requestQueue = Volley.newRequestQueue(this);

        shared_getData = getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
        editor = shared_getData.edit();
        uId = (shared_getData.getString("id", ""));
        uPass = (shared_getData.getString("password", ""));
        uProfileIMG = (shared_getData.getString("profile_picture", ""));
        uName = (shared_getData.getString("_username", ""));
        uEmail = (shared_getData.getString("email", ""));
        uIsAdmin = (shared_getData.getString("isAdmin", ""));

        if (Integer.parseInt(uIsAdmin) == 2) {
            logout.setVisibility(View.VISIBLE);
            manageAdmins.setVisibility(View.VISIBLE);
            editProfilePic.setVisibility(View.VISIBLE);
        } else if (Integer.parseInt(uIsAdmin) == 1) {
            editProfilePic.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
        }

        Glide.with(this).load(uProfileIMG).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.default_profile).into(profileImg);
        username.setText(uName);
        email.setHint(uEmail);
        email.setEnabled(false);

        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passConfirm.setEnabled(false);
                emailTxt.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                editEmail.setVisibility(View.GONE);
                changePass.setVisibility(View.GONE);
                manageAdmins.setVisibility(View.GONE);
                logout.setVisibility(View.GONE);
                saveProfilePic.setVisibility(View.VISIBLE);
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Select your new profile picture"), 1);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Are you sure you want to logout of your account?").setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editor.clear();
                                editor.apply();
                                Intent intent = new Intent(AccountSettings.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        });

        manageAdmins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSettings.this, AdminManageAdmins.class);
                startActivity(intent);
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setEnabled(true);
                passTxt.setVisibility(View.VISIBLE);
                passConfirm.setVisibility(View.VISIBLE);
                saveEmail.setVisibility(View.VISIBLE);
                changePass.setVisibility(View.GONE);
                if (Integer.parseInt(uIsAdmin) == 2) {
                    manageAdmins.setVisibility(View.GONE);
                    logout.setVisibility(View.GONE);
                } else if (Integer.parseInt(uIsAdmin) == 1) {
                    logout.setVisibility(View.GONE);
                }
                saveEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nEmail = email.getText().toString().trim();
                        String nPassword = passConfirm.getText().toString().trim();
                        String url = "https://abcprogproject.000webhostapp.com/editEmail.php";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String success = jsonObject.optString("success");

                                            if (success.equals("1")) {
                                                Toast.makeText(AccountSettings.this, "Email changed successfully", Toast.LENGTH_SHORT).show();
                                                email.setText(nEmail);
                                                editor.putString("email", nEmail);
                                                editor.apply();
                                            } else {
                                                Toast.makeText(AccountSettings.this, success, Toast.LENGTH_SHORT).show();
                                                email.setText(uEmail);

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(AccountSettings.this, "Changes has not been saved!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(AccountSettings.this, "Changes has not been saved!", Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            //Add parameters to the request
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> parms = new HashMap<>();
                                parms.put("user_id", uId);
                                parms.put("Nemail", nEmail);
                                return parms;
                            }
                        };
                        builder.setMessage("You are changing your email from " + uEmail + "\nto " + nEmail + "\n\nDo you want to proceed?").setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (nEmail.equals("") || nPassword.equals("")) {
                                            editWarning.setText("Email and Password are required to proceed");

                                        } else if (!isEmailValid(nEmail)) {
                                            editWarning.setText("Invalid email address");

                                        } else if (!nPassword.equals(uPass)) {
                                            editWarning.setText("Incorrect password");

                                        } else {
                                            requestQueue.add(stringRequest);
                                            email.setEnabled(false);
                                            passConfirm.setText("");
                                            editWarning.setText("");
                                            passTxt.setVisibility(View.GONE);
                                            passConfirm.setVisibility(View.GONE);
                                            saveEmail.setVisibility(View.GONE);
                                            changePass.setVisibility(View.VISIBLE);
                                            if (Integer.parseInt(uIsAdmin) == 2) {
                                                manageAdmins.setVisibility(View.VISIBLE);
                                                logout.setVisibility(View.VISIBLE);
                                            } else if (Integer.parseInt(uIsAdmin) == 1) {
                                                logout.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                }).show();
                    }
                });
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide default fields
                emailTxt.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                editEmail.setVisibility(View.GONE);
                changePass.setVisibility(View.GONE);
                if (Integer.parseInt(uIsAdmin) == 2) {
                    manageAdmins.setVisibility(View.GONE);
                    logout.setVisibility(View.GONE);
                } else if (Integer.parseInt(uIsAdmin) == 1) {
                    logout.setVisibility(View.GONE);
                }

                // Show change password fields
                oldPass.setEnabled(true);
                oldPassTxt.setVisibility(View.VISIBLE);
                oldPass.setVisibility(View.VISIBLE);
                newPassTxt.setVisibility(View.VISIBLE);
                newPass.setVisibility(View.VISIBLE);
                cPassTxt.setVisibility(View.VISIBLE);
                cPass.setVisibility(View.VISIBLE);
                savePass.setVisibility(View.VISIBLE);

                savePass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oPass = oldPass.getText().toString().trim();
                        String nPass = newPass.getText().toString().trim();
                        String ncPass = cPass.getText().toString().trim();

                        String url = "https://abcprogproject.000webhostapp.com/editPass.php";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                response -> Toast.makeText(AccountSettings.this, "Password changed successfully", Toast.LENGTH_SHORT).show(),
                                error -> Toast.makeText(AccountSettings.this, "Failed to change password", Toast.LENGTH_SHORT).show()) {

                            //Add parameters to the request
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> parms = new HashMap<>();
                                parms.put("Puser_id", uId);
                                parms.put("Npass", nPass);
                                return parms;
                            }
                        };
                        builder.setMessage("Confirm changing your password?").setCancelable(true)
                                .setPositiveButton("Yes, change it", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (oPass.equals("") || nPass.equals("") || ncPass.equals("")) {
                                            editWarning.setText("Please fill in all fields to proceed");
                                        } else if (!oPass.equals(uPass)) {
                                            editWarning.setText("Incorrect password");

                                        } else if (!isPasswordValid(nPass)) {
                                            editWarning.setText("Incorrect password format");
                                            newPass.setError("Password must contain the following:\n" +
                                                    "- at least one digit.\n" +
                                                    "- at least one lowercase Letter.\n" +
                                                    "- at least one uppercase Letter.\n" +
                                                    "- at least one special character like ! @ # & ( ).\n" +
                                                    "- a length of at least 8 characters.");
                                        } else if (!nPass.equals(ncPass)) {
                                            editWarning.setText("Passwords do not match!");
                                        } else if (oPass.equals(nPass)) {
                                            editWarning.setText("New password can not be same as old password!");
                                        } else {
                                            requestQueue = Volley.newRequestQueue(AccountSettings.this);
                                            requestQueue.add(stringRequest);
                                            oldPass.setEnabled(false);
                                            editor.putString("password", nPass);
                                            editor.apply();
                                            oldPass.setText("");
                                            newPass.setText("");
                                            cPass.setText("");
                                            editWarning.setText("");
                                            // Hide change password fildes
                                            oldPassTxt.setVisibility(View.GONE);
                                            newPassTxt.setVisibility(View.GONE);
                                            cPassTxt.setVisibility(View.GONE);
                                            oldPass.setVisibility(View.GONE);
                                            newPass.setVisibility(View.GONE);
                                            cPass.setVisibility(View.GONE);
                                            savePass.setVisibility(View.GONE);
                                            // Show default fields
                                            emailTxt.setVisibility(View.VISIBLE);
                                            email.setVisibility(View.VISIBLE);
                                            editEmail.setVisibility(View.VISIBLE);
                                            changePass.setVisibility(View.VISIBLE);
                                            if (Integer.parseInt(uIsAdmin) == 2) {
                                                manageAdmins.setVisibility(View.VISIBLE);
                                                logout.setVisibility(View.VISIBLE);
                                            } else if (Integer.parseInt(uIsAdmin) == 1) {
                                                logout.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                }).show();
                    }
                });
            }
        });

        saveProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (email.isEnabled()) {
            if (Integer.parseInt(uIsAdmin) == 2) {
                manageAdmins.setVisibility(View.VISIBLE);
                logout.setVisibility(View.VISIBLE);
            } else if (Integer.parseInt(uIsAdmin) == 1) {
                logout.setVisibility(View.VISIBLE);
            }
            email.setEnabled(false);
            editWarning.setText("");
            passTxt.setVisibility(View.GONE);
            passConfirm.setVisibility(View.GONE);
            saveEmail.setVisibility(View.GONE);
            changePass.setVisibility(View.VISIBLE);
        } else if (oldPass.isEnabled()) {
            if (Integer.parseInt(uIsAdmin) == 2) {
                manageAdmins.setVisibility(View.VISIBLE);
                logout.setVisibility(View.VISIBLE);
            } else if (Integer.parseInt(uIsAdmin) == 1) {
                logout.setVisibility(View.VISIBLE);
            }
            oldPass.setEnabled(false);
            editWarning.setText("");
            // Hide change password fields
            oldPassTxt.setVisibility(View.GONE);
            newPassTxt.setVisibility(View.GONE);
            cPassTxt.setVisibility(View.GONE);
            oldPass.setVisibility(View.GONE);
            newPass.setVisibility(View.GONE);
            cPass.setVisibility(View.GONE);
            savePass.setVisibility(View.GONE);
            // Show default fields
            emailTxt.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            editEmail.setVisibility(View.VISIBLE);
            changePass.setVisibility(View.VISIBLE);
        } else if (!passConfirm.isEnabled()) {
            if (Integer.parseInt(uIsAdmin) == 2) {
                manageAdmins.setVisibility(View.VISIBLE);
            }
            passConfirm.setEnabled(true);
            emailTxt.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            editEmail.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            changePass.setVisibility(View.VISIBLE);
            saveProfilePic.setVisibility(View.GONE);
        } else {
            if (Integer.parseInt(uIsAdmin) != 2 && Integer.parseInt(uIsAdmin) != 1) {
                Intent intent = new Intent(AccountSettings.this, MainActivity.class);
                startActivity(intent);
            }
            finish();
        }
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
                loading = ProgressDialog.show(AccountSettings.this, "Uploading Image", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getUserInfo(shared_getData.getString("username", ""), shared_getData.getString("password", ""));
                Intent intent = new Intent(AccountSettings.this, AccountSettings.class);
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

    private boolean isEmailValid(String text) {
        CharSequence email = text;
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isPasswordValid(String text) {
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–_[{}]:;',?/*~$^+=<>]).{8,20}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}