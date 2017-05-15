package com.ncatz.babyguard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ncatz.babyguard.adapter.Chat_Adapter;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.model.Chat;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.repository.Repository;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class Chat_Fragment extends Fragment {

    public static final String USER_CHAT_ID_KEY = "id";
    public static final String KID_CHAT_ID_KEY = "kid";
    private TextView tvName;
    private CircleImageView ivProfile;
    private ListView lvChat;
    private Button btSend;
    private EditText etSend;
    private Chat_Adapter adapter;

    private Chat chat;
    private String userChatId;
    private String kidId;

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
        kidId = getArguments().getString(KID_CHAT_ID_KEY);
        ivProfile = (CircleImageView) view.findViewById(R.id.ivProfile_chat);
        tvName = (TextView) view.findViewById(R.id.tvName_chat);
        lvChat = (ListView) view.findViewById(R.id.lvChat);
        btSend = (Button) view.findViewById(R.id.btSendMessage);
        etSend = (EditText) view.findViewById(R.id.etMessage_chat);
        refreshList();
        tvName.setText(chat.getName());
        Picasso.with(getContext()).load(chat.getPhoto()).into(ivProfile);
        //lvChat.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvChat.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        lvChat.setStackFromBottom(true);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        ((Babyguard_Application)getContext().getApplicationContext()).addChatListener(new Babyguard_Application.ActionEndListener() {
            @Override
            public void onEnd() {
                refreshList();
            }
        });
        return view;
    }

    private void sendMessage() {
        String et = etSend.getText().toString();
        if (!TextUtils.isEmpty(et)) {
            Long timeUnix = System.currentTimeMillis();
            ChatMessage message = new ChatMessage(kidId, userChatId, et, String.valueOf(timeUnix));
            FirebaseManager.getInstance().sendMessage(userChatId, message);
            if (Repository.getInstance().addMessage(message,userChatId)) {
                refreshList();
            }
            etSend.setText("");
        }
    }

    private void setToolbar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_chat);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    }

    private void refreshList(){
        chat = Repository.getInstance().getChat(userChatId);
        adapter = new Chat_Adapter(getContext(), chat.getMessages(),kidId);
        lvChat.setAdapter(adapter);
    }
    @Override
    public void onStop() {
        super.onStop();
        ((Babyguard_Application)(getContext()).getApplicationContext()).removeChatListener();
    }
}