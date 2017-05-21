package com.ncatz.babyguard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ncatz.babyguard.adapter.ChatTeacher_Adapter;
import com.ncatz.babyguard.repository.Repository;


public class ChatTeacher_Fragment extends Fragment {

    private ChatTeacher_Adapter adapter;
    private ListView lvChats;

    public ChatTeacher_Fragment() {
        // Required empty public constructor
    }

    public static ChatTeacher_Fragment newInstance(Bundle args) {
        ChatTeacher_Fragment fragment = new ChatTeacher_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_teacher, container, false);
        lvChats = (ListView) view.findViewById(R.id.lvChat);
        adapter = new ChatTeacher_Adapter(getContext(), Repository.getInstance().getChats());
        lvChats.setAdapter(adapter);
        return view;
    }
}
