package com.ncatz.babyguard;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.model.NurseryClass;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yeray697 on 8/06/17.
 */

public class AddEvent_Fragment extends Fragment {

    public static final String EVENT_KEY = "event";
    public static final String CLASS_ID_KEY = "classId";

    private ImageView backButton;
    private EditText etTitle;
    private EditText etDescription;
    private TextView tvDate;
    private TextView tvSelectClass;
    private LinearLayout rlClassesList;
    private Button btSubmit;
    private Button btCancel;
    private TextView tvToolbar;

    private ArrayList<CheckBox> classesCheckbox;
    private boolean editMode;
    private DiaryCalendarEvent event;
    private String eventClassId;
    SimpleDateFormat sdf;

    public static AddEvent_Fragment newInstance(Bundle args) {
        AddEvent_Fragment fragment = new AddEvent_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    public AddEvent_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            editMode = true;
            event = args.getParcelable(EVENT_KEY);
            eventClassId = args.getString(CLASS_ID_KEY);
        } else {
            editMode = false;
        }
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        tvToolbar = (TextView)  view.findViewById(R.id.tvToolbar_AddEvent);
        etTitle = (EditText) view.findViewById(R.id.etTitle_AddEvent);
        etDescription = (EditText) view.findViewById(R.id.etDescription_AddEvent);
        tvDate = (TextView) view.findViewById(R.id.tvDate2_AddEvent);
        rlClassesList = (LinearLayout) view.findViewById(R.id.rlClassesList_AddEvent);
        backButton = (ImageView) view.findViewById(R.id.backButton_AddEvent);
        tvSelectClass = (TextView) view.findViewById(R.id.tvSelectClass_AddEvent);
        btCancel = (Button) view.findViewById(R.id.btCancel_AddEvent);
        btSubmit = (Button) view.findViewById(R.id.btSubmit_AddEvent);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });
        loadClassesCheckbox();
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
            etTitle.setText(event.getTitle());
            etDescription.setText(event.getDescription());
            tvDate.setText(event.getDate());
            tvToolbar.setText("Editar");
            btSubmit.setText("Editar");
            tvSelectClass.setVisibility(View.GONE);
            rlClassesList.setVisibility(View.GONE);
        } else {
            tvToolbar.setText("Añadir");
            btSubmit.setText("Añadir");
            tvSelectClass.setVisibility(View.VISIBLE);
            rlClassesList.setVisibility(View.VISIBLE);
        }
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        return view;
    }

    private void submit() {
        String title = etTitle.getText().toString(),
                description = etDescription.getText().toString(),
                date = tvDate.getText().toString();
        ArrayList<String> classesSelected = new ArrayList<>();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || date.equals("Select")) {
            Toast.makeText(getContext(), "No has rellenado todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            if (!editMode) {
                for (CheckBox checkBox : classesCheckbox) {
                    if (checkBox.isChecked())
                        classesSelected.add((String) checkBox.getTag());
                }
            }
            if (!editMode && classesSelected.size() == 0) {
                Toast.makeText(getContext(), "Selecciona al menos una clase", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Calendar dateParsed = Calendar.getInstance();
                    dateParsed.setTime(sdf.parse(date));
                    int year = dateParsed.get(Calendar.YEAR),
                            month = dateParsed.get(Calendar.MONTH),
                            day = dateParsed.get(Calendar.DAY_OF_MONTH);
                    String id = "";
                    if (editMode)
                        id = event.getId();
                    event = new DiaryCalendarEvent(title,year,month,day,description);
                    if (editMode) {
                        event.setId(id);
                        FirebaseManager.getInstance().updateEvent(Repository.getInstance().getUser().getId_nursery(),eventClassId,event);
                        Repository.getInstance().getNurserySchool().updateEvent(eventClassId, event);
                    } else {
                        for (String idClass : classesSelected) {
                            event = FirebaseManager.getInstance().addEvent(Repository.getInstance().getUser().getId_nursery(), idClass, event);
                            Repository.getInstance().getNurserySchool().addEvent(idClass, event);
                        }
                    }
                    getActivity().onBackPressed();
                } catch (ParseException e) {
                    Toast.makeText(getContext(), "No es una fecha válida", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void loadClassesCheckbox() {
        classesCheckbox = new ArrayList<>();
        ArrayList<NurseryClass> classes = Repository.getInstance().getNurserySchool().getNurseryClassesList();
        CheckBox aux;
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (NurseryClass nurseryClass : classes) {
            aux = new CheckBox(getContext());
            aux.setText(nurseryClass.getName());
            aux.setTag(nurseryClass.getId());
            aux.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            aux.setLayoutParams(params);
            rlClassesList.addView(aux);
            classesCheckbox.add(aux);
        }
    }

    private void openDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(tvDate.getText().toString()));
        } catch (Exception e){
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = ((dayOfMonth < 10) ? ("0") : "") + dayOfMonth + "/" + (((month + 1) < 10) ? "0" : "") + (month + 1) + "/" + year;
                tvDate.setText(date);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbar();
    }

    private void setToolbar() {
        ((Home_Teacher_Activity)getActivity()).getSupportActionBar().hide();
        ((Home_Teacher_Activity)getActivity()).setNavigationBottomBarHide(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((Babyguard_Application)(getContext()).getApplicationContext()).removeChatListener();
        ((Home_Teacher_Activity) getActivity()).getSupportActionBar().show();
        ((Home_Teacher_Activity)getActivity()).setNavigationBottomBarHide(false);
    }
}
