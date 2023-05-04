package com.example.testapplication.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.testapplication.HomeActivity;
import com.example.testapplication.SignUpActivity;
import com.example.testapplication.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpViewmodel extends AndroidViewModel {

    public MutableLiveData<Boolean> sucessLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> dataAddedLiveData = new MutableLiveData<>();


    public SignUpViewmodel(Application application) {
        super(application);

    }

    public void Signup(FirebaseAuth auth, String email, String pass) {

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sucessLiveData.setValue(true);
//                            progressBar.setVisibility(View.GONE);

                        //                            progressBar.setVisibility(View.GONE);
                    } else {
                        sucessLiveData.setValue(false);

//                            progressBar.setVisibility(View.GONE);
                    }
                });
    }

    public void addDataToFirestore(String firstName, String lastName, String gender, String email, String mobileNumber, String dob) {

        // creating a collection reference
        // for our Firebase Firestore database.
        CollectionReference dbCourses = FirebaseFirestore.getInstance().collection("userDetails");

        // adding our data to our courses object class.
        User courses = new User(firstName, lastName, gender, email, mobileNumber, dob);

        dbCourses.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(courses).addOnSuccessListener(documentReference -> {
            dataAddedLiveData.setValue(true);
        }).addOnFailureListener(e -> dataAddedLiveData.setValue(false));
    }


}
