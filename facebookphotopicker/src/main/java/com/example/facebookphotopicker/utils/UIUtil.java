package com.example.facebookphotopicker.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.facebookphotopicker.R;


/**
 * Created by zhangxiaoyu on 16/7/19.
 */
public class UIUtil {

    public static TextView getTextButton(Context context, CharSequence text, int resourceId) {
        TextView textView = new TextView(context);
        RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ScreenUtil.dip2px(context, 44));
        textView.setLayoutParams(lParams);

        textView.setBackgroundResource(R.color.transparent);
        textView.setText(text);
        textView.setTextColor(context.getResources().getColor(resourceId));
        textView.setTextSize(17);
        textView.setGravity(Gravity.CENTER);
        textView.setSingleLine();

        return textView;
    }
}
