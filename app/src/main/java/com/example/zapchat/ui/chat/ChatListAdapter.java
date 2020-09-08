package com.example.zapchat.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchat.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private ArrayList<String> mChatList;
    LayoutInflater mInflater;

   /* ChatListAdapter(List<ChatListAdapter> chatList){
        setList(chatList);
    }*/

    public ChatListAdapter(FragmentActivity activity, ArrayList<String> names) {
        this.mInflater = LayoutInflater.from(activity);
        this.mChatList = names;
    }

    /*private void setList(List<ChatListAdapter> chatList) {
        mChatList = chatList;
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View noteView = inflater.inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(noteView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String names = mChatList.get(position);
        holder.usernameChat.setText(names);
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameChat;
        ViewHolder(View noteView) {
            super(noteView);

            usernameChat = noteView.findViewById(R.id.usernameChat);
        }
    }
}
