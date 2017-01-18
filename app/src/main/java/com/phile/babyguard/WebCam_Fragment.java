package com.phile.babyguard;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class WebCam_Fragment extends Fragment {

    public static WebCam_Fragment newInstance(Bundle args) {
        WebCam_Fragment fragment = new WebCam_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    public WebCam_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webcam, container, false);
        return view;
    }
}
