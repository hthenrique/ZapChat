package com.example.zapchat.ui.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchat.R;
import com.example.zapchat.ui.data.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    ArrayList<DocumentSnapshot> mContactList;
    ArrayList<String> profilePhotoList;
    LayoutInflater mInflater;
    Context context;
    User user;

    public ContactListAdapter(ContactsActivity contactsLists, ArrayList<DocumentSnapshot> user){
        this.mInflater = LayoutInflater.from(contactsLists);
        this.mContactList = user;
        //this.profilePhotoList = profilePhoto;
    }

    @NonNull
    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View noteView = inflater.inflate(R.layout.contact_item, parent, false);
        return new ContactListAdapter.ViewHolder(noteView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //String photo = user.getProfileUrl();
        /*DocumentSnapshot contacts = mContactList.get(position);
        Picasso.get()
                .load(String.valueOf(contacts))
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.contactPhoto);
        holder.usernameCont.setText(user.getUsername());*/
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameCont;
        ImageView contactPhoto;
        public ViewHolder(@NonNull View noteView) {
            super(noteView);
            usernameCont = noteView.findViewById(R.id.contactName);
            contactPhoto = noteView.findViewById(R.id.contactPhoto);
        }
    }
}
