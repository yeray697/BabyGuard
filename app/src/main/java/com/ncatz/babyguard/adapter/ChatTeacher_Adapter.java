package com.ncatz.babyguard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ncatz.babyguard.model.Chat;

import java.util.List;

/**
 * Created by yeray697 on 20/05/17.
 */

public class ChatTeacher_Adapter extends ArrayAdapter<Chat> {
    public ChatTeacher_Adapter(Context context, int resource, List<Chat> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
