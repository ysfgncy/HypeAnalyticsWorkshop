package com.gencay.yusuf.hypeanalyticsworkshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class GirisYapildi extends AppCompatActivity {

    private static final String TAG = "GirişYap Ekranı";

    private FirebaseAuth mAuth;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yapildi);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Bundle params = new Bundle();
        params.putString("screenName", "Giriş Yapıldı");
        params.putString("platform", "android");
        params.putString("userid", uid);
        mFirebaseAnalytics.logEvent("screenView", params);


        findViewById(R.id.buttonSignOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mAuth.signOut();

                Bundle params = new Bundle();
                params.putString("eventCategory", "Functions");
                params.putString("eventAction", "SignOut");
                params.putString("eventLabel", "Success");
                params.putString("platform", "android");
                params.putString("userid", uid);
                mFirebaseAnalytics.logEvent("GAEvent", params);

                Intent activity2Intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activity2Intent);


            }
        });


        mAuth = FirebaseAuth.getInstance();


    }
}
