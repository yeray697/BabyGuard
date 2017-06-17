package com.ncatz.babyguard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ncatz.babyguard.R;
import com.ncatz.babyguard.model.Chat;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.utils.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yeray697 on 20/05/17.
 */

public class Conversation_Adapter extends ArrayAdapter<Chat> {
    private int margin10dp;

    public Conversation_Adapter(Context context, List<Chat> objects) {
        super(context, R.layout.item_conversation, objects);
        margin10dp = getContext().getResources().getDimensionPixelSize(R.dimen.chatMessage_margin);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ChatHolder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_conversation,parent,false);
            holder = new ChatHolder();
            holder.tvName = (TextView) view.findViewById(R.id.tvNameChat_item);
            holder.tvDate = (TextView) view.findViewById(R.id.tvDateChat_item);
            holder.tvMessage = (TextView) view.findViewById(R.id.tvMessageChat_item);
            holder.ivProfile = (CircleImageView) view.findViewById(R.id.ivProfileChat_item);
            view.setTag(R.id.conversationHolder,holder);
        } else {
            holder = (ChatHolder) view.getTag(R.id.conversationHolder);
        }
        Chat aux = getItem(position);
        holder.tvName.setText(aux.getName());
        Glide.with(getContext())
                .load(aux.getPhoto())
                .apply(RequestOptions.placeholderOf(R.mipmap.ic_placeholder_profile_img))
                .apply(RequestOptions.errorOf(R.mipmap.ic_placeholder_profile_img))
                .into(holder.ivProfile);
        ChatMessage lastMessage = aux.getLastMessage();
        RelativeLayout.LayoutParams params  = (RelativeLayout.LayoutParams) holder.tvName.getLayoutParams();
        if (lastMessage != null) {
            params.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
            holder.tvMessage.setText(lastMessage.getMessage());
            holder.tvDate.setText(Utils.parseDatetimeToChat(lastMessage.getDatetime()));
        } else {
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            holder.tvMessage.setText("");
            holder.tvDate.setText("");
        }
        holder.tvName.setLayoutParams(params);
        view.setTag(R.id.conversationId,aux.getId());
        view.setTag(R.id.deviceId,aux.getFcmToken());
        return view;
    }

    class ChatHolder{
        TextView tvName;
        TextView tvDate;
        TextView tvMessage;
        CircleImageView ivProfile;
    }
}
