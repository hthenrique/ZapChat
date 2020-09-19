package com.example.zapchat.ui.contacts;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchat.R;
import com.example.zapchat.ui.data.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    TextView contactsText;
    RecyclerView contactsList;
    ContactListAdapter contactListAdapter;
    User user;
    ArrayList<String> contacts;
    ArrayList<String> profilePhoto;
    FirebaseFirestore firebaseFirestore;
    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        contactsText = findViewById(R.id.contactsText);
        contactsList = findViewById(R.id.contactsList);

        fetchUsers();
    }

    private void fetchUsers() {
        FirebaseFirestore.getInstance().collection("/users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        contacts = new ArrayList<>();
                        profilePhoto = new ArrayList<>();
                        for (QueryDocumentSnapshot doc: task.getResult()){
                            Log.d("Test 1", doc.getId() + " => " + doc.getData());
                            user = doc.toObject(User.class);
                            Log.d("Test", user.getUsername() + user.getProfileUrl());

                            setContactList();
                        }
                    }
                });
    }

    private void setContactList() {

        if (contacts != null){
            contacts.add(user.getUsername());
            profilePhoto.add(user.getProfileUrl());
            contactsList.setLayoutManager(new LinearLayoutManager(ContactsActivity.this));
            contactListAdapter = new ContactListAdapter(ContactsActivity.this, contacts, profilePhoto);
            contactsList.setHasFixedSize(true);
            contactsList.setAdapter(contactListAdapter);
            Collections.sort(contacts);
        }else {
            contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
            contactViewModel.getText().observe(this, s -> contactsText.setText(s));
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            default:break;
        }
        return true;
    }
}