package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;
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
    EditText et_first_name, et_last_name, et_email, et_pass, et_cnfmpass, et_mobile_number, et_date;
    TextView tv_sign_in, tv_submit;
    String firstName = "", lastName = "", gender = "", email = "", mobileNumber = "", dob = "", pass = "", cnfmPass = "";
    RadioButton rb_male, rb_female, rb_other, rb_selected;
    RadioGroup radioGroup;
    ProgressDialog loading = null;

    ImageView iv_pass, iv_cnfmpass;

    boolean isPassVisible = false;
    boolean isCnfmPassVisible = false;

    SignUpViewmodel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loading = Common.getDialog(this);

        viewModel = ViewModelProviders.of(this).get(SignUpViewmodel.class);

        et_date = findViewById(R.id.et_date);
        tv_sign_in = findViewById(R.id.tv_sign_in);
        tv_submit = findViewById(R.id.tv_submit);
        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_email = findViewById(R.id.et_email);
        et_mobile_number = findViewById(R.id.et_mobile_number);
        et_pass = findViewById(R.id.et_pass);
        et_cnfmpass = findViewById(R.id.et_cnfmpass);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        rb_other = findViewById(R.id.rb_other);
        radioGroup = findViewById(R.id.radioGroup);
        iv_pass = findViewById(R.id.iv_pass);
        iv_cnfmpass = findViewById(R.id.iv_cnfmpass);
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

        iv_pass.setOnClickListener(view -> {
            if (!isPassVisible) {
                et_pass.setTransformationMethod(new HideReturnsTransformationMethod());
                iv_pass.setImageDrawable(getResources().getDrawable(R.drawable.pass_hide));
                isPassVisible = true;
            } else {
                et_pass.setTransformationMethod(new PasswordTransformationMethod());
                iv_pass.setImageDrawable(getResources().getDrawable(R.drawable.pass_see));
                isPassVisible = false;

            }

            et_pass.setSelection(et_pass.getText().length());
        });

        iv_cnfmpass.setOnClickListener(view -> {
            if (!isCnfmPassVisible) {
                et_cnfmpass.setTransformationMethod(new HideReturnsTransformationMethod());
                iv_cnfmpass.setImageDrawable(getResources().getDrawable(R.drawable.pass_hide));
                isCnfmPassVisible = true;
            } else {
                et_cnfmpass.setTransformationMethod(new PasswordTransformationMethod());
                iv_cnfmpass.setImageDrawable(getResources().getDrawable(R.drawable.pass_see));
                isCnfmPassVisible = false;

            }

            et_cnfmpass.setSelection(et_pass.getText().length());
        });

        et_date.setOnClickListener(view -> new DatePickerDialog(SignUpActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        tv_sign_in.setOnClickListener(view -> onBackPressed());
        tv_submit.setOnClickListener(view -> {
            firstName = et_first_name.getText().toString();
            lastName = et_last_name.getText().toString();
            email = et_email.getText().toString();
            int selectedId = radioGroup.getCheckedRadioButtonId();
            rb_selected = findViewById(selectedId);
            if (rb_selected != null)
                gender = rb_selected.getText().toString();
            mobileNumber = et_mobile_number.getText().toString();
            dob = et_date.getText().toString();
            pass = et_pass.getText().toString();
            cnfmPass = et_cnfmpass.getText().toString();


            tv_sign_in.requestFocus();

            if (TextUtils.isEmpty(firstName)) {
                et_first_name.setError("Please enter First Name");
                et_first_name.requestFocus();
            } else if (TextUtils.isEmpty(lastName)) {
                et_last_name.setError("Please enter Last Name");
                et_last_name.requestFocus();
            } else if (TextUtils.isEmpty(gender)) {
                Toast.makeText(SignUpActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(email)) {
                et_email.setError("Please enter Email");
                et_email.requestFocus();
            } else if (!Common.emailValidator(email)) {
                et_email.setError("Please enter Valid Email");
                et_email.requestFocus();
            } else if (TextUtils.isEmpty(pass)) {
                et_pass.setError("Please enter Password");
                et_pass.requestFocus();
            } else if (pass.length() < 7) {
                et_pass.setError("Please enter Password Upto 7 letters");
                et_pass.requestFocus();
            } else if (TextUtils.isEmpty(cnfmPass)) {
                et_cnfmpass.setError("Please enter Confirm Password");
                et_cnfmpass.requestFocus();
            } else if (!pass.equals(cnfmPass)) {
                et_cnfmpass.setError("Password and Confirm Password must match");
                et_cnfmpass.requestFocus();
            } else if (TextUtils.isEmpty(mobileNumber)) {
                et_mobile_number.setError("Please enter Mobile Number");
                et_mobile_number.requestFocus();
            } else if (mobileNumber.length() < 10) {
                et_mobile_number.setError("Please enter Valid Mobile Number");
                et_mobile_number.requestFocus();
            } else if (TextUtils.isEmpty(dob)) {
                et_date.setError("Please enter Date of Birth");
            } else {
                loading.show();
                viewModel.Signup(FirebaseAuth.getInstance(), email, pass);
            }
        });
    }


    private void listeners() {

        viewModel.sucessLiveData.observe(this, aBoolean -> {
            if (aBoolean == Boolean.TRUE) {
                viewModel.addDataToFirestore(firstName,lastName,gender,email,mobileNumber,dob);

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

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        et_date.setText(dateFormat.format(myCalendar.getTime()));
    }


}