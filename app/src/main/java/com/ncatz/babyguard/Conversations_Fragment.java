package com.ncatz.babyguard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ncatz.babyguard.adapter.Conversation_Adapter;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.repository.Repository;


public class Conversations_Fragment extends Fragment {

    public static final String ID_KEY = "id";
    private Conversation_Adapter adapter;
    private ListView lvChats;
    private String transmitterId;

    public Conversations_Fragment() {
        // Required empty public constructor
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
        transmitterId = getArguments().getString(ID_KEY);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Babyguard_Application.isTeacher()) {
            ((Home_Parent_Activity)getActivity()).enableNavigationDrawer(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_chat_teacher, container, false);
        lvChats = (ListView) view.findViewById(R.id.lvChat);
        adapter = new Conversation_Adapter(getContext(), Repository.getInstance().getChats());
        lvChats.setAdapter(adapter);
        lvChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chatId = (String) view.getTag(R.id.conversationId);
                openChat(chatId);
            }
        });
        return view;
    }

    private void openChat(String chatId) {
        Bundle args = new Bundle();
        args.putString(Chat_Fragment.USER_CHAT_ID_KEY, chatId);
        args.putString(Chat_Fragment.KID_CHAT_ID_KEY, transmitterId);
        Chat_Fragment fragment = Chat_Fragment.newInstance(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, R.anim.fade_out,R.anim.fade_in,R.anim.fade_out)
                .replace(R.id.container_home, fragment,"chat")
                .addToBackStack("chat")
                .commit();
    }
}
