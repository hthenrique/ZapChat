package com.example.zapchat.interfaces.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.zapchat.R;
import com.example.zapchat.interfaces.ui.login.LoginActivity;

public class SettingsFragment extends Fragment {

    ListView settingsList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsList = root.findViewById(R.id.settingsList);
        String[] options = new String[]{
                "Dark Mode", "Logout"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, options);
        settingsList.setAdapter(adapter);
        settingsList.setOnItemClickListener((parent, view, position, id) -> {
            if (position==1){
                logoutUser();
            }
        });

        return root;
    }

    private void logoutUser() {
            SharedPreferences preferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("isUserLogin");
            editor.commit();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
    }
}
    