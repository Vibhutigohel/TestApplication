package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailActivity extends AppCompatActivity {

    TextView tv_name, tv_gender, tv_email, tv_mob_number, tv_dob;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        tv_name = findViewById(R.id.tv_name);
        tv_gender = findViewById(R.id.tv_gender);
        tv_email = findViewById(R.id.tv_email);
        tv_mob_number = findViewById(R.id.tv_mob_number);
        tv_dob = findViewById(R.id.tv_dob);


        mFirestore.collection("userDetails").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            String firstName = documentSnapshot.getString("firstName");
            String lastName = documentSnapshot.getString("lastName");
            String gender = documentSnapshot.getString("gender");
            String email = documentSnapshot.getString("email");
            String mobileNumber = documentSnapshot.getString("mobileNumber");
            String dob = documentSnapshot.getString("dob");

            tv_name.setText(firstName + " " + lastName);
            tv_gender.setText(gender);
            tv_email.setText(email);
            tv_mob_number.setText(mobileNumber);
            tv_dob.setText(dob);
        });
    }
}