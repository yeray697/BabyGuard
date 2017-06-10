package com.yeray697.dotLineRecyclerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Recycler view component
 * @author yeray697
 * @version 1.0
 * Created on 21/12/16.
 */
public class DotLineRecyclerView extends RelativeLayout {
    private View line;
    private RecyclerView recyclerView;

    private int margin;
    private int lineColor;

    //Constructors
    public DotLineRecyclerView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initialize(attrs);
    }

    public DotLineRecyclerView(Context context) {
        super(context);
        initialize(null);
    }

    public DotLineRecyclerView(Context context, AttributeSet attrs) {
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
        li.inflate(R.layout.dot_line_recycler_view, this, true);

        recyclerView = (RecyclerView) findViewById(R.id.rvCustomDot);
        line = findViewById(R.id.lineView);

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
        setLineColor(Color.BLACK);
    }

    /**
     * Get attributes from xml declaration
     * @param attrs XML Attribues
     */
    private void getXMLValues(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DotLineRecyclerView,
                0, 0);

        try {
            setLineColor(a.getColor(R.styleable.DotLineRecyclerView_lineColor, Color.BLACK));
        } finally {
            a.recycle();
        }
    }

    /**
     * Set the left margin from the line
     */
    private void setMarginLine(int dotSize){
        if (recyclerView.getAdapter() != null) {
            ViewGroup.MarginLayoutParams relativeParams = (ViewGroup.MarginLayoutParams) line.getLayoutParams();
            //int lineWidth = (line.getWidth() / 2);
            //relativeParams.setMargins((margin - lineWidth), 0, 0, 0);
            relativeParams.setMargins((margin - dotSize), 0, 0, 0);
            line.setLayoutParams(relativeParams);
            line.requestLayout();
        }
    }

    //Getters and setters

    /**
     * Get the recyclerView
     * @return RecyclerView used
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * Get the color used in the line
     * @return Color used
     */
    public int getLineColor() {
        return lineColor;
    }

    /**
     * Set the color used in the line
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        line.setBackgroundColor(this.lineColor);
    }

    /**
     * Set the adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter){
        margin = (int)(Utils.dpToPx(getContext(),((DotLineRecyclerAdapter) adapter).getDotMarginLeft())) + 5;
        margin += ((int)(Utils.dpToPx(getContext(),((DotLineRecyclerAdapter) adapter).getDotSize()))) * 10 / 2;
        recyclerView.setAdapter(adapter);
        setMarginLine(0);
    }

    /**
     * Set the layout manager
     */
    public void setLayoutManager(LinearLayoutManager layoutManager) {
        this.recyclerView.setLayoutManager(layoutManager);
    }

    public void setLineWidth(int width){
        int widthConverted = (int) Utils.dpToPx(getContext(),width);
        line.getLayoutParams().width = widthConverted;
        setMarginLine((widthConverted/2));

    }
}
