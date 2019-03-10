package com.example.bhanu.github.repos.ui;

import android.net.Uri;

import androidx.navigation.NavDirections;

public interface OnFragmentInteractionListener {
    void onFragmentInteraction(Uri uri);
    void openloginPage();
    void openUserDetailPage(String id);
    void openPage(NavDirections action);
}
