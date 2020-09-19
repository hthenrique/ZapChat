package com.example.zapchat.ui.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchat.R;
import com.example.zapchat.ui.data.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    ArrayList<String> mContactList;
    ArrayList<String> profilePhotoList;
    LayoutInflater mInflater;
    Context context;
    User user;
    //List<ContactListAdapter>mContactList;
    public ContactListAdapter(ContactsActivity contactsLists, ArrayList<String> names, ArrayList<String> profilePhoto){
        this.mInflater = LayoutInflater.from(contactsLists);
        this.mContactList = names;
        this.profilePhotoList = profilePhoto;
        //setList(contactList);
    }

    /*private void setList(List<ContactListAdapter> contactList) {
        mContactList = contactList;
    }*/

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
        String contacts = mContactList.get(position);
        String photo = profilePhotoList.get(position);
        Picasso.get()
                .load(photo)
                //.placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.contactPhoto);
        holder.usernameCont.setText(contacts);
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
