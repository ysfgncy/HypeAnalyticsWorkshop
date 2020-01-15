package com.gencay.yusuf.hypeanalyticsworkshop;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class WebViewJava extends AppCompatActivity {

    public static android.webkit.WebView webView;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Bundle params = new Bundle();
        params.putString("screenName", "WebView");
        params.putString("platform", "android");
        params.putString("userid", uid);
        mFirebaseAnalytics.logEvent("screenView", params);


        webView = findViewById(R.id.webviewid);
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);


        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://sunkagitmatbaa.com/hype/");

    }

}
