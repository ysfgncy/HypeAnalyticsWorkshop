package com.gencay.yusuf.hypeanalyticsworkshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;

public class GirisYap extends AppCompatActivity {

    private static final String TAG = "GirişYap Ekranı";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private EditText eposta;
    private EditText sifre;

    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yap);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle params = new Bundle();
        params.putString("screenName", "Giriş Yap");
        mFirebaseAnalytics.logEvent("screenView", params);

        eposta = findViewById(R.id.inputEpostaGiris);
        sifre = findViewById(R.id.inputSifreGiris);

        findViewById(R.id.buttonLoginGiris).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(eposta.getText().toString(),sifre.getText().toString());
            }
        });

        findViewById(R.id.signInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signInWithGoogle();




            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);



                Bundle product1 = new Bundle();
                product1.putString( FirebaseAnalytics.Param.ITEM_ID, "sku1234");  // Ürün için ID
                product1.putString( FirebaseAnalytics.Param.ITEM_NAME, "Herkes İçin TV Paketi"); //Ürün adı
                product1.putString( FirebaseAnalytics.Param.ITEM_CATEGORY, "Ana Paket"); //Ana paket veya ek paket
                product1.putString( FirebaseAnalytics.Param.ITEM_VARIANT, "Bireysel"); //Bireysel veya Kurumsal
                product1.putString( FirebaseAnalytics.Param.ITEM_BRAND, "TV+"); //DSS ürünü
                product1.putDouble( FirebaseAnalytics.Param.PRICE, 12.90); //Aylık ücret
                product1.putString( FirebaseAnalytics.Param.CURRENCY, "TRY" ); //Para birimi
                product1.putLong( FirebaseAnalytics.Param.INDEX, 1 ); // Hangi sırada görüntülendiği
                product1.putLong( FirebaseAnalytics.Param.QUANTITY, 1 );


                ArrayList items = new ArrayList();
                items.add(product1);


                Bundle ecommerceBundle = new Bundle();
                ecommerceBundle.putParcelableArrayList( "items", items );

                // Sepet ile alakalı bilgiler
                ecommerceBundle.putString( FirebaseAnalytics.Param.TRANSACTION_ID, "T12345" ); // Sipariş ID’si
                ecommerceBundle.putString( FirebaseAnalytics.Param.AFFILIATION, "" ); // Boş kalacak
                ecommerceBundle.putDouble( FirebaseAnalytics.Param.VALUE, 37.39 ); // Toplam sepet geliri. Decimal ayracı nokta olacak.
                ecommerceBundle.putDouble( FirebaseAnalytics.Param.TAX, 2.85 ); // Vergi
                ecommerceBundle.putDouble( FirebaseAnalytics.Param.SHIPPING, 5.34 ); // Varsa Kargo
                ecommerceBundle.putString( FirebaseAnalytics.Param.CURRENCY, "TRY" ); // Para birimi TRY/USD/EUR
                ecommerceBundle.putString( FirebaseAnalytics.Param.COUPON, "SUMMER2017" ); // Varsa indirim kuponu yoksa boş gelecek.
                ecommerceBundle.putString("eventCategory","Enhanced Ecommerce");
                ecommerceBundle.putString("eventAction","Transaction");
                ecommerceBundle.putString("eventLabel","Success");
                

                mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, ecommerceBundle );


                Bundle params = new Bundle();

                params.putString("eventCategory", "Functions");
                params.putString("eventAction", "SignInWithGoogle");
                params.putString("eventLabel", "Success");
                mFirebaseAnalytics.logEvent("GAEvent", params);



            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately



                Log.w(TAG, "Google sign in failed", e);

                Bundle params = new Bundle();
                params.putString("eventCategory", "Functions");
                params.putString("eventAction", "SignInWithGoogle");
                params.putString("eventLabel", "Failure");
                mFirebaseAnalytics.logEvent("GAEvent", params);
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent activity2Intent = new Intent(getApplicationContext(), GirisYapildi.class);
                            startActivity(activity2Intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.girisyap_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });
    }


    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }


        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Bundle params = new Bundle();
                            params.putString("eventCategory", "Functions");
                            params.putString("eventAction", "SignIn");
                            params.putString("eventLabel", "Success");
                            mFirebaseAnalytics.logEvent("GAEvent", params);



                            Intent activity2Intent = new Intent(getApplicationContext(), GirisYapildi.class);
                            startActivity(activity2Intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(GirisYap.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            Bundle params = new Bundle();
                            params.putString("eventCategory", "Functions");
                            params.putString("eventAction", "SignIn");
                            params.putString("eventLabel", "Failure");
                            mFirebaseAnalytics.logEvent("GAEvent", params);


                        }


                    }
                });
        // [END sign_in_with_email]
    }



    private boolean validateForm() {
        boolean valid = true;

        String email = eposta.getText().toString();
        if (TextUtils.isEmpty(email)) {
            eposta.setError("Required.");
            valid = false;
        } else {
            eposta.setError(null);
        }

        String password = sifre.getText().toString();
        if (TextUtils.isEmpty(password)) {
            sifre.setError("Required.");
            valid = false;
        } else {
            sifre.setError(null);
        }

        return valid;
    }





}





