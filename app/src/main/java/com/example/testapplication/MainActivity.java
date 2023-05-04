package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
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

import com.example.testapplication.databinding.ActivityMainBinding;
import com.example.testapplication.viewmodel.LoginViewmodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    boolean isPassVisible = false;

    ActivityMainBinding binding;
    String email, pass;

    ProgressDialog loading;

    LoginViewmodel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(LoginViewmodel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        loading = Common.getDialog(this);

        listeners();

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

        binding.tvSignIn.setOnClickListener(view -> {
            login();
        });

        binding.tvSignUp.setOnClickListener(new View.OnClickListener() {
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

        email = binding.etEmail.getText().toString();
        pass = binding.etPass.getText().toString();

        if(TextUtils.isEmpty(email)){
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
        }else {
            binding.tvSignIn.requestFocus();

            loading.show();

            viewModel.login(FirebaseAuth.getInstance(),email,pass);

        }
    }
}