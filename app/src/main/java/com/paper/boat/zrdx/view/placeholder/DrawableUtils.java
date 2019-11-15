package com.paper.boat.zrdx.view.placeholder;

import android.annotation.TargetApi;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

/**
 * @author SamLeung
 */
public class DrawableUtils {
    private DrawableUtils(){
        throw new UnsupportedOperationException("无法实例化。");
    }

    /*创建矩形可绘制*/
    public static GradientDrawable createRectangleDrawable(int color, float cornerRadius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(cornerRadius);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    /*创建矩形可绘制*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static GradientDrawable createRectangleDrawable(int[] colors, float cornerRadius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(cornerRadius);
        gradientDrawable.setColors(colors);
        return gradientDrawable;
    }

    /*创建椭圆形可绘制*/
    public static GradientDrawable createOvalDrawable(int color) {
        GradientDrawable gradientDrawable =  new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    /*创建椭圆形可绘制*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static GradientDrawable createOvalDrawable(int[] colors) {
        GradientDrawable gradientDrawable =  new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColors(colors);
        return gradientDrawable;
    }
}
