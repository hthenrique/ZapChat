package com.example.zapchat.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.zapchat.R;
import com.example.zapchat.ui.util.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    TextView userName, deleteAccount;
    ImageView profilePhoto;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profilePhoto = root.findViewById(R.id.userPhoto);
        userName = root.findViewById(R.id.userName);
        deleteAccount = root.findViewById(R.id.deleteAccount);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        loadUser();
        deleteAccount.setOnClickListener(view -> deleteUser());
        return root;
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
            firebaseUser.delete()
                    .addOnCompleteListener(task -> Log.d("Delete User", "User account deleted."));
            new Utils().logoutUser(getContext());
        });
        chooseDelete.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog alertDialog = chooseDelete.create();
        alertDialog.show();
    }

    /*private void logoutUser() {
        SharedPreferences preferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("isUserLogin");
        editor.commit();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/
}
