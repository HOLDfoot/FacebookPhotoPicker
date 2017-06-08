package com.example.facebookphotopicker.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.facebookphotopicker.R;
import com.example.facebookphotopicker.models.FbPhoto;
import com.example.facebookphotopicker.utils.ScreenUtil;

/**
 * Created by ZhuMingren on 17/6/1.
 */
public class PhotoItemView extends RelativeLayout {

    private Context mContext;
    private ImageView imageView;

    public PhotoItemView(Context context) {
        super(context);
        initView(context);
    }

    public PhotoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PhotoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.photo_album, this);
        mContext = context;
        imageView = (ImageView) findViewById(R.id.iv_fb_photo);

        int count = 4;
        int screenWidth = ScreenUtil.getScreenWidth(context);
        float itemWidth = (screenWidth - ScreenUtil.dip2px(mContext, 1) * (count - 1)) / count;
        AbsListView.LayoutParams params = new AbsListView.LayoutParams((int) itemWidth, (int) itemWidth);
        setLayoutParams(params);
    }

    public void setModel(FbPhoto fbPhoto) {
        Glide.with(mContext)
                .load(fbPhoto.pictureUrl)
                .placeholder(R.drawable.white)
                .error(R.drawable.white)
                .into(imageView);
    }
}
