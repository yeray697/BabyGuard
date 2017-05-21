package com.ncatz.babyguard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ncatz.babyguard.R;
import com.ncatz.babyguard.model.Chat;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yeray697 on 20/05/17.
 */

public class ChatTeacher_Adapter extends ArrayAdapter<Chat> {
    public ChatTeacher_Adapter(Context context, List<Chat> objects) {
        super(context, R.layout.chat_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ChatHolder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.chat_item,parent,false);
            holder = new ChatHolder();
            holder.tvName = (TextView) view.findViewById(R.id.tvNameChat_item);
            holder.tvDate = (TextView) view.findViewById(R.id.tvDateChat_item);
            holder.tvMessage = (TextView) view.findViewById(R.id.tvMessageChat_item);
            holder.ivProfile = (CircleImageView) view.findViewById(R.id.ivProfileChat_item);
            view.setTag(holder);
        } else {
            holder = (ChatHolder) view.getTag();
        }
        Chat aux = getItem(position);
        holder.tvName.setText(aux.getName());
        Picasso.with(getContext())
                .load(aux.getPhoto())
                .into(holder.ivProfile);
        ChatMessage lastMessage = aux.getLastMessage();
        if (lastMessage != null) {
            holder.tvMessage.setText(lastMessage.getMessage());
            holder.tvDate.setText(Utils.parseDatetimeToChat(lastMessage.getDatetime()));
        } else {
            holder.tvMessage.setText("");
            holder.tvDate.setText("");
        }
        return view;
    }

    class ChatHolder{
        TextView tvName;
        TextView tvDate;
        TextView tvMessage;
        CircleImageView ivProfile;
    }
}
