package com.phile.babyguard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phile.babyguard.R;
import com.phile.babyguard.model.Kid;
import com.phile.babyguard.repository.Repository;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by usuario on 19/12/16.
 */

public class KidList_Adapter extends ArrayAdapter<Kid>{
    public KidList_Adapter(Context context) {
        super(context, R.layout.kid_list_item, Repository.getInstance().getKids());
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.kid_list_item,parent,false);
            holder = new ViewHolder();
            holder.ivKid = (CircleImageView) view.findViewById(R.id.ivKid_item);
            holder.tvKidName = (TextView) view.findViewById(R.id.tvNameKid_item);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        Picasso.with(getContext()).load(getItem(position).getPhoto()).into(holder.ivKid);
        holder.tvKidName.setText(getItem(position).getName());
        return view;
    }
    class ViewHolder{
        CircleImageView ivKid;
        TextView tvKidName;
    }
}
