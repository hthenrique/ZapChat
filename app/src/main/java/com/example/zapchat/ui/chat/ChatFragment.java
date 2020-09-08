package com.example.zapchat.ui.chat;

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

public class ChatFragment extends Fragment {

    private ChatViewModel chatViewModel;
    RecyclerView messageList;
    ChatListAdapter chatListAdapter;
    TextView text_chat;
    FloatingActionButton newChat;
    ArrayList<String> names;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        newChat = root.findViewById(R.id.newChat);
        text_chat = root.findViewById(R.id.text_chat);
        messageList = root.findViewById(R.id.messageList);

        newChat.setOnClickListener(view -> showContacts());

        names = new ArrayList<>();
        names.add("Horse");
        names.add("Cow");
        names.add("Camel");
        names.add("Sheep");
        names.add("Goat");
        names.add("Horse");
        names.add("Cow");
        names.add("Camel");
        names.add("Sheep");
        names.add("Goat");
        names.add("Horse");
        names.add("Cow");
        names.add("Camel");
        names.add("Sheep");
        names.add("Goat");

        setChatList();

        return root;
    }

    private void showContacts() {
        Intent contacts = new Intent(getContext(), ContactsActivity.class);
        startActivity(contacts);
    }

    private void setChatList() {
        if (names != null){
            messageList.setLayoutManager(new LinearLayoutManager(getContext()));
            chatListAdapter = new ChatListAdapter(getActivity(), names);
            messageList.setHasFixedSize(true);
            messageList.setAdapter(chatListAdapter);
        }else {
            chatViewModel.getText().observe(getViewLifecycleOwner(), s -> text_chat.setText(s));
        }
    }
}