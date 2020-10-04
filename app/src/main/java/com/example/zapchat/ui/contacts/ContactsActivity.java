package com.example.zapchat.ui.contacts;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchat.R;
import com.example.zapchat.ui.data.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    TextView contactsText;
    RecyclerView contactsList;
    ContactListAdapter contactListAdapter;
    User user;
    ArrayList<DocumentSnapshot> contacts;
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

        Query query = firebaseFirestore.collection("/users");
        FirestoreRecyclerOptions<User> userResponse = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        FirebaseRecyclerAdapter<User, ContactListAdapter.ViewHolder> firebaseRecyclerAdapter;
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, ContactListAdapter.ViewHolder>(userResponse) {
            @Override
            protected void populateViewHolder(ContactListAdapter.ViewHolder viewHolder, User user, int i) {

            }
        };
        /*firebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc: docs){
                            contacts = new ArrayList<DocumentSnapshot>();
                            user = doc.toObject(User.class);
                            contacts.addAll(docs);
                            setContactList();
                        }
                    }
                });*/
                    /*if (task.isSuccessful()){
                        contacts = new ArrayList<>();
                        profilePhoto = new ArrayList<>();
                        for (QueryDocumentSnapshot doc: task.getResult()){
                            Log.d("Test 1", doc.getId() + " => " + doc.getData());
                            user = doc.toObject(User.class);
                            Log.d("Test", user.getUsername() + user.getProfileUrl());

                            setContactList();
                        }
                    }
                });*/
    }


    private void setContactList() {

        if (user != null){
            contactsList.setLayoutManager(new LinearLayoutManager(ContactsActivity.this));
            contactListAdapter = new ContactListAdapter(ContactsActivity.this, contacts);
            contactsList.setHasFixedSize(true);
            contactsList.setAdapter(contactListAdapter);
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