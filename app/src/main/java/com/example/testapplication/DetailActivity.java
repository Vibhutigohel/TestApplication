package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.TextView;

import com.example.testapplication.viewmodel.DetailViewmodel;
import com.example.testapplication.viewmodel.HomeViewmodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailActivity extends AppCompatActivity {

    TextView tv_name, tv_gender, tv_email, tv_mob_number, tv_dob;

    DetailViewmodel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        viewModel = ViewModelProviders.of(this).get(DetailViewmodel.class);

        tv_name = findViewById(R.id.tv_name);
        tv_gender = findViewById(R.id.tv_gender);
        tv_email = findViewById(R.id.tv_email);
        tv_mob_number = findViewById(R.id.tv_mob_number);
        tv_dob = findViewById(R.id.tv_dob);
        viewModel.getUserDetail();
        listeners();


    }

    private void listeners() {

        viewModel.sucessLiveData.observe(this, user -> {

            tv_name.setText(user.getFirstName() + " " + user.getLastName());
            tv_gender.setText(user.getGender());
            tv_email.setText(user.getEmail());
            tv_mob_number.setText(user.getMobileNumber());
            tv_dob.setText(user.getDob());
        });
    }
}