package com.yeray697.dotLineRecyclerView;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * DotLineRecycler's adapter
 * @author yeray697
 * @version 1.0
 * Created on 21/12/16.
 */
public abstract class DotLineRecyclerAdapter extends RecyclerView.Adapter<DotLineRecyclerAdapter.Holder> {

    private static final int DEFAULT_DOT_MARGIN_RIGHT = 100;
    private static final int TEXT_TITLE_SIZE = 17;
    private static final int TEXT_SUBTITLE_SIZE = 12;
    private static final int MESSAGE_BACKGROUND = R.drawable.bocadillo;
    private static final int MESSAGE_BACKGROUND_PRESSED = R.drawable.bocadillo_pressed;

    private final int DOT_BORDER_COLOR = Color.BLACK;
    private final int DOT_COLOR = Color.WHITE;
    private final int DOT_SIZE = 2;
    private final int DOT_BORDER_SIZE = 2;

    private final int TEXT_TITLE_COLOR = Color.GRAY;
    private final int TEXT_SUBTITLE_COLOR  = Color.GRAY;

    private ArrayList<RecyclerData> list;
    private int SEPARATOR = 10;
    private int dotMarginLeft;
    private final int MESSAGE_MARGIN_TOP = 10;
    private final int MESSAGE_MARGIN_RIGHT = 10;
    ArrayList<Integer> colorList;

    private OnImageClickListener mCallbackImageClick;
    private OnImageLongClickListener mCallbackImageLongClick;
    private Message_View.OnMessageClickListener mCallbackMessageClick;
    private Message_View.OnMessageLongClickListener mCallbackMessageLongClick;
    private DotView.OnDotClickListener mCallbackDotClick;
    private DotView.OnDotLongClickListener mCallbackDotLongClick;

    public interface OnImageClickListener{
        void onClick(View v, int position);
    }
    public interface OnImageLongClickListener{
        boolean onClick(View v, int position);
    }
    //Constructors
    public DotLineRecyclerAdapter(ArrayList<RecyclerData> data) {
        this.list = data;
        this.dotMarginLeft = DEFAULT_DOT_MARGIN_RIGHT;
    }

    public DotLineRecyclerAdapter(ArrayList<RecyclerData> data, int dotMarginLeft) {
        this.list = data;
        this.dotMarginLeft = (dotMarginLeft >= 0 ? dotMarginLeft :0);
    }

    public DotLineRecyclerAdapter(ArrayList<RecyclerData> data,  ArrayList<Integer> colorList) {
        this.list = data;
        this.dotMarginLeft = DEFAULT_DOT_MARGIN_RIGHT;
        this.colorList = colorList;
    }

