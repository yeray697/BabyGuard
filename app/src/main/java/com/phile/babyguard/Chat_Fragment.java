package com.phile.babyguard;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Chat_Fragment extends Fragment {

    public static Chat_Fragment newInstance(Bundle args) {
        Chat_Fragment fragment = new Chat_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    public Chat_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        return view;
    }
}
