package com.example.zapchat.ui.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.zapchat.MainActivity;
import com.example.zapchat.ui.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Utils {
    FirebaseAuth mAuth;

    public void autoLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences( "login" , Context.MODE_PRIVATE);
        String autoEmail = preferences.getString("email", "");
        String autoPassword = preferences.getString("password", "");
        mAuth = FirebaseAuth.getInstance();
        if (preferences.contains("isUserLogin")){
            mAuth.signInWithEmailAndPassword(autoEmail,autoPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
        }
    }

    public void logoutUser(Context context){
        SharedPreferences preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("isUserLogin");
        editor.commit();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
