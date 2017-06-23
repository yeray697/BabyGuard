package com.yeray697.dotLineRecyclerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * DotView component
 * @author yeray697
 * @version 1.0
 * Created on 20/12/16.
 */
public class DotView extends RelativeLayout {
    private View dot;

    private int position;

    private int dotBorderColor;
    private int dotColor;
    private int dotSize;
    private int dotBorderWidth;
    private int dotMarginTop;
    private int dotMarginRight;
    private int dotMarginLeft;
    private int dotMarginBottom;
    private OnDotClickListener mCallbackClick;
    private OnDotLongClickListener mCallbackLongClick;

    //Constructors
    public DotView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initialize(attrs);
    }

    public DotView(Context context) {
        super(context);
        initialize(null);
    }

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public interface OnDotClickListener{
        void onClick(View v, int position);
    }
    public interface OnDotLongClickListener{
        boolean onClick(View v, int position);
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
        li.inflate(R.layout.dot_view, this, true);

        this.dot = findViewById(R.id.dot);
        this.dot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallbackClick != null)
                    mCallbackClick.onClick(v, position);
            }
        });
        this.dot.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mCallbackLongClick != null)
                    return mCallbackLongClick.onClick(v, position);
                return false;
            }
        });

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
        setDotColor(Color.WHITE);
        setDotBorderColor(Color.BLACK);
        setDotBorderSize(2);
        setDotSize(15);
        setDotMarginTop(8);
        setDotMarginLeft(0);
        setDotMarginRight(0);
        setDotMarginBottom(0);
    }

    /**
     * Get attributes from xml declaration
     * @param attrs XML Attribues
     */
    private void getXMLValues(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DotView,
                0, 0);

        try {
            setDotColor(a.getColor(R.styleable.DotView_dotColor, Color.WHITE));
            setDotBorderColor(a.getColor(R.styleable.DotView_dotBorderColor, Color.BLACK));
            setDotBorderSize((int) a.getDimension(R.styleable.DotView_dotBorderWidth, 2));
            setDotSize((int) a.getDimension(R.styleable.DotView_dotSize,15));
            this.dotMarginTop = (int) a.getDimension(R.styleable.DotView_dotMarginTop, 0);
            this.dotMarginLeft = (int) a.getDimension(R.styleable.DotView_dotMarginLeft, 0);
            this.dotMarginRight = (int) a.getDimension(R.styleable.DotView_dotMarginRight, 0);
            this.dotMarginBottom = (int) a.getDimension(R.styleable.DotView_dotMarginBottom, 0);
            ((LayoutParams)this.dot.getLayoutParams()).setMargins(dotMarginLeft,dotMarginTop, dotMarginRight,dotMarginBottom);

        } finally {
            a.recycle();
        }
    }

    //Getters and setters
    public int getDotBorderWidth() {
        return this.dotBorderWidth;
    }

    public void setDotBorderSize(int dotBorderWidth) {
        this.dotBorderWidth = dotBorderWidth;
        GradientDrawable bgShape = (GradientDrawable)dot.getBackground();
        bgShape.setStroke(this.dotBorderWidth, this.dotBorderColor);
    }

    public int getDotColor() {
        return this.dotColor;
    }

    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
        GradientDrawable bgShape = (GradientDrawable)dot.getBackground();
        bgShape.setColor(this.dotColor);
    }

    public int getDotBorderColor() {
        return this.dotBorderColor;
    }

    public void setDotBorderColor(int dotBorderColor) {
        this.dotBorderColor = dotBorderColor;
        GradientDrawable bgShape = (GradientDrawable)dot.getBackground();
        bgShape.setStroke(this.dotBorderWidth, this.dotBorderColor);
    }

    public int getDotSize() {
        return this.dotSize;
    }

    public void setDotSize(int dotSize) {
        this.dotSize = dotSize;
        this.dot.getLayoutParams().width = dotSize;
        this.dot.getLayoutParams().height = dotSize;
    }

    public int getDotMarginTop() {
        return this.dotMarginTop;
    }

    public void setDotMarginTop(int dotMarginTop) {
        this.dotMarginTop = dotMarginTop;
        ((LayoutParams)this.dot.getLayoutParams()).setMargins(dotMarginLeft,dotMarginTop, dotMarginRight,dotMarginBottom);
    }

    public int getDotMarginBottom() {
        return dotMarginBottom;
    }

    public void setDotMarginBottom(int dotMarginBottom) {
        this.dotMarginBottom = dotMarginBottom;
        ((LayoutParams)this.dot.getLayoutParams()).setMargins(dotMarginLeft,dotMarginTop, dotMarginRight,dotMarginBottom);
    }

    public int getDotMarginLeft() {
        return dotMarginLeft;
    }

    public void setDotMarginLeft(int dotMarginLeft) {
        this.dotMarginLeft = dotMarginLeft;
        ((LayoutParams)this.dot.getLayoutParams()).setMargins(dotMarginLeft,dotMarginTop, dotMarginRight,dotMarginBottom);
    }

    public int getDotMarginRight() {
        return dotMarginRight;
    }

    public void setDotMarginRight(int dotMarginRight) {
        this.dotMarginRight = dotMarginRight;
        ((LayoutParams)this.dot.getLayoutParams()).setMargins(dotMarginLeft,dotMarginTop, dotMarginRight,dotMarginBottom);
    }

    //Listeners
    public void setOnDotClickListener(OnDotClickListener onDotClickListener){
        this.mCallbackClick = onDotClickListener;
    }

    public void setOnDotLongClickListener(OnDotLongClickListener onDotLongClickListener){
        this.mCallbackLongClick = onDotLongClickListener;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
