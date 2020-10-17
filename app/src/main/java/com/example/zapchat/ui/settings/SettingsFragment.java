package com.example.zapchat.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.zapchat.R;
import com.example.zapchat.ui.util.Utils;

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
                new Utils().logoutUser(getContext());
            }
        });

        return root;
    }
}
    