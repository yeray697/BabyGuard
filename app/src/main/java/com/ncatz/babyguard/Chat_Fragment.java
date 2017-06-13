package com.ncatz.babyguard;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ncatz.babyguard.adapter.Chat_Adapter;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.model.Chat;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.babyguard.utils.Utils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class Chat_Fragment extends Fragment {

    public static final String TEACHER_ID_KEY = "id";
    public static final String KID_ID_KEY = "kid";
    private TextView tvName;
    private ImageView backButton;
    private CircleImageView ivProfile;
    private ListView lvChat;
    private Button btSend;
    private EditText etSend;
    private Chat_Adapter adapter;
    private Context context;

    private Chat chat;
    private String teacherId;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context = getContext();
        } else {
            context = getActivity();
        }
        if (!Babyguard_Application.isTeacher())
            ((Home_Parent_Activity)getActivity()).enableNavigationDrawer(false);
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        teacherId = getArguments().getString(TEACHER_ID_KEY);
        kidId = getArguments().getString(KID_ID_KEY);
        backButton = (ImageView) view.findViewById(R.id.backButtonChat);
        ivProfile = (CircleImageView) view.findViewById(R.id.ivProfile_chat);
        tvName = (TextView) view.findViewById(R.id.tvName_chat);
        lvChat = (ListView) view.findViewById(R.id.lvChat);
        btSend = (Button) view.findViewById(R.id.btSendMessage);
        etSend = (EditText) view.findViewById(R.id.etMessage_chat);
        refreshList();
        tvName.setText(chat.getName());
        Glide.with(context).load(chat.getPhoto()).into(ivProfile);
        lvChat.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        lvChat.setStackFromBottom(true);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        ((Babyguard_Application)context.getApplicationContext()).addChatListener(new Babyguard_Application.ActionEndListener() {
            @Override
            public void onEnd() {
                refreshList();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    private void sendMessage() {
        String et = etSend.getText().toString();
        if (!TextUtils.isEmpty(et)) {
            Long timeUnix = System.currentTimeMillis();
            ChatMessage message = new ChatMessage(teacherId, kidId, et, String.valueOf(timeUnix));
            if (Babyguard_Application.isTeacher()) {
                FirebaseManager.getInstance().sendMessage(kidId, message);
            } else {
                FirebaseManager.getInstance().sendMessage(teacherId, message);
            }
            if (Repository.getInstance().addMessage(message)) {
                refreshList();
            }
            etSend.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbar();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar_chat);
        //toolbar.setTitle("Chat");
        if (Babyguard_Application.isTeacher()) {
            ((Home_Teacher_Activity)getActivity()).getSupportActionBar().hide();
            ((Home_Teacher_Activity)getActivity()).setNavigationBottomBarHide(true);
            //((Home_Teacher_Activity)getActivity()).prepareChatToolbar(toolbar);
        } else {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }
    }

    private void refreshList(){
        chat = Repository.getInstance().getChat(kidId,teacherId);
        ArrayList<ChatMessage> messages = Utils.parseChatMessageToChat(chat.getMessages());
        if (Babyguard_Application.isTeacher()) {
            adapter = new Chat_Adapter(context, messages,teacherId);
        } else {
            adapter = new Chat_Adapter(context, messages,kidId);
        }
        lvChat.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((Babyguard_Application)(context).getApplicationContext()).removeChatListener();
        if (Babyguard_Application.isTeacher()) {
            ((Home_Teacher_Activity) getActivity()).getSupportActionBar().show();
            ((Home_Teacher_Activity)getActivity()).setNavigationBottomBarHide(false);
        }
    }
}