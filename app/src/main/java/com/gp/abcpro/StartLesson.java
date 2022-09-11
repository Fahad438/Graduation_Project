package com.gp.abcpro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class StartLesson extends AppCompatActivity {
    private String lessonName, lessonUrl;
    private TextView title;
    private WebView content;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_lesson);
        getWindow().setBackgroundDrawableResource(R.drawable.img);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lessonName = (String) extras.get("lessonName");
            lessonUrl = (String) extras.get("lessonURL");
        }

        title = findViewById(R.id.lessonNameTxt);
        content = findViewById(R.id.lessonContentWV);

        title.setText(lessonName);
        content.setWebViewClient(new WebViewClient());
        content.getSettings().setSupportZoom(true);
        content.getSettings().setJavaScriptEnabled(true);
        showPdf(lessonUrl);
    }

    private void showPdf(final String imageString) {
        content.invalidate();
        content.getSettings().setJavaScriptEnabled(true);
        content.getSettings().setSupportZoom(true);
        content.loadUrl("http://docs.google.com/gview?embedded=true&url=" + imageString);
        content.setWebViewClient(new WebViewClient() {
            boolean checkhasOnPageStarted = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                checkhasOnPageStarted = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (checkhasOnPageStarted ) {
                } else {
                    showPdf(imageString);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}