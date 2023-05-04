package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.viewmodel.HomeViewmodel;
import com.example.testapplication.viewmodel.LoginViewmodel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    ImageView iv_detail;
    TextView tv_user_name, tv_logout;

    HomeViewmodel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        iv_detail = findViewById(R.id.iv_detail);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_logout = findViewById(R.id.tv_logout);

        viewModel = ViewModelProviders.of(this).get(HomeViewmodel.class);
        viewModel.getUserName();

        listeners();

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        iv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });


    }

    private void listeners() {

        viewModel.sucessLiveData.observe(this, user_name -> {
            tv_user_name.setText(user_name);
        });
    }
}