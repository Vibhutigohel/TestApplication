package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.TextView;

import com.example.testapplication.databinding.ActivityDetailBinding;
import com.example.testapplication.databinding.ActivityMainBinding;
import com.example.testapplication.viewmodel.DetailViewmodel;
import com.example.testapplication.viewmodel.HomeViewmodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailActivity extends AppCompatActivity {

    TextView tv_name, tv_gender, tv_email, tv_mob_number, tv_dob;

    DetailViewmodel viewModel;

    ActivityDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        viewModel = ViewModelProviders.of(this).get(DetailViewmodel.class);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail);
        viewModel.getUserDetail();
        listeners();


    }

    private void listeners() {

        viewModel.sucessLiveData.observe(this, user -> {

            binding.tvFirstName.setText(user.getFirstName());
            binding.tvLastName.setText(user.getLastName());
            binding.tvGender.setText(user.getGender());
            binding.tvEmail.setText(user.getEmail());
            binding.tvMobNumber.setText(user.getMobileNumber());
            binding.tvDob.setText(user.getDob());
        });
    }
}