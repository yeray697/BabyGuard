package com.ncatz.babyguard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ncatz.babyguard.R;
import com.ncatz.babyguard.model.Kid;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * KidList adapter
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class KidList_Adapter extends ArrayAdapter<Kid>{
    OnImageClickListener mCallback;

    public KidList_Adapter(Context context, List<Kid> objects, OnImageClickListener mCallback) {
        super(context, R.layout.item_kid_list, objects);
        this.mCallback = mCallback;
    }

    public interface OnImageClickListener{
        void clicked(View view, Drawable drawable);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_kid_list,parent,false);
            holder = new ViewHolder();
            holder.ivKid = (CircleImageView) view.findViewById(R.id.ivKid_item);
            holder.tvKidName = (TextView) view.findViewById(R.id.tvNameKid_item);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.ivKid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.clicked(v,holder.ivKid.getDrawable());
            }
        });
        holder.ivKid.setOnTouchListener(new View.OnTouchListener() {
            private Rect rect;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    holder.ivKid.setColorFilter(Color.argb(50, 0, 0, 0));
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                } else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
                    holder.ivKid.setColorFilter(Color.argb(0, 0, 0, 0));
                } else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                        holder.ivKid.setColorFilter(Color.argb(0, 0, 0, 0));
                    }
                }
                return false;
            }
        });
        Glide.with(getContext())
                .load(getItem(position).getImg())
                .apply(RequestOptions.placeholderOf(R.mipmap.ic_placeholder_profile_img))
                .apply(RequestOptions.errorOf(R.mipmap.ic_placeholder_profile_img))
                .into(holder.ivKid);
        holder.tvKidName.setText(getItem(position).getName());
        return view;
    }

    public class ViewHolder{
        public CircleImageView ivKid;
        TextView tvKidName;
    }

}
