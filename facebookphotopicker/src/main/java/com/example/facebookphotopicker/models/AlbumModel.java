package com.example.facebookphotopicker.models;

/**
 * Created by ZhuMingren on 2017/5/31.
 */

public class AlbumModel {
    public String name;
    public String id;
    public String coverPhotoId; // 有可能为空
    public String coverPhotoUrl;
    public int count;

    @Override
    public String toString() {
        return "AlbumModel: name = " + name + " id = " + id + " coverPhotoUrl = " + coverPhotoUrl + " count = " + count + " coverPhotoId = " +
                (coverPhotoId == null ? "null" : coverPhotoId);
    }
}
