package com.gencay.yusuf.hypeanalyticsworkshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Kayıt Ol Ekranı";

    private EditText epostaField;
    private EditText sifreField;

    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        GoogleAnalytics.getInstance(this).setLocalDispatchPeriod(1);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Bundle params = new Bundle();
        params.putString("screenName", "Kayıt Ol");
        params.putString("platform", "android");
        params.putString("userid", uid);
        mFirebaseAnalytics.logEvent("screenView", params);

        epostaField = findViewById(R.id.inputEposta);
        sifreField = findViewById(R.id.insputSifre);

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(epostaField.getText().toString(), sifreField.getText().toString());
            }
        });


        findViewById(R.id.buttonSignIN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent activity2Intent = new Intent(getApplicationContext(), GirisYap.class);
                startActivity(activity2Intent);
            }
        });

        findViewById(R.id.buttonWebView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity1intent = new Intent(getApplicationContext(), WebViewJava.class);
                startActivity(activity1intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);

            }
        });


    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            Bundle params = new Bundle();
                            params.putString("eventCategory", "Functions");
                            params.putString("eventAction", "SignUp");
                            params.putString("eventLabel", "Success");
                            params.putString("platform", "android");
                            params.putString("userid", uid);
                            mFirebaseAnalytics.logEvent("GAEvent", params);


                            Intent activity2Intent = new Intent(getApplicationContext(), GirisYap.class);
                            startActivity(activity2Intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            Bundle params = new Bundle();
                            params.putString("eventCategory", "Functions");
                            params.putString("eventAction", "SignUp");
                            params.putString("eventLabel", "Failure");
                            params.putString("userid", uid);
                            params.putString("platform", "android");

                            mFirebaseAnalytics.logEvent("GAEvent", params);


                        }


                    }
                });
        // [END create_user_with_email]
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = epostaField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            epostaField.setError("Required.");
            valid = false;
        } else {
            epostaField.setError(null);
        }

        String password = sifreField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            sifreField.setError("Required.");
            valid = false;
        } else {
            sifreField.setError(null);
        }

        return valid;
    }


}
