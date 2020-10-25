package com.example.zapchat.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.zapchat.R;
import com.example.zapchat.ui.data.User;
import com.example.zapchat.ui.register.RegisterActivity;
import com.example.zapchat.ui.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    TextView userName, deleteAccount;
    ImageView profilePhoto;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    Uri imgUri, selectedUri, destinationUri;
    StorageReference storageReference;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profilePhoto = root.findViewById(R.id.userPhoto);
        userName = root.findViewById(R.id.userName);
        deleteAccount = root.findViewById(R.id.deleteAccount);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        deleteAccount.setOnClickListener(view -> deleteUser());
        profilePhoto.setOnClickListener(v -> selectPhoto());
        loadUser();
        return root;
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //uCrop Error
        if (requestCode == UCrop.RESULT_ERROR) {
            Toast.makeText(getContext(), "uCrop error", Toast.LENGTH_SHORT).show();
            return;
        }

        //Set image on activity or cancel crop back to activity
        if (requestCode == UCrop.REQUEST_CROP) {
            String uid = FirebaseAuth.getInstance().getUid();
            if (data==null){
                onStart();
            }else {
                imgUri = UCrop.getOutput(data);
                StorageReference fileRef = storageReference.child("users/" + uid + "/profile.jpg");
                DocumentReference docRef = firebaseFirestore.collection("cities").document("DC");
                fileRef.putFile(imgUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String profileUrl = uri.toString();
                    docRef.update("profileUrl", profileUrl);
                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                }));
                loadUser();
            }
        }

        //get and crop image
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (data!=null){
                if (data.getData() != null) {
                    selectedUri = data.getData();
                    File tempCropped = new File(getActivity().getCacheDir(), "tempImgCropped.png");
                    destinationUri = Uri.fromFile(tempCropped);
                    UCrop.of(selectedUri, destinationUri)
                            .withAspectRatio(1, 1)
                            //.withMaxResultSize(MAX_WIDTH, MAX_HEIGHT)
                            .start(getActivity());
                }
            }
        }
    }

    private void updateProfile(Uri imgUri) {

        File tempCropped = new File(getActivity().getCacheDir(), "tempImgCropped.png");
        destinationUri = Uri.fromFile(tempCropped);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (imgUri!=null){

            /*UserProfileChangeRequest updateProfile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(destinationUri.toString())).build();
            currentUser.updateProfile(updateProfile)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Photo updated", Toast.LENGTH_SHORT).show();
                        }
                    });*/



            /*StorageReference fileRef = storageReference.child("users/" + uid + "/profile.jpg");
            fileRef.putFile(imgUri)
                    .addOnSuccessListener(taskSnapshot -> {
                                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        String profileUrl = uri.toString();
                                        User user = new User(uid, null, null, profileUrl);
                                        FirebaseFirestore.getInstance().collection("users").document(uid).set(user)
                                                .addOnSuccessListener(documentReference -> Log.i("Upload user success", uid))
                                                .addOnFailureListener(e -> Log.i("Upload user fail", e.getMessage(), e));

                                    });
                                })
                                .addOnFailureListener(e -> {
                                    Log.i("Upload image fail", e.getMessage(), e);
                                });*/
        }
    }

    private void loadUser() {
        if (profileViewModel.getImage()!=null){
            profileViewModel.getImage().observe(getViewLifecycleOwner(),
                    image -> Picasso.get().load(image).into(profilePhoto));
            profileViewModel.getText().observe(getViewLifecycleOwner(), s -> userName.setText(s));
        }else {
            Picasso.get().load(R.drawable.ic_baseline_person_24).into(profilePhoto);
            profileViewModel.getText().observe(getViewLifecycleOwner(), s -> userName.setText(s));
        }
    }

    private void deleteUser() {
        AlertDialog.Builder chooseDelete = new AlertDialog.Builder(getContext());

        chooseDelete.setTitle("Delete account");
        chooseDelete.setMessage("You have sure if you want delete this account");

        chooseDelete.setPositiveButton("Yes", (dialogInterface, i) -> {
            String userId = firebaseUser.getUid();
            firebaseFirestore.collection("users").document(userId)
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("Delete User Document", "User account deleted."))
                    .addOnFailureListener(e -> Log.d("Delete User Document", "User account delete fail"));
            storageReference.child("users/" + userId + "/profile.jpg")
                    .delete();
            firebaseUser.delete()
                    .addOnCompleteListener(task -> Log.d("Delete User", "User account deleted."));
            new Utils().logoutUser(getContext());
        });
        chooseDelete.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog alertDialog = chooseDelete.create();
        alertDialog.show();
    }

}
