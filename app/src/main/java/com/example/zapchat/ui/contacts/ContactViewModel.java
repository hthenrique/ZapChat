package com.example.zapchat.ui.contacts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContactViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ContactViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("No Contacts");
    }

    public LiveData<String> getText(){
        return mText;
    }
}
