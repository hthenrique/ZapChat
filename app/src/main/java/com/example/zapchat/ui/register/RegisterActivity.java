package com.example.zapchat.ui.register;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText registerName, registerEmail, registerPassword, registerConfirmPass;
    Button registerButton, btn_selectPhoto;
    String nameUser, emailUser, passUser, cPassUser;
    String uid, username, profileUrl;
    ImageView imagePhoto;
    StorageReference storageReference;
    Uri selectedUri, croppedUri, imgUri, destinationUri;
    User user;
    static Bitmap bitmap;

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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UCrop.RESULT_ERROR) {
            Toast.makeText(this, "uCrop error", Toast.LENGTH_SHORT).show();
            return;
        }

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

    private void createUser() {
        nameUser = registerName.getText().toString();
        emailUser = registerEmail.getText().toString();
        passUser = registerPassword.getText().toString();
        cPassUser = registerConfirmPass.getText().toString();

        if (nameUser == null || nameUser.isEmpty()){
            Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show();
        }else {
            if (emailUser == null || emailUser.isEmpty() || passUser == null || passUser.isEmpty()){
                Toast.makeText(this, "Email and Password are empty", Toast.LENGTH_SHORT).show();
            }else {
                if (nameUser != null || !nameUser.isEmpty() || emailUser != null || !emailUser.isEmpty() ||
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
        }
    }

    private void saveUserFirebase() {
        if (imgUri != null){
            uid = FirebaseAuth.getInstance().getUid();
            username = registerName.getText().toString();
            emailUser = registerEmail.getText().toString();
            StorageReference fileRef = storageReference.child("users/" + uid + "/profile.jpg");
            fileRef.putFile(imgUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                profileUrl = uri.toString();
                                user = new User(uid, username, emailUser, profileUrl);
                                FirebaseFirestore.getInstance().collection("users")
                                        .add(user)
                                        .addOnSuccessListener(documentReference -> Log.i("Upload user success", documentReference.getId()))
                                        .addOnFailureListener(e -> Log.i("Upload user fail", e.getMessage(), e));
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.i("Upload image fail", e.getMessage(), e);
                    });
        }else {
            uid = FirebaseAuth.getInstance().getUid();
            username = registerName.getText().toString();
            emailUser = registerEmail.getText().toString();
            user = new User(uid, username, emailUser, null);

            FirebaseFirestore.getInstance().collection("users")
                    .add(user)
                    .addOnSuccessListener(documentReference -> Log.i("Upload user success", documentReference.getId()))
                    .addOnFailureListener(e -> Log.i("Upload user fail", e.getMessage(), e));
        }

        Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:break;
        }
        return true;
    }
}