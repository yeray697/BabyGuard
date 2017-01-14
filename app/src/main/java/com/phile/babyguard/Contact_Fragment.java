package com.phile.babyguard;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phile.babyguard.model.NurserySchool;
import com.phile.babyguard.repository.Repository;
import com.phile.babyguard.utils.Utils;


public class Contact_Fragment extends Fragment {

    TextView tvName, tvAddress, tvPhones, tvWeb, tvEmail;

    NurserySchool nurserySchool;

    public static Contact_Fragment newInstance(Bundle args) {
        Contact_Fragment fragment = new Contact_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    public Contact_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        nurserySchool = Repository.getInstance().getNurserySchool();
        tvName = (TextView) view.findViewById(R.id.tvNameContact);
        tvAddress = (TextView) view.findViewById(R.id.tvAddressContact);
        tvPhones = (TextView) view.findViewById(R.id.tvTelephoneContact);
        tvWeb = (TextView) view.findViewById(R.id.tvWebContact);
        tvEmail = (TextView) view.findViewById(R.id.tvEmailContact);

        String telephones = "";
        for (String telephone : nurserySchool.getTelephone()) {
            //telephones += "" + telephone + "\n";
            telephones += "<i>" + telephone + "</i><br>";
        }

        tvName.setText(nurserySchool.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvAddress.setText(Html.fromHtml(getString(R.string.address_contact_text, nurserySchool.getAddress()), Html.FROM_HTML_MODE_LEGACY));
            tvWeb.setText(Html.fromHtml(getString(R.string.web_contact_text, nurserySchool.getWeb()), Html.FROM_HTML_MODE_LEGACY));
            tvEmail.setText(Html.fromHtml(getString(R.string.email_contact_text, nurserySchool.getEmail()), Html.FROM_HTML_MODE_LEGACY));
            tvPhones.setText(Html.fromHtml(telephones, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvAddress.setText(Html.fromHtml(getString(R.string.address_contact_text, nurserySchool.getAddress())));
            tvWeb.setText(Html.fromHtml(getString(R.string.web_contact_text, nurserySchool.getWeb())));
            tvEmail.setText(Html.fromHtml(getString(R.string.email_contact_text, nurserySchool.getEmail())));
            tvPhones.setText(Html.fromHtml(telephones));
        }
        setToolbar(view);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        return view;
    }

    private void setToolbar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbarContact);
        toolbar.setTitle(R.string.item_contacto);
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.mipmap.ic_navigation_drawer);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        Utils.colorizeToolbar(toolbar, getResources().getColor(R.color.toolbar_color), getActivity());
    }
}
