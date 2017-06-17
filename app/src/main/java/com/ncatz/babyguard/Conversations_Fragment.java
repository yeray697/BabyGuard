package com.ncatz.babyguard;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ncatz.babyguard.adapter.Conversation_Adapter;
import com.ncatz.babyguard.components.CustomToolbar;
import com.ncatz.babyguard.model.Chat;
import com.ncatz.babyguard.repository.Repository;

import java.util.ArrayList;
import java.util.Collections;


public class Conversations_Fragment extends Fragment {

    public static final String ID_KEY = "id";
    private Conversation_Adapter adapter;
    private Toolbar toolbar;
    private ListView lvChats;
    private String transmitterId;
    private Home_Teacher_Activity.OnSelectedClassIdChangedListener listener;
    private String classId;
    private Context context;

    public Conversations_Fragment() {
        // Required empty public constructor
        listener = new Home_Teacher_Activity.OnSelectedClassIdChangedListener() {
            @Override
            public void selectedClassIdChanged(String newId) {
                classId = newId;
                refreshChatList();
            }
        };
    }

    public static Conversations_Fragment newInstance(Bundle args) {
        Conversations_Fragment fragment = new Conversations_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context = getContext();
        } else {
            context = getActivity();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Babyguard_Application.isTeacher()) {
            ((Home_Parent_Activity)getActivity()).enableNavigationDrawer(true);
        }
        Babyguard_Application.setCurrentActivity("Conversations_Fragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        Babyguard_Application.setCurrentActivity("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        toolbar = (CustomToolbar) view.findViewById(R.id.toolbar_conversation);
        lvChats = (ListView) view.findViewById(R.id.lvChat);
        lvChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chatId = (String) view.getTag(R.id.conversationId);
                String deviceId = (String) view.getTag(R.id.deviceId);
                openChat(chatId,deviceId);
            }
        });
        setToolbar();
        if (Babyguard_Application.isTeacher()) {
            classId = ((Home_Teacher_Activity) getActivity()).getSelectedClassId();
            transmitterId = Repository.getInstance().getUser().getId();
        } else {
            transmitterId = getArguments().getString(ID_KEY);
        }
        refreshChatList();
        return view;
    }

    private void setToolbar() {
        if (Babyguard_Application.isTeacher()) {
            toolbar.setVisibility(View.GONE);
            ((Home_Teacher_Activity) getActivity()).getSupportActionBar().setTitle("Babyguard");
        } else {
            toolbar.setVisibility(View.VISIBLE);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            final ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if (ab != null) {
                ab.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void openChat(String chatId, String deviceId) {
        Bundle args = new Bundle();
        if (Babyguard_Application.isTeacher()) {
            args.putString(Chat_Fragment.TEACHER_ID_KEY, transmitterId);
            args.putString(Chat_Fragment.KID_ID_KEY, chatId);
        } else {
            args.putString(Chat_Fragment.TEACHER_ID_KEY, chatId);
            args.putString(Chat_Fragment.KID_ID_KEY, transmitterId);
        }
        args.putString(Chat_Fragment.DEVICE_ID_KEY,deviceId);
        Chat_Fragment fragment = Chat_Fragment.newInstance(args);

        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.container_home, fragment,"chat")
                .addToBackStack("chat")
                .commit();
    }

    private void refreshChatList() {
        ArrayList<Chat> chats;
        if (Babyguard_Application.isTeacher()) {
            chats = Repository.getInstance().getChats(classId);
        } else {
            chats = Repository.getInstance().getChatByKidId(transmitterId);
        }
        Collections.sort(chats,Chat.comparator);
        adapter = new Conversation_Adapter(context, chats);
        lvChats.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (Babyguard_Application.isTeacher())
            ((Home_Teacher_Activity)getActivity()).setSelectedClassIdChangedListener(listener);
    }
}
