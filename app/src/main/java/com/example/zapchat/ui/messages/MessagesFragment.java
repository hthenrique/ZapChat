package com.example.zapchat.ui.messages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchat.R;
import com.example.zapchat.ui.contacts.ContactsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MessagesFragment extends Fragment {

    private MessagesViewModel messagesViewModel;
    RecyclerView messageList;
    MessagesListAdapter messagesListAdapter;
    TextView text_messages;
    FloatingActionButton newMessage;
    ArrayList<String> names;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        messagesViewModel = ViewModelProviders.of(this).get(MessagesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_messages, container, false);

        newMessage = root.findViewById(R.id.newMessage);
        text_messages = root.findViewById(R.id.text_messages);
        messageList = root.findViewById(R.id.messageList);

        newMessage.setOnClickListener(view -> showContacts());

        names = new ArrayList<>();

        setMessagesList();

        return root;
    }

    private void showContacts() {
        Intent contacts = new Intent(getContext(), ContactsActivity.class);
        startActivity(contacts);
    }

    private void setMessagesList() {
        if (names != null){
            messageList.setLayoutManager(new LinearLayoutManager(getContext()));
            messagesListAdapter = new MessagesListAdapter(getActivity(), names);
            messageList.setHasFixedSize(true);
            messageList.setAdapter(messagesListAdapter);
        }else {
            messagesViewModel.getText().observe(getViewLifecycleOwner(), s -> text_messages.setText(s));
        }
    }
}