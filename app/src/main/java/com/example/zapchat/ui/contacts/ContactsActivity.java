package com.example.zapchat.ui.contacts;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchat.R;
import com.example.zapchat.ui.data.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    TextView contactsText;
    RecyclerView contactsList;
    ContactListAdapter contactListAdapter;
    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        contactsText = findViewById(R.id.contactsText);
        contactsList = findViewById(R.id.contactsList);
        contactsList.setHasFixedSize(true);

        fetchUsers();
        //setContactList();

    }

    private void fetchUsers() {
        FirebaseFirestore.getInstance().collection("/users/")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null){
                            Log.e("test",e.getMessage(), e);
                            return;
                        }
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc: documents){
                            User user = doc.toObject(User.class);
                            Log.d("Test", user.getUsername());
                        }
                    }
                });
    }

    private void setContactList() {
        if (contactListAdapter != null){
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