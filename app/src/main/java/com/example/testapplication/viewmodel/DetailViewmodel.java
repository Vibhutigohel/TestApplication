package com.example.testapplication.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.testapplication.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailViewmodel extends AndroidViewModel {

    public MutableLiveData<User> sucessLiveData = new MutableLiveData<>();


    public DetailViewmodel(Application application) {
        super(application);

    }

    public void getUserDetail(){


        FirebaseFirestore.getInstance().collection("userDetails")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            String firstName = documentSnapshot.getString("firstName");
            String lastName = documentSnapshot.getString("lastName");
            String gender = documentSnapshot.getString("gender");
            String email = documentSnapshot.getString("email");
            String mobileNumber = documentSnapshot.getString("mobileNumber");
            String dob = documentSnapshot.getString("dob");

            User user = new User(firstName,lastName,gender,email,mobileNumber,dob);

            sucessLiveData.setValue(user);
        });


    }

}
