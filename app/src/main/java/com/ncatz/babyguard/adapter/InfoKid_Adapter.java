package com.ncatz.babyguard.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.ncatz.babyguard.R;
import com.ncatz.babyguard.model.TrackingKid;
import com.yeray697.dotLineRecyclerView.DotLineRecyclerAdapter;
import com.yeray697.dotLineRecyclerView.RecyclerData;

import java.util.ArrayList;

/**
 * Created by yeray697 on 19/12/16.
 */

public class InfoKid_Adapter extends DotLineRecyclerAdapter {


    private Context context;

    public InfoKid_Adapter(Context context, ArrayList<RecyclerData> data, int dotMarginLeft) {
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
        int type = ((TrackingKid)getItemAtPosition(position)).getType();
        if(position != 0 && type == ((TrackingKid)getItemAtPosition(position - 1)).getType()){
            holder.iv_item.setImageDrawable(null);
        } else {
            int resource = 0;
            switch (type){
                //TODO poner fotos correspondientes
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
            holder.iv_item.setImageResource(resource);
        }
    }

    @Override
    public int getMessageBackground() {
        return R.drawable.bocadillo;
    }

    @Override
    public int getMessageBackgroundPressed() {
        return R.drawable.bocadillo_pressed;
    }

    @Override
    public int getMessageMarginTop() {
        return 25;
    }

    @Override
    public int getMessageMarginRight() {
        return 15;
    }
}
