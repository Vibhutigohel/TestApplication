package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.databinding.ActivityHomeBinding;
import com.example.testapplication.viewmodel.HomeViewmodel;
import com.example.testapplication.viewmodel.LoginViewmodel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    HomeViewmodel viewModel;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        viewModel = ViewModelProviders.of(this).get(HomeViewmodel.class);
        viewModel.getUserName();

        listeners();
        binding.tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.ivDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });


    }

    private void listeners() {

        viewModel.sucessLiveData.observe(this, user_name -> {
            binding.tvUserName.setText(user_name);
        });
    }
}