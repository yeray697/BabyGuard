package com.phile.babyguard.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.phile.babyguard.R;
import com.phile.babyguard.model.InfoKid;
import com.yeray697.dotLineRecyclerView.DotLineRecyclerAdapter;
import com.yeray697.dotLineRecyclerView.RecyclerData;

import java.util.ArrayList;

/**
 * Created by yeray697 on 19/12/16.
 */

public class InfoKid_Adapter extends DotLineRecyclerAdapter {


    private final Context context;


    public InfoKid_Adapter(ArrayList<RecyclerData> data, Context context) {
        super(data);
        this.context = context;
    }

    @Override
    public int getDotColor() {
        return ContextCompat.getColor(context, R.color.colorPrimaryLight);
    }

    @Override
    public int getDotBorderSize() {
        return 8;
    }

    @Override
    public int getDotSize() {
        return 4;
    }

    @Override
    public int getDotBorderColor() {
        return ContextCompat.getColor(context, R.color.colorDotBorder);
    }

    @Override
    public void onBindViewHolder(DotLineRecyclerAdapter.Holder holder, int position) {
        super.onBindViewHolder(holder, position);
        int type = ((InfoKid)getItemAtPosition(position)).getType();
        if(position != 0 && type == ((InfoKid)getItemAtPosition(position - 1)).getType()){
            holder.iv_item.setImageDrawable(null);
        } else {
            int resource = 0;
            switch (type){
                //TODO poner fotos correspondientes
                case InfoKid.Type.POOP:
                    resource = R.drawable.poop;
                    break;
                case InfoKid.Type.FOOD:
                    resource = R.drawable.food;
                    break;
                case InfoKid.Type.SLEEP:
                    resource = R.drawable.sleep;
                    break;
                case InfoKid.Type.OTHER:
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
        return 65;
    }

    @Override
    public int getMessageMarginRight() {
        return 15;
    }
}
