package com.example.facebookphotopicker.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.facebookphotopicker.R;
import com.example.facebookphotopicker.utils.ScreenUtil;

public class HeaderView extends RelativeLayout {

    private TextView titleView;
    private RelativeLayout rightView;
    private View barBgView;

    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundResource(R.color.transparent);
        barBgView = new View(context);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtil.dip2px(context, 48));
            barBgView.setLayoutParams(lParams);
        } else {
            LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtil.dip2px(context, 73));
            barBgView.setLayoutParams(lParams);
        }
        barBgView.setBackgroundResource(R.drawable.bg_topbar);
        this.addView(barBgView);

        View view = LayoutInflater.from(context).inflate(R.layout.v_title, this, false);
        LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtil.dip2px(context, 48));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            lParams.topMargin = ScreenUtil.dip2px(context, 0);
        } else {
            lParams.topMargin = ScreenUtil.dip2px(context, 25);
        }
        view.setLayoutParams(lParams);
        rightView = (RelativeLayout) view.findViewById(R.id.title_rightview);
        titleView = (TextView) view.findViewById(R.id.tv_title);

        this.addView(view);
    }

    public void setTitle(CharSequence title) {
        titleView.setText(title);
    }

    public void setRightView(View view) {
        rightView.addView(view);
    }

}
