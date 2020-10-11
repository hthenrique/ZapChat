package com.example.zapchat.ui.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.zapchat.ui.ui.login.LoginActivity;

public class Utils {

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
