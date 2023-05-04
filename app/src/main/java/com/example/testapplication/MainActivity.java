package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.viewmodel.LoginViewmodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    TextView tv_sign_up, tv_sign_in;
    EditText et_email, et_pass;
    ImageView iv_pass;
    boolean isPassVisible = false;

    String email, pass;

    ProgressDialog loading;

    LoginViewmodel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(LoginViewmodel.class);

        tv_sign_in = findViewById(R.id.tv_sign_in);
        tv_sign_up = findViewById(R.id.tv_sign_up);
        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        iv_pass = findViewById(R.id.iv_pass);
        loading = Common.getDialog(this);

        listeners();

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

        tv_sign_in.setOnClickListener(view -> {
            login();
        });

        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void listeners() {

        viewModel.sucessLiveData.observe(this, aBoolean -> {
            if(aBoolean == Boolean.TRUE){
                Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                loading.hide();

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }   else{
                Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                loading.hide();
            }
        });

    }

    public void login() {

        email = et_email.getText().toString();
        pass = et_pass.getText().toString();

        if(TextUtils.isEmpty(email)){
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
        }else {
            tv_sign_in.requestFocus();

            loading.show();

            viewModel.login(FirebaseAuth.getInstance(),email,pass);

        }
    }
}