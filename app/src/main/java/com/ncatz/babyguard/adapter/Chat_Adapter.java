package com.ncatz.babyguard.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ncatz.babyguard.R;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.utils.Utils;

import java.util.ArrayList;

/**
 * Created by yeray697 on 1/05/17.
 */

public class Chat_Adapter extends ArrayAdapter<ChatMessage> {
    private int marginMessage;
    private String userId;
    public Chat_Adapter(Context context, ArrayList<ChatMessage> messages, String userId) {
        super(context, R.layout.item_chat_message, messages);
        this.userId = userId;
        this.marginMessage = getContext().getResources().getDimensionPixelSize(R.dimen.chatMessage_margin);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ChatMessage message = getItem(position);
        boolean isSender = isSender(message);
        MessageHolder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_chat_message,parent,false);
            holder = new MessageHolder();
            holder.rlMessage = (RelativeLayout) view.findViewById(R.id.rlMessage);
            holder.message = (TextView) view.findViewById(R.id.tvMessage_chatItem);
            holder.time = (TextView) view.findViewById(R.id.tvDate_chatItem);
            view.setTag(holder);
        } else{
            holder = (MessageHolder) view.getTag();
        }

        holder.message.setText(message.getMessage());
        holder.time.setText(Utils.getTimeByUnix(message.getDatetime()));

        Drawable background;
        int messageLayout = isSender ? R.drawable.bubble_receiver: R.drawable.bubble_sender;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            background = getMessageBackground(messageLayout);
        } else {
            int messageLayoutPressed = isSender ? R.drawable.bubble_pressed_receiver: R.drawable.bubble_pressed_sender;
            background = getMessageBackground(messageLayout, messageLayoutPressed);
        }
        holder.rlMessage.setBackground(background);
        RelativeLayout.LayoutParams params  = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (isSender){
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
        }
        getContext().getResources().getDimensionPixelSize(R.dimen.chatMessage_margin);
        params.setMargins(marginMessage,marginMessage,marginMessage,marginMessage);
        holder.rlMessage.setLayoutParams(params);
        return view;
    }

    private boolean isSender(ChatMessage message) {
        return !message.getSender().equals(userId);
    }

    public Drawable getMessageBackground(int resource, int resourcePressed) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{-android.R.attr.state_pressed, android.R.attr.state_enabled}, ContextCompat.getDrawable(getContext(), resource));
        states.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, ContextCompat.getDrawable(getContext(), resourcePressed));
        return states;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Drawable getMessageBackground(int messageBackground) {
        int color = ContextCompat.getColor(getContext(), R.color.gray_transparent);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]
                        {
                                new int[]{}
                        },
                new int[]
                        {
                                color
                        }
        );

        StateListDrawable drawable = new StateListDrawable();

        drawable.addState(new int[]{-android.R.attr.state_pressed, -android.R.attr.state_enabled}, ContextCompat.getDrawable(getContext(), messageBackground));

        RippleDrawable rippleDrawable = new RippleDrawable(colorStateList, drawable, null);

        return rippleDrawable;
    }

    private class MessageHolder{
        RelativeLayout rlMessage;
        TextView message;
        TextView time;
    }
}
