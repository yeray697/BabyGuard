package com.ncatz.babyguard.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ncatz.babyguard.R;
import com.ncatz.babyguard.model.TrackingKid;
import com.ncatz.babyguard.utils.Utils;
import com.yeray697.dotLineRecyclerView.DotLineRecyclerAdapter;
import com.yeray697.dotLineRecyclerView.RecyclerData;

import java.util.ArrayList;

/**
 * Created by yeray697 on 19/12/16.
 */

public class TrackingKid_Adapter extends DotLineRecyclerAdapter {


    private Context context;
    private View.OnCreateContextMenuListener onCreateContextMenuListener;

    public TrackingKid_Adapter(Context context, ArrayList<RecyclerData> data, int dotMarginLeft) {
        super(data, dotMarginLeft);
        this.context = context;
    }

    @Override
    public int getDotColor() {
        return ContextCompat.getColor(context, R.color.colorPrimaryLight);
    }

    @Override
    public int getDotBorderSize() {
        return 3;
    }

    @Override
    public int getDotSize() {
        return 2;
    }

    @Override
    public int getDotBorderColor() {
        return ContextCompat.getColor(context, R.color.colorDotBorder);
    }

    @Override
    public void onBindViewHolder(DotLineRecyclerAdapter.Holder holder, int position) {
        super.onBindViewHolder(holder, position);
        TrackingKid aux = ((TrackingKid)getItemAtPosition(position));
        int type = aux.getType();
        int resource = 0;
        switch (type){
            case TrackingKid.Type.POOP:
                resource = R.drawable.poop;
                break;
            case TrackingKid.Type.FOOD:
                resource = R.drawable.food;
                break;
            case TrackingKid.Type.SLEEP:
                resource = R.drawable.sleep;
                break;
            case TrackingKid.Type.OTHER:
                resource = R.drawable.other;
                break;

        }
        if (onCreateContextMenuListener != null)
            holder.message.setOnCreateContextMenuListener(onCreateContextMenuListener);
        holder.iv_item.setImageResource(resource);
        holder.message.setTextSubTitle(Utils.getTimeByUnix(aux.getDatetime()));
        holder.message.setTag(aux);
    }

    @Override
    public int getMessageBackground() {
        return R.drawable.bubble_receiver;
    }

    @Override
    public int getMessageBackgroundPressed() {
        return R.drawable.bubble_pressed_receiver;
    }

    @Override
    public int getMessageMarginTop() {
        return 25;
    }

    @Override
    public int getMessageMarginRight() {
        return 15;
    }

    public void setOnCreateContextMenuListenerItem(View.OnCreateContextMenuListener onCreateContextMenuListener) {
        this.onCreateContextMenuListener = onCreateContextMenuListener;
    }
}
