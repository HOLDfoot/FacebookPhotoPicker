package com.example.facebookphotopicker.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.facebookphotopicker.models.AlbumModel;
import com.example.facebookphotopicker.models.FbPhoto;
import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FacebookRequestUtil {

    private static int totalTimes;

    private static int times;
    private static final String PARAMETER_FIELDS = "fields";
    private static final String PARAMETER_ALBUM_PHOTOS = "link,picture,images";
    private static final String PARAMETER_ALBUM = "cover_photo,picture,count,name";

    private static final String URL_ALBUMS = "/%s/albums";
    private static final String URL_ALBUM_PHOTOS = "/%s/photos";

    private static final String URL_ALBUM = "/%s";

    public interface ObjectCallback {
        void callback(Object object);
    }

    public interface MultiCallback {
        void callback(Object object, boolean isComplete);
    }

    public interface CompleteCallback {
        void onComplete(boolean isSuccess, JSONObject jsonObject);
    }

    public static void getAlbums(final Context context, final MultiCallback objectCallback) {
        getUserAlbums(new ObjectCallback() {
            @Override
            public void callback(Object object) {
                if (object != null) {
                    List<AlbumModel> albumModels = (List<AlbumModel>) object;
                    totalTimes = albumModels.size();
                    Log.d("zhumr", "totalTimes = " + totalTimes);
                    for (AlbumModel albumModel : albumModels) {
                        Log.d("zhumr", "getAlbums name = " + albumModel.name);
                        getAlbum(albumModel, objectCallback);
                    }
                } else {
                    //CommonUtil.showToast(context, "FacebookRequestError!");
                    objectCallback.callback(null, true); // 出错，给提示，取消进度条
                }
            }
        });
    }


    public static void getAlbumPhotos(String albumId, final ObjectCallback simpleCallback) {
        String url = String.format(URL_ALBUM_PHOTOS, albumId);
        Bundle parameters = new Bundle();
        parameters.putString(PARAMETER_FIELDS, PARAMETER_ALBUM_PHOTOS);

        final List<FbPhoto> photoList = new ArrayList<>();
        graphRequest(url, parameters, HttpMethod.GET, new CompleteCallback() {
            @Override
            public void onComplete(boolean isSuccess, JSONObject jsonObject) {
                Log.d("zhumr", "getAlbumPhotos = onComplete");
                if (isSuccess) {
                    Log.d("zhumr", "getAlbumPhotos = " + jsonObject.toString());
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            FbPhoto fbPhoto = new FbPhoto();
                            JSONObject photoJson = jsonArray.optJSONObject(i);
                            fbPhoto.pictureUrl = photoJson.optString("picture");
                            fbPhoto.id = photoJson.optString("id");
                            JSONArray imagesArray = photoJson.optJSONArray("images");
                            fbPhoto.url = imagesArray.optJSONObject(0).optString("source");
                            photoList.add(fbPhoto);
                        }
                    }
                    if (simpleCallback != null) {
                        simpleCallback.callback(photoList);
                    }
                } else {
                    if (simpleCallback != null) {
                        simpleCallback.callback(null);
                    }
                }
            }
        });
    }

    public static void getAlbum(final AlbumModel albumModel, final MultiCallback objectCallback) {
        String url = String.format(URL_ALBUM, albumModel.id);

        Bundle parameters = new Bundle();
        parameters.putString(PARAMETER_FIELDS, PARAMETER_ALBUM);
        graphRequest(url, parameters, HttpMethod.GET, new CompleteCallback() {
            @Override
            public void onComplete(boolean isSuccess, JSONObject jsonObject) {
                if (isSuccess) {
                    Log.d("zhumr", "getAlbum = " + jsonObject.toString());

                    String coverPhotoId = null;
                    JSONObject coverPhoto = jsonObject.optJSONObject("cover_photo");
                    if (coverPhoto != null) {
                        coverPhotoId = coverPhoto.optString("id");
                    }
                    String coverPhotoUrl = jsonObject.optJSONObject("picture").optJSONObject("data").optString("url");
                    String name = jsonObject.optString("name");
                    Log.d("zhumr", "getAlbum coverPhotoUrl = " + coverPhotoUrl + " name = " + name);
                    int count = jsonObject.optInt("count");
                    albumModel.coverPhotoId = coverPhotoId;
                    albumModel.coverPhotoUrl = coverPhotoUrl;
                    albumModel.name = name;
                    albumModel.count = count;
                    if (objectCallback != null) {
                        times ++;
                        Log.d("zhumr", "times ++ = " + times);
                        if (times < totalTimes) {
                            objectCallback .callback(albumModel, false);
                        } else if (times == totalTimes) {
                            objectCallback.callback(albumModel, true);
                            // 重置, 下次调用的时候还会用到
                            times = 0;
                            totalTimes = 0;
                        }
                    }
                } else {
                    if (objectCallback != null) {
                        objectCallback.callback(null, true);
                    }
                }
            }
        });
    }

    public static List<AlbumModel> getUserAlbums(final ObjectCallback objectCallback) {
        String uid = AccessToken.getCurrentAccessToken().getUserId();
        String url = String.format(URL_ALBUMS, uid);
        Log.d("zhumr url ", "url  = " + url);

        final List<AlbumModel> albumModels = new ArrayList<AlbumModel>();
        graphRequest(url, null, HttpMethod.GET, new CompleteCallback() {
            @Override
            public void onComplete(boolean isSuccess, JSONObject jsonObject) {
                if (isSuccess) {
                    Log.d("zhumr", "albums = " + jsonObject.toString());
                    JSONArray data = jsonObject.optJSONArray("data");
                    for(int i = 0; i < data.length(); i ++) {
                        JSONObject object = data.optJSONObject(i);
                        AlbumModel model = new AlbumModel();
                        model.name = object.optString("name");
                        model.id = object.optString("id");
                        Log.d("zhumr", "getUserAlbums object = " + object.toString() + " model = " + model.toString());
                        albumModels.add(model);
                    }
                    if (objectCallback != null) {
                        objectCallback.callback(albumModels);
                    }
                } else {
                    if (objectCallback != null) {
                        objectCallback.callback(null);
                    }
                }
            }
        });

        return albumModels;
    }

    private static void graphRequest(String url, Bundle parameters, HttpMethod method, final CompleteCallback completeCallback) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                url,
                parameters,
                method,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        JSONArray jsonArray = response.getJSONArray();
                        JSONObject jsonObject = response.getJSONObject();
                        String rawResponse = response.getRawResponse();
                        FacebookRequestError error = response.getError();
                        if (error != null) {
                            Log.d("zhumr", "Error code =" + error.getErrorCode() + " Error message = " + error.getErrorMessage());
                            completeCallback.onComplete(false, null);
                        }
                        if (jsonArray != null) {
                            Log.d("zhumr", "GraphRequest jsonArray = " + jsonArray.toString());
                        }
                        if (jsonObject != null) {
                            completeCallback.onComplete(true, jsonObject);
                            String zhuanyi = jsonObject.toString().replaceAll("\\\\", "");
                            Log.d("zhumr", zhuanyi);
                        }
                        if (rawResponse != null) {
                            Log.d("zhumr", "GraphRequest rawResponse = " + rawResponse.toString());
                        }
                    }
                }
        ).executeAsync();
    }
}
