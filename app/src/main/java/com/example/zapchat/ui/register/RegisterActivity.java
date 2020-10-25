package com.example.zapchat.ui.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zapchat.R;
import com.example.zapchat.ui.data.User;
import com.example.zapchat.ui.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

/**
    Created by Henrique Teixeira, 23 september 2020
*/

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText registerName, registerEmail, registerPassword, registerConfirmPass;
    Button registerButton, btn_selectPhoto;
    String nameUser, emailUser, passUser, cPassUser;
    String uid, username, profileUrl;
    ImageView imagePhoto;
    StorageReference storageReference;
    Uri selectedUri, imgUri, destinationUri;
    User user;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        imagePhoto = findViewById(R.id.imagePhoto);
        btn_selectPhoto = findViewById(R.id.btn_selectPhoto);
        registerName = findViewById(R.id.registerName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPass = findViewById(R.id.registerConfirmPass);
        registerButton = findViewById(R.id.registerButton);

        progressDialog = new ProgressDialog(this);

        imagePhoto.setOnClickListener(view -> selectPhoto());
        btn_selectPhoto.setOnClickListener(view -> selectPhoto());
        registerButton.setOnClickListener(view -> createUser());
    }


    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //uCrop Error
        if (requestCode == UCrop.RESULT_ERROR) {
            Toast.makeText(this, "uCrop error", Toast.LENGTH_SHORT).show();
            return;
        }

        //Set image on activity or cancel crop back to activity
        if (requestCode == UCrop.REQUEST_CROP) {
            if (data==null){
                onStart();
            }else {
                imgUri = UCrop.getOutput(data);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(RegisterActivity.this.getContentResolver(),imgUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imagePhoto.setImageBitmap(bitmap);
            }

        }

        //get and crop image
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (data!=null){
                if (data.getData() != null) {
                    selectedUri = data.getData();
                    File tempCropped = new File(getCacheDir(), "tempImgCropped.png");
                    destinationUri = Uri.fromFile(tempCropped);
                    UCrop.of(selectedUri, destinationUri)
                            .withAspectRatio(1, 1)
                            //.withMaxResultSize(MAX_WIDTH, MAX_HEIGHT)
                            .start(this);
                    onStart();
                }
            }
        }
    }

    //Create user profile
    private void createUser() {
        nameUser = registerName.getText().toString();
        emailUser = registerEmail.getText().toString();
        passUser = registerPassword.getText().toString();
        cPassUser = registerConfirmPass.getText().toString();

        if (nameUser == null || nameUser.isEmpty()){
            Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show();
        }else if (emailUser == null || emailUser.isEmpty() || passUser == null || passUser.isEmpty()){
                Toast.makeText(this, "Email and Password are empty", Toast.LENGTH_SHORT).show();
            }if (nameUser != null || !nameUser.isEmpty() ||
                    emailUser != null || !emailUser.isEmpty() ||
                        passUser != null || !passUser.isEmpty()) {
                    if (cPassUser.equals(passUser)){
                        mAuth.createUserWithEmailAndPassword(emailUser, passUser)
                                .addOnCompleteListener(this, task -> {
                                    if (task.isSuccessful()) {
                                        saveUserFirebase();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Create user fail", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else {
                        Toast.makeText(this, "Your password is different ", Toast.LENGTH_SHORT).show();
                    }
                }
    }

    //Save user profile in firebase
    private void saveUserFirebase() {
        uid = FirebaseAuth.getInstance().getUid();
        username = registerName.getText().toString();
        emailUser = registerEmail.getText().toString();
        if (imgUri != null){
            StorageReference fileRef = storageReference.child("users/" + uid + "/profile.jpg");
            fileRef.putFile(imgUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            profileUrl = uri.toString();
                            user = new User(uid, username, emailUser, profileUrl);
                            FirebaseFirestore.getInstance().collection("users").document(uid).set(user)
                                    .addOnSuccessListener(documentReference -> Log.i("Upload user success", uid))
                                    .addOnFailureListener(e -> Log.i("Upload user fail", e.getMessage(), e));
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.i("Upload image fail", e.getMessage(), e);
                    });
        }else {
            user = new User(uid, username, emailUser, null);

            FirebaseFirestore.getInstance().collection("users").document(uid).set(user)
                    .addOnSuccessListener(documentReference -> Log.i("Upload user success", uid))
                    .addOnFailureListener(e -> Log.i("Upload user fail", e.getMessage(), e));
        }

        Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Back to login activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}