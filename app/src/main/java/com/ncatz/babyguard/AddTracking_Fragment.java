package com.ncatz.babyguard;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.model.PushNotification;
import com.ncatz.babyguard.model.TrackingKid;
import com.ncatz.babyguard.repository.Repository;

import java.util.Calendar;

/**
 * Created by yeray697 on 10/06/17.
 */

public class AddTracking_Fragment extends Fragment {
    public static final String TRACKING_KEY = "tracking";
    public static final String KID_ID = "kidId";
    public static final String DEVICE_ID_KEY = "deviceId";

    private EditText etTitle;
    private EditText etDescription;
    private Spinner spType;
    private Button btSubmit;
    private Button btCancel;
    private ImageView backButton;
    private TextView tvToolbar;

    private TrackingKid trackingKid;
    private boolean editMode;
    private String kidId;
    private String deviceId;
    private Context context;

    public static AddTracking_Fragment newInstance(Bundle args) {
        AddTracking_Fragment fragment = new AddTracking_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    public AddTracking_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context = getContext();
        } else {
            context = getActivity();
        }
        Bundle args = getArguments();
        trackingKid = args.getParcelable(TRACKING_KEY);
        editMode = (trackingKid != null);
        kidId = args.getString(KID_ID);
        deviceId = args.getString(DEVICE_ID_KEY);
        View view = inflater.inflate(R.layout.fragment_add_tracking, container, false);
        tvToolbar = (TextView) view.findViewById(R.id.tvToolbar_AddTracking);
        backButton = (ImageView) view.findViewById(R.id.backButton_AddTracking);
        etTitle = (EditText) view.findViewById(R.id.etTitle_AddTracking);
        etDescription = (EditText) view.findViewById(R.id.etDescription_AddTracking);
        spType = (Spinner) view.findViewById(R.id.spType_AddTracking);
        btCancel = (Button) view.findViewById(R.id.btCancel_AddTracking);
        btSubmit = (Button) view.findViewById(R.id.btSubmit_AddTracking);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        if (editMode) {
            tvToolbar.setText("Editar");
            btSubmit.setText("Editar");
            etTitle.setText(trackingKid.getTitle());
            etDescription.setText(trackingKid.getDescription());
            spType.setSelection(trackingKid.getType() - 1);
        } else {
            tvToolbar.setText("Añadir");
            btSubmit.setText("Añadir");
        }
        return view;
    }

    private void submit() {

        String title = etTitle.getText().toString(),
                description = etDescription.getText().toString(),
                datetime;
        Integer type = spType.getSelectedItemPosition() + 1;
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            Toast.makeText(context, "No has rellenado todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            Calendar dateParsed = Calendar.getInstance();
            datetime = String.valueOf(dateParsed.getTime().getTime());
            String id = "";
            if (editMode)
                id = trackingKid.getId();
            trackingKid = new TrackingKid("", title, datetime ,TrackingKid.parseIntToType(type),description);
            PushNotification notification = new PushNotification();
            if (editMode) {
                trackingKid.setId(id);
                FirebaseManager.getInstance().updateTracking(kidId,trackingKid);
                notification.setType(PushNotification.TYPE_TRACKING_EDIT);
            } else {
                trackingKid = FirebaseManager.getInstance().addTracking(kidId,trackingKid);
                notification.setType(PushNotification.TYPE_TRACKING_ADD);
            }
            notification.setTrackingKid(trackingKid);
            notification.setToUser(kidId);
            notification.setFromUser(Repository.getInstance().getUser().getId());
            notification.pushNotification(deviceId);
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbar();
        Babyguard_Application.setCurrentActivity("AddTracking_Fragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        Babyguard_Application.setCurrentActivity("");
    }

    private void setToolbar() {
        ((Home_Teacher_Activity)getActivity()).getSupportActionBar().hide();
        ((Home_Teacher_Activity)getActivity()).setNavigationBottomBarHide(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((Babyguard_Application)context.getApplicationContext()).removeChatListener();
        ((Home_Teacher_Activity) getActivity()).getSupportActionBar().show();
        ((Home_Teacher_Activity)getActivity()).setNavigationBottomBarHide(false);
    }
}
