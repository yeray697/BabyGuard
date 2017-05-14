package com.ncatz.babyguard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ncatz.babyguard.adapter.Chat_Adapter;
import com.ncatz.babyguard.model.Chat;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.repository.Repository;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Chat_Fragment extends Fragment {

    public static final String USER_CHAT_ID_KEY = "id";
    private TextView tvName;
    private CircleImageView ivProfile;
    private ListView lvChat;
    private Button btSend;
    private EditText etSend;
    private Chat_Adapter adapter;

    private Chat chat;
    private String userChatId;

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
        ((Home_Activity)getActivity()).enableNavigationDrawer(false);
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        setToolbar(view);
        userChatId = getArguments().getString(USER_CHAT_ID_KEY);
        chat = Repository.getInstance().getChat(userChatId);
        ivProfile = (CircleImageView) view.findViewById(R.id.ivProfile_chat);
        tvName = (TextView) view.findViewById(R.id.tvName_chat);
        lvChat = (ListView) view.findViewById(R.id.lvChat);
        btSend = (Button) view.findViewById(R.id.btSendMessage);
        etSend = (EditText) view.findViewById(R.id.etMessage_chat);
        tvName.setText(chat.getName());
        Picasso.with(getContext()).load(chat.getPhoto()).into(ivProfile);
        adapter = new Chat_Adapter(getContext(), chat.getMessages());
        lvChat.setAdapter(adapter);
        lvChat.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        ((Babyguard_Application)getContext().getApplicationContext()).addChatListener(new Babyguard_Application.ActionEndListener() {
            @Override
            public void onEnd() {
                chat = Repository.getInstance().getChat(userChatId);
                adapter = new Chat_Adapter(getContext(), chat.getMessages());
                lvChat.setAdapter(adapter);
            }
        });
        return view;
    }

    private void sendMessage() {
        adapter.add(new ChatMessage("1",etSend.getText().toString(),"gsg","16:31"));
        adapter.notifyDataSetChanged();
    }

    private void setToolbar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_chat);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((Babyguard_Application)(getContext()).getApplicationContext()).removeChatListener();
    }
}