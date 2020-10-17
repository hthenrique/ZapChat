package com.example.zapchat.ui.messages;

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

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.ViewHolder> {

    private ArrayList<String> mMessageList;
    LayoutInflater mInflater;

   /* ChatListAdapter(List<ChatListAdapter> chatList){
        setList(chatList);
    }*/

    public MessagesListAdapter(FragmentActivity activity, ArrayList<String> names) {
        this.mInflater = LayoutInflater.from(activity);
        this.mMessageList = names;
    }

    /*private void setList(List<ChatListAdapter> chatList) {
        mChatList = chatList;
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View noteView = inflater.inflate(R.layout.item_messages, parent, false);
        return new ViewHolder(noteView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String names = mMessageList.get(position);
        holder.usernameMessage.setText(names);
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameMessage;
        ViewHolder(View noteView) {
            super(noteView);
            usernameMessage = noteView.findViewById(R.id.usernameMessage);
        }
    }
}
