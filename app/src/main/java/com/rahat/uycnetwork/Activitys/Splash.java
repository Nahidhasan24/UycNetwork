package com.rahat.uycnetwork.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.rahat.uycnetwork.R;

public class Splash extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth=FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser()!=null){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finishAffinity();
                }else{
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finishAffinity();
                }
            }
        },2000);

    }
}