package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Time to launch the another activity
        int TIME_OUT = 2000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i;
                if(currentUser == null) {
                    i = new Intent(SplashActivity.this, MainActivity.class);
                }else {
                    i = new Intent(SplashActivity.this, HomeActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, TIME_OUT);

    }
}