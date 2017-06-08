package com.example.facebookphotopicker.utils;

import android.content.Context;

public class ScreenUtil {

    /**
     * 屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * dip转化为px
     *
     * @param context
     * @param dipValue 要转化的dip
     * @return int 单位px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
}

