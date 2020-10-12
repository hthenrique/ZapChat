package com.example.zapchat.ui.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zapchat.MainActivity;
import com.example.zapchat.R;
import com.example.zapchat.ui.register.RegisterActivity;
import com.example.zapchat.ui.util.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String email;
    String password;
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    TextView registerLink;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        registerLink = findViewById(R.id.registerLink);
        loginButton = findViewById(R.id.login);

        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();

        //onStart();
        new Utils().autoLogin(getBaseContext());

        loginButton.setOnClickListener(v -> {
            loginUser();
        });

        //Register user
        registerLink.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.i("user", currentUser.getDisplayName());
        }
        if (currentUser == null){
        }
    }*/

    private void loginUser() {
        email = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.i("signInWithEmail:success", email);
                        SharedPreferences preferences1 = getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences1.edit();
                        editor.putBoolean("isUserLogin", true);
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.apply();
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.i( "signInWithEmail:failure", String.valueOf(task.getException()));
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                });
    }

}