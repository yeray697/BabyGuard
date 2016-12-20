package com.phile.babyguard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.phile.babyguard.R;
import com.phile.babyguard.model.InfoKid;
import com.phile.babyguard.repository.Repository;

import java.util.ArrayList;

/**
 * Created by yeray697 on 19/12/16.
 */

public class InfoKid_Adapter extends RecyclerView.Adapter<InfoKid_Adapter.Holder> {

    private final ArrayList<InfoKid> info;

    private String id_kid;

    public InfoKid_Adapter (ArrayList<InfoKid> info, String id_kid) {
        this.info = info;
        this.id_kid = id_kid;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.infokid_list_item, parent, false);

        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        int type = info.get(position).getType();
        if (position != 0 && type == info.get(position - 1).getType()) {
            holder.ivType.setBackground(null);
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
            holder.ivType.setBackgroundResource(resource);
        }
        holder.tvTitle.setText(info.get(position).getTitle());
        holder.tvDate.setText(info.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    public void orderBy(@Repository.Sort int order) {
        info.clear();
        info.addAll(Repository.getInstance().getInfoKidById(order));
    }
    class Holder extends RecyclerView.ViewHolder {
        ImageView ivType;
        TextView tvTitle, tvDate;

        public Holder(View itemView) {
            super(itemView);
            ivType = (ImageView) itemView.findViewById(R.id.ivTypeInfoKid_item);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitleInfoKid_item);
            tvDate = (TextView) itemView.findViewById(R.id.tvDateInfoKid_item);
        }
    }
}
