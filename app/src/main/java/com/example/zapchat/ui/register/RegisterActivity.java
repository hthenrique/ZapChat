package com.example.zapchat.ui.register;

import android.content.Intent;
import android.graphics.Bitmap;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zapchat.R;
import com.example.zapchat.ui.data.User;
import com.example.zapchat.ui.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import io.grpc.Context;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText registerName, registerEmail, registerPassword, registerConfirmPass;
    Button registerButton, btn_selectPhoto;
    String nameUser, emailUser, passUser, cPassUser;
    String uid, username, profileUrl;
    ImageView imagePhoto;
    Uri selectedUri;
    User user;
    static Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        imagePhoto = findViewById(R.id.imagePhoto);
        btn_selectPhoto = findViewById(R.id.btn_selectPhoto);
        registerName = findViewById(R.id.registerName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPass = findViewById(R.id.registerConfirmPass);
        registerButton = findViewById(R.id.registerButton);

        imagePhoto.setOnClickListener(view -> selectPhoto());
        btn_selectPhoto.setOnClickListener(view -> selectPhoto());
        registerButton.setOnClickListener(view -> createUser());
    }


    private void selectPhoto() {
        onStart();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 0:
                if (data!=null){
                    if (data.getData()!=null){
                        selectedUri = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedUri);
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            Bitmap bitmapRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            imagePhoto.setImageBitmap(bitmapRotated);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:break;
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
        String fileName = UUID.randomUUID().toString();
        if (selectedUri != null){
            final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + fileName);
            ref.putFile(selectedUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        ref.getDownloadUrl().addOnSuccessListener(uri -> Log.i("Test", uri.toString()));

                        uid = FirebaseAuth.getInstance().getUid();
                        username = registerName.getText().toString();
                        profileUrl = selectedUri.toString();
                        user = new User(uid, username, profileUrl);

                        FirebaseFirestore.getInstance().collection("users")
                                .add(user)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.i("Test", documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("test fail 2", e.getMessage(), e);
                                    }
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.i("test fail", e.getMessage(), e);
                    });
        }else {
            uid = FirebaseAuth.getInstance().getUid();
            username = registerName.getText().toString();
            profileUrl = null;
            user = new User(uid, username,profileUrl);

            FirebaseFirestore.getInstance().collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.i("Test", documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("test fail 2", e.getMessage(), e);
                        }
                    });
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