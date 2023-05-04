package com.example.testapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Patterns;

public class Common {

    public static boolean emailValidator(String email) {

        // extract the entered data from the EditText
        boolean isValid = !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();

        return isValid;
    }

    public  static ProgressDialog getDialog(Context context){

        ProgressDialog loading = new ProgressDialog(context);
        loading.setMessage("Please wait...");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setIndeterminate(true);

        return  loading;
    }


}