    public DotLineRecyclerAdapter(ArrayList<RecyclerData> data, int dotMarginLeft,  ArrayList<Integer> colorList) {
        this.list = data;
        this.dotMarginLeft = (dotMarginLeft >= 0 ? dotMarginLeft :0);
        this.colorList = colorList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        RecyclerData aux = list.get(position);
        boolean isMessageClickListener = (mCallbackMessageClick!= null || mCallbackMessageLongClick != null);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            holder.message.setMessageBackground(getMessageBackground(),isMessageClickListener);
        } else {
            holder.message.setMessageBackground(getMessageBackground(), getMessageBackgroundPressed(),isMessageClickListener);

        }
        if (aux.isImageADrawable()){
            holder.iv_item.setImageDrawable(aux.getImage());
        } else if (aux.isImageAResource()){
            holder.iv_item.setImageResource(aux.getImageResource());
        } else if (aux.isImageAnURL()){
            Picasso.with(holder.iv_item.getContext())
                    .load(aux.getImageUrl())
                    .error(getImageError())
                    .into(holder.iv_item);
        } else
            holder.iv_item.setImageDrawable(null);
        holder.iv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallbackImageClick != null)
                    mCallbackImageClick.onClick(v,position);
            }
        });
        holder.iv_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mCallbackImageLongClick != null)
                    return mCallbackImageLongClick.onClick(v,position);
                return false;
            }
        });
        if (mCallbackImageClick != null || mCallbackImageLongClick != null) {
            holder.iv_item.setOnTouchListener(new View.OnTouchListener() {
                Rect rect;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        holder.iv_item.setColorFilter(Color.argb(50, 0, 0, 0));
                        rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        holder.iv_item.setColorFilter(Color.argb(0, 0, 0, 0));
                    }
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            holder.iv_item.setColorFilter(Color.argb(0, 0, 0, 0));
                        }
                    }
                    if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        holder.iv_item.setColorFilter(Color.argb(0, 0, 0, 0));
                    }
                    return false;
                }
            });
        }
        holder.dot.setPosition(position);
        holder.message.setPosition(position);
        holder.dot.setOnDotClickListener(mCallbackDotClick);
        holder.dot.setOnDotLongClickListener(mCallbackDotLongClick);
        holder.message.setOnMessageClickListener(mCallbackMessageClick);
        holder.message.setOnMessageLongClickListener(mCallbackMessageLongClick);
        holder.message.setTextTitle(aux.getTitle());
        holder.message.setTextSubTitle(aux.getSubtitle());
        setHolderSettings(holder,aux);
    }

    /**
     * Called from onBindViewHolder, it sets settings on the view components
     *
     * @param holder Holder that contains the view
     * @param aux
     */
    private void setHolderSettings(Holder holder, RecyclerData aux) {
        if (colorList == null || aux.getIdColor() == RecyclerData.NO_ID_COLOR)
            holder.dot.setDotBorderColor(getDotBorderColor());
        else
            holder.dot.setDotBorderColor(colorList.get(aux.getIdColor()));
        holder.dot.setDotColor(getDotColor());
        holder.dot.setDotSize((int)(Utils.dpToPx(holder.dot.getContext(),getDotSize())) * 10);
        holder.dot.setDotBorderSize((int)(Utils.dpToPx(holder.dot.getContext(),getDotBorderSize())));
        holder.dot.setDotMarginLeft((int)(Utils.dpToPx(holder.dot.getContext(),getDotMarginLeft())));

        holder.message.setTextSubTitleColor(getTextSubtitleColor());
        holder.message.setTextTitleColor(getTextTitleColor());
        holder.message.setTextTitleSize(getTextTitleSize());
        holder.message.setTextSubTitleSize(getTextSubtitleSize());
        ((RelativeLayout.LayoutParams) holder.message.getLayoutParams()).setMargins(0,
                (int)(Utils.dpToPx(holder.dot.getContext(),getMessageMarginTop())),
                (int)(Utils.dpToPx(holder.dot.getContext(),getMessageMarginRight())),
                0);

        ViewGroup.MarginLayoutParams relativeParams = (ViewGroup.MarginLayoutParams) holder.rlItem.getLayoutParams();
        relativeParams.setMargins(5, 20, 5, getSeparator() * 4);
        holder.rlItem.setLayoutParams(relativeParams);
        holder.rlItem.requestLayout();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public RelativeLayout rlItem;
        public ImageView iv_item;
        public Message_View message;
        public DotView dot;

        Holder(View itemView) {
            super(itemView);
            rlItem = (RelativeLayout) itemView.findViewById(R.id.rlItem);
            iv_item = (ImageView) itemView.findViewById(R.id.iv_item);
            message = (Message_View) itemView.findViewById(R.id.message_item);
            dot = (DotView) itemView.findViewById(R.id.line_and_dot);
        }
    }

    /**
     * Returns the item at X position
     * @param position Item position
     * @return Return the item
     */
    public RecyclerData getItemAtPosition(int position){
        return list.get(position);
    }

    public final int getDotMarginLeft() {
        return dotMarginLeft;
    }


    public ArrayList<RecyclerData> getList(){
        return list;
    }
    public ArrayList<Integer> getColorList(){
        return colorList;
    }
    public void setColorList(ArrayList<Integer> colorList){
        this.colorList = colorList;
        notifyDataSetChanged();
    }
    public void clear(){
        this.list.clear();
        notifyDataSetChanged();
    }
    public void addAll(ArrayList<RecyclerData> list){
        this.list.addAll(list);
    }

    //Methods to override
    public int getDotBorderColor() {
        return DOT_BORDER_COLOR;
    }

    public int getDotBorderSize() {
        return DOT_BORDER_SIZE;
    }

    public int getDotColor() {
        return DOT_COLOR;
    }

    public int getDotSize() {
        return DOT_SIZE;
    }

    public int getTextSubtitleColor() {
        return TEXT_SUBTITLE_COLOR;
    }

    public int getTextTitleColor() {
        return TEXT_TITLE_COLOR;
    }

    public int getTextSubtitleSize() {
        return TEXT_SUBTITLE_SIZE;
    }

    public int getTextTitleSize() {
        return TEXT_TITLE_SIZE;
    }

    public int getSeparator(){
        return SEPARATOR;
    }

    public int getImageError(){
        return R.mipmap.ic_launcher;
    }

    public int getMessageBackground(){
        return MESSAGE_BACKGROUND;
    }

    public int getMessageBackgroundPressed(){
        return MESSAGE_BACKGROUND_PRESSED;
    }

    public int getMessageMarginTop() {
        return MESSAGE_MARGIN_TOP;
    }

    public int getMessageMarginRight() {
        return MESSAGE_MARGIN_RIGHT;
    }

    //Listeners
    public void setOnImageClickListener(OnImageClickListener onDotClickListener){
        this.mCallbackImageClick = onDotClickListener;
    }

    public void setOnImageLongClickListener(OnImageLongClickListener onDotLongClickListener){
        this.mCallbackImageLongClick = onDotLongClickListener;
    }

    public void setOnMessageClickListener(Message_View.OnMessageClickListener onDotClickListener){
        this.mCallbackMessageClick = onDotClickListener;
    }

    public void setOnMessageLongClickListener(Message_View.OnMessageLongClickListener onDotLongClickListener){
        this.mCallbackMessageLongClick = onDotLongClickListener;
    }

    public void setOnDotClickListener(DotView.OnDotClickListener onDotClickListener){
        this.mCallbackDotClick = onDotClickListener;
    }

    public void setOnDotLongClickListener(DotView.OnDotLongClickListener onDotLongClickListener){
        this.mCallbackDotLongClick = onDotLongClickListener;
    }
}
