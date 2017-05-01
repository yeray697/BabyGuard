package com.ncatz.babyguard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ncatz.babyguard.adapter.Chat_Adapter;
import com.ncatz.babyguard.model.ChatMessage;

import java.util.List;


public class Chat_Fragment extends Fragment {

    private ListView lvChat;
    private Button btSend;
    private EditText etSend;
    private Chat_Adapter adapter;

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
        lvChat = (ListView) view.findViewById(R.id.lvChat);
        btSend = (Button) view.findViewById(R.id.btSendMessage);
        etSend = (EditText) view.findViewById(R.id.etMessage_chat);
        adapter = new Chat_Adapter(getContext(),"1");
        lvChat.setAdapter(adapter);
        lvChat.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.add(new ChatMessage("1",etSend.getText().toString(),"gsg","16:31"));
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }
}