package com.phile.babyguard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Profile_Fragment extends Fragment {

    public static Profile_Fragment newInstance(Bundle args) {
        Profile_Fragment fragment = new Profile_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    public Profile_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        return view;
    }
}
