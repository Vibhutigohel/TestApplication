package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.databinding.ActivitySignUpBinding;
import com.example.testapplication.viewmodel.LoginViewmodel;
import com.example.testapplication.viewmodel.SignUpViewmodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    ProgressDialog loading = null;

    String firstName,lastName,pass,cnfmPass,gender,email,mobileNumber,dob;
    RadioButton rb_selected;

    boolean isPassVisible = false;
    boolean isCnfmPassVisible = false;

    SignUpViewmodel viewModel;
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loading = Common.getDialog(this);

        viewModel = ViewModelProviders.of(this).get(SignUpViewmodel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        listeners();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        binding.ivPass.setOnClickListener(view -> {
            if (!isPassVisible) {
                binding.etPass.setTransformationMethod(new HideReturnsTransformationMethod());
                binding.ivPass.setImageDrawable(getResources().getDrawable(R.drawable.pass_hide));
                isPassVisible = true;
            } else {
                binding.etPass.setTransformationMethod(new PasswordTransformationMethod());
                binding.ivPass.setImageDrawable(getResources().getDrawable(R.drawable.pass_see));
                isPassVisible = false;

            }

            binding.etPass.setSelection(binding.etPass.getText().length());
        });

        binding.ivCnfmpass.setOnClickListener(view -> {
            if (!isCnfmPassVisible) {
                binding.etCnfmpass.setTransformationMethod(new HideReturnsTransformationMethod());
                binding.ivCnfmpass.setImageDrawable(getResources().getDrawable(R.drawable.pass_hide));
                isCnfmPassVisible = true;
            } else {
                binding.etCnfmpass.setTransformationMethod(new PasswordTransformationMethod());
                binding.ivCnfmpass.setImageDrawable(getResources().getDrawable(R.drawable.pass_see));
                isCnfmPassVisible = false;

            }

            binding.etCnfmpass.setSelection(binding.etCnfmpass.getText().length());
        });

        binding.etDate.setOnClickListener(view -> new DatePickerDialog(SignUpActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        binding.tvSignIn.setOnClickListener(view -> onBackPressed());

        binding.tvSubmit.setOnClickListener(view -> {
            firstName = binding.etFirstName.getText().toString();
            lastName = binding.etLastName.getText().toString();
            email = binding.etEmail.getText().toString();
            int selectedId = binding.radioGroup.getCheckedRadioButtonId();
            rb_selected = findViewById(selectedId);
            if (rb_selected != null)
                gender = rb_selected.getText().toString();
            mobileNumber = binding.etMobileNumber.getText().toString();
            dob = binding.etDate.getText().toString();
            pass = binding.etPass.getText().toString();
            cnfmPass = binding.etCnfmpass.getText().toString();


            binding.tvSignIn.requestFocus();

            if (TextUtils.isEmpty(firstName)) {
                binding.etFirstName.setError("Please enter First Name");
                binding.etFirstName.requestFocus();
            } else if (TextUtils.isEmpty(lastName)) {
                binding.etLastName.setError("Please enter Last Name");
                binding.etLastName.requestFocus();
            } else if (TextUtils.isEmpty(gender)) {
                Toast.makeText(SignUpActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError("Please enter Email");
                binding.etEmail.requestFocus();
            } else if (!Common.emailValidator(email)) {
                binding.etEmail.setError("Please enter Valid Email");
                binding.etEmail.requestFocus();
            } else if (TextUtils.isEmpty(pass)) {
                binding.etPass.setError("Please enter Password");
                binding.etPass.requestFocus();
            } else if (pass.length() < 7) {
                binding.etPass.setError("Please enter Password Upto 7 letters");
                binding.etPass.requestFocus();
            } else if (TextUtils.isEmpty(cnfmPass)) {
                binding.etCnfmpass.setError("Please enter Confirm Password");
                binding.etCnfmpass.requestFocus();
            } else if (!pass.equals(cnfmPass)) {
                binding.etCnfmpass.setError("Password and Confirm Password must match");
                binding.etCnfmpass.requestFocus();
            } else if (TextUtils.isEmpty(mobileNumber)) {
                binding.etMobileNumber.setError("Please enter Mobile Number");
                binding.etMobileNumber.requestFocus();
            } else if (mobileNumber.length() < 10) {
                binding.etMobileNumber.setError("Please enter Valid Mobile Number");
                binding.etMobileNumber.requestFocus();
            } else if (TextUtils.isEmpty(dob)) {
                binding.etDate.setError("Please enter Date of Birth");
            } else {
                loading.show();
                viewModel.Signup(FirebaseAuth.getInstance(), email, pass);
            }
        });
    }


    private void listeners() {

        viewModel.sucessLiveData.observe(this, aBoolean -> {
            if (aBoolean == Boolean.TRUE) {
                viewModel.addDataToFirestore(firstName, lastName, gender, email, mobileNumber, dob);

            } else {
                Toast.makeText(getApplicationContext(), "User already exists.", Toast.LENGTH_LONG).show();
                loading.hide();
            }
        });

        viewModel.dataAddedLiveData.observe(this, aBoolean -> {
            if (aBoolean == Boolean.TRUE) {
                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                loading.hide();

            } else {
                Toast.makeText(getApplicationContext(), "User already exists.", Toast.LENGTH_LONG).show();
                loading.hide();
            }
        });
    }

    //update date in date formate
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        binding.etDate.setText(dateFormat.format(myCalendar.getTime()));
    }


}