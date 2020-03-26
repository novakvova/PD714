package com.example.begin;

import androidx.fragment.app.Fragment;

public interface ConnectionInternetError {
    void navigateErrorPage(Fragment callbackfragment, boolean addToBackstack);
    void refreshCurrentErrorPage();
}
