package com.phile.yrj.testlinedotrecyclerview;

import android.graphics.Color;


import java.util.ArrayList;

public class CustomAdapter extends DotLineRecyclerAdapter {

    public CustomAdapter(ArrayList<RecyclerData> data) {
        super(data);
    }

    public CustomAdapter(ArrayList<RecyclerData> data, ArrayList<Integer> colorList) {
        super(data, colorList);
    }

    public CustomAdapter(ArrayList<RecyclerData> data, int dotMarginLeft) {
        super(data, dotMarginLeft);
    }

    public CustomAdapter(ArrayList<RecyclerData> data, int dotMarginLeft, ArrayList<Integer> colorList) {
        super(data, dotMarginLeft, colorList);
    }

    @Override
    public int getDotBorderColor() {
        return Color.RED;
    }

    @Override
    public int getDotBorderSize() {
        return super.getDotBorderSize();
    }

    @Override
    public int getDotColor() {
        return super.getDotColor();
    }

    @Override
    public int getDotSize() {
        return super.getDotSize();
    }

    @Override
    public int getImageError() {
        return super.getImageError();
    }

    @Override
    public RecyclerData getItemAtPosition(int position) {
        return super.getItemAtPosition(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getMessageBackground() {
        return super.getMessageBackground();
    }

    @Override
    public int getMessageBackgroundPressed() {
        return super.getMessageBackgroundPressed();
    }

    @Override
    public int getSeparator() {
        return super.getSeparator();
    }

    @Override
    public int getTextSubtitleColor() {
        return super.getTextSubtitleColor();
    }

    @Override
    public int getTextSubtitleSize() {
        return super.getTextSubtitleSize();
    }

    @Override
    public int getTextTitleColor() {
        return super.getTextTitleColor();
    }

    @Override
    public int getTextTitleSize() {
        return super.getTextTitleSize();
    }
}
