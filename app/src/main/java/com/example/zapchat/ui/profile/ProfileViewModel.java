package com.example.zapchat.ui.profile;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zapchat.ui.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> username, photoUri;
    FirebaseFirestore firebaseFirestore;

    public ProfileViewModel(){
        username = new MutableLiveData<>();
        photoUri = new MutableLiveData<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                String uid = profile.getUid();
                firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference docRef = firebaseFirestore.collection("users").document(uid);
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("user", "DocumentSnapshot data: " + document.getData());
                            String name = document.getString("username");
                            String photo = document.getString("profileUrl");
                            if (photo==null){
                                username.postValue(name);
                            }else if (photo!=null){
                                username.postValue(name);
                                photoUri.postValue(photo);
                            }
                        } else {
                            Log.d("user", "No such document");
                        }
                    } else {
                        Log.d("user", "get failed with ", task.getException());
                    }
                });
            }

        }else {
            username.setValue("Your name");
        }

    }

    public LiveData<String> getText(){
        return username;
    }

    public LiveData<String> getImage() {
        return photoUri;
    }
}
