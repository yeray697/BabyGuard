package com.ncatz.babyguard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ncatz.babyguard.R;
import com.ncatz.babyguard.model.NurseryClass;

import java.util.List;

/**
 * Adapter used in spinner to select active class (teacher view)
 */

public class SpinnerKidsTeacher_Adapter extends ArrayAdapter<NurseryClass> {
    public SpinnerKidsTeacher_Adapter(@NonNull Context context, @NonNull List<NurseryClass> objects) {
        super(context, R.layout.item_class_spinner, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView v = (TextView) super
                .getView(position, convertView, parent);
        v.setText(getItem(position).getName());
        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        v.setText(getItem(position).getName());
        v.setBackgroundColor(v.getContext().getResources().getColor(R.color.background_item_not_expanded));
        return v;
    }
}
