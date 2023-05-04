package com.example.testapplication.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testapplication.HomeActivity;
import com.example.testapplication.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginViewmodel extends AndroidViewModel {

    public MutableLiveData<Boolean> sucessLiveData = new MutableLiveData<>();


    public LoginViewmodel(Application application) {
        super(application);

    }

    //login method using firebase authentication
    public void login(FirebaseAuth auth, String email, String pwd){

        auth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sucessLiveData.setValue(true);
                        }
                        else {
                            sucessLiveData.setValue(false);
                        }
                    }
                });

    }

}
