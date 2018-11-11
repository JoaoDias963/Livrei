package com.jd.livrei0.Utils;

import android.graphics.Color;

public class GradientDrawable {
    public android.graphics.drawable.GradientDrawable setBorderRoundedEDITTEXT(){
        android.graphics.drawable.GradientDrawable shape = new android.graphics.drawable.GradientDrawable();
        shape.setColor(Color.WHITE);
        shape.setCornerRadius(70);
        shape.setStroke(2, Color.BLACK);
        return shape;
    }

    public android.graphics.drawable.GradientDrawable setBorderRoundedBUTTONConfirma(){
        android.graphics.drawable.GradientDrawable shape = new android.graphics.drawable.GradientDrawable();
        shape.setColor(Color.parseColor("#ea6a01"));
        shape.setCornerRadius(70);
        shape.setStroke(5, Color.parseColor("#816459"));
        return shape;
    }

    public android.graphics.drawable.GradientDrawable setBorderRoundedBUTTONCancela(){
        android.graphics.drawable.GradientDrawable shape = new android.graphics.drawable.GradientDrawable();
        shape.setColor(Color.parseColor("#e59752"));
        shape.setCornerRadius(70);
        shape.setStroke(5, Color.parseColor("#7a502c"));
        return shape;
    }
}
