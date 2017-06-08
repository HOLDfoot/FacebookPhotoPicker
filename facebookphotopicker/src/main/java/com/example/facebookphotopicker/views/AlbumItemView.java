package com.example.facebookphotopicker.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.facebookphotopicker.R;
import com.example.facebookphotopicker.models.AlbumModel;


/**
 * Created by ZhuMingren on 17/6/1.
 */
public class AlbumItemView extends RelativeLayout {

    private ImageView coverPhoto;
    private TextView mainTitle;
    private TextView subTitle;

    private Context mContext;

    public AlbumItemView(Context context) {
        super(context);
        initView(context);
    }

    public AlbumItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AlbumItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album, this);
        mContext = context;
        coverPhoto = (ImageView) view.findViewById(R.id.iv_album_cover);
        mainTitle = (TextView) view.findViewById(R.id.tv_album_name);
        subTitle = (TextView) view.findViewById(R.id.tv_photo_number);
    }

    public void setModel(AlbumModel albumModel) {
        Glide.with(mContext)
                .load(albumModel.coverPhotoUrl)
                .placeholder(R.drawable.white)
                .error(R.drawable.white)
                .into(coverPhoto);
        mainTitle.setText(albumModel.name);
        subTitle.setText(String.valueOf(albumModel.count));
    }
}
