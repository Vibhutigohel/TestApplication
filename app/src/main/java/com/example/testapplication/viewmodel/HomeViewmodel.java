package com.example.testapplication.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeViewmodel extends AndroidViewModel {

    public MutableLiveData<String> sucessLiveData = new MutableLiveData<>();

    public String user_name;

    public HomeViewmodel(Application application) {
        super(application);

    }

    public void getUserName(){

        FirebaseFirestore.getInstance().collection("userDetails")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
             user_name = documentSnapshot.getString("firstName");

            sucessLiveData.setValue(user_name);
        });


    }

}
