package com.yeray697.dotLineRecyclerView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Message view component
 * @author yeray697
 * @version 1.0
 * Created on 21/12/16.
 */
public class Message_View extends RelativeLayout {
    private RelativeLayout rlMessage;
    private TextView tvTitle;
    private TextView tvSubTitle;

    private String textTitle;
    private String textSubTitle;
    private int textTitleColor;
    private int textSubTitleColor;
    private float textTitleSize;
    private float textSubTitleSize;

    private int position;
    private final int pico = 20;
    private OnMessageClickListener mCallbackMessageClick;
    private OnMessageLongClickListener mCallbackMessageLongClick;

    public interface OnMessageClickListener{
        void onClick(View v, int position);
    }
    public interface OnMessageLongClickListener{
        boolean onClick(View v, int position);
    }

    //Constructors
    public Message_View(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initialize(attrs);
    }

    public Message_View(Context context) {
        super(context);
        initialize(null);
    }

    public Message_View(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    //Privated methods
    /**
     * Inflate the view and set the attributes if they  are not null
     * @param attrs XML Attributes
     */
    private void initialize(AttributeSet attrs) {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li =
                (LayoutInflater)getContext().getSystemService(infService);
        View view = li.inflate(R.layout.message_view, this, true);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallbackMessageClick != null)
                    mCallbackMessageClick.onClick(v,position);
            }
        });
        view.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mCallbackMessageLongClick != null)
                    return mCallbackMessageLongClick.onClick(v,position);
                return false;
            }
        });
        rlMessage = (RelativeLayout) findViewById(R.id.rlMessage);
        tvTitle = (TextView) findViewById(R.id.tvTitle_item);
        tvSubTitle = (TextView) findViewById(R.id.tvSubTitle_item);

        if (attrs != null) {
            getXMLValues(attrs);

        }
        else {
            defaultAttributes();
        }
    }

    /**
     * Setting default attributes
     */
    private void defaultAttributes() {
        setTextTitle("Title");
        setTextSubTitle("Subtitle");
        setTextTitleColor(Color.BLACK);
        setTextSubTitleColor(Color.BLACK);
        setTextTitleSize(17);
        setTextSubTitleSize(12);
    }

    /**
     * Get attributes from xml declaration
     * @param attrs XML Attribues
     */
    private void getXMLValues(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Message_View,
                0, 0);

        try {
            setTextTitle(a.getString(R.styleable.Message_View_textTitle));
            setTextSubTitle(a.getString(R.styleable.Message_View_textSubtitle));
            setTextTitleColor(a.getColor(R.styleable.Message_View_textTitleColor, Color.BLACK));
            setTextSubTitleColor(a.getColor(R.styleable.Message_View_textSubtitleColor, 2));
            setTextTitleSize(a.getDimension(R.styleable.Message_View_textTitleSize, 17));
            setTextSubTitleSize(a.getDimension(R.styleable.Message_View_textSubtitleSize, 12));
        } finally {
            a.recycle();
        }
    }

    //Getters and setters

    public String getTextSubTitle() {
        return textSubTitle;
    }

    public void setTextSubTitle(String textSubTitle) {
        if (TextUtils.isEmpty(textSubTitle))
            tvSubTitle.setVisibility(View.GONE);
        else
            tvSubTitle.setVisibility(View.VISIBLE);
        this.textSubTitle = textSubTitle;
        this.tvSubTitle.setText(this.textSubTitle);
    }

    public int getTextSubTitleColor() {
        return textSubTitleColor;
    }

    public void setTextSubTitleColor(int textSubTitleColor) {
        this.textSubTitleColor = textSubTitleColor;
        this.tvSubTitle.setTextColor(this.textSubTitleColor);
    }

    public String getTextTitle() {
        return textTitle;
    }

    public void setTextTitle(String textTitle) {
        this.textTitle = textTitle;
        this.tvTitle.setText(this.textTitle);
    }

    public int getTextTitleColor() {
        return textTitleColor;
    }

    public void setTextTitleColor(int textTitleColor) {
        this.textTitleColor = textTitleColor;
        this.tvTitle.setTextColor(this.textTitleColor);
    }

    public float getTextTitleSize() {
        return textTitleSize;
    }

    public void setTextTitleSize(float textTitleSize) {
        this.textTitleSize = textTitleSize;
        this.tvTitle.setTextSize(this.textTitleSize);
    }

    public float getTextSubTitleSize() {
        return textSubTitleSize;
    }

    public void setTextSubTitleSize(float textSubTitleSize) {
        this.textSubTitleSize = textSubTitleSize;
        this.tvSubTitle.setTextSize(this.textSubTitleSize);
    }

    public void setMessageBackground(int resource, int resourcePressed, boolean isListener) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{-android.R.attr.state_pressed, android.R.attr.state_enabled}, ContextCompat.getDrawable(getContext(), resource));
        if (isListener) {
            states.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, ContextCompat.getDrawable(getContext(), resourcePressed));
        } else{
            states.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, ContextCompat.getDrawable(getContext(), resource));
        }
        rlMessage.setBackground(states);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setMessageBackground(int messageBackground, boolean isListener) {
        int color = ContextCompat.getColor(getContext(),R.color.gray_transparent);
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

        if (isListener) {
            rlMessage.setBackground(rippleDrawable);
        }
        else
            rlMessage.setBackground(drawable);
    }

    //Listeners
    public void setOnMessageClickListener(OnMessageClickListener onDotClickListener){
        this.mCallbackMessageClick = onDotClickListener;
    }

    public void setOnMessageLongClickListener(OnMessageLongClickListener onDotLongClickListener){
        this.mCallbackMessageLongClick = onDotLongClickListener;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
