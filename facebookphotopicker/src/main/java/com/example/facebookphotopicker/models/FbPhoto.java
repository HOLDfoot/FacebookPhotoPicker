package com.example.facebookphotopicker.models;

/**
 * Created by ZhuMingren on 2017/5/31.
 */

public class FbPhoto {
    public String name;
    public String id;
    public String pictureUrl; // 小图片
    public String url; // 大图片

    @Override
    public String toString() {
        return "FbPhoto: name = " + name + " id = " + id;
    }
}
