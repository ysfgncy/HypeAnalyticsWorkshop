package com.gencay.yusuf.hypeanalyticsworkshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
        Bundle params = new Bundle();
        params.putString("screenName", "Giriş Yapıldı");
        mFirebaseAnalytics.logEvent("screenView", params);


        findViewById(R.id.buttonSignOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();

                Bundle params = new Bundle();
                params.putString("eventCategory", "Functions");
                params.putString("eventAction", "SignOut");
                params.putString("eventLabel", "Success");
                mFirebaseAnalytics.logEvent("GAEvent", params);

                Intent activity2Intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activity2Intent);



            }
        });


        mAuth = FirebaseAuth.getInstance();


    }
}
