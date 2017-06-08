package com.example.facebookphotopicker;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.facebookphotopicker.models.FbPhoto;
import com.example.facebookphotopicker.utils.CommonUtil;
import com.example.facebookphotopicker.utils.FacebookRequestUtil;
import com.example.facebookphotopicker.utils.UIUtil;
import com.example.facebookphotopicker.views.HeaderView;
import com.example.facebookphotopicker.views.PhotoItemView;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

public class FbPhotoPickerActivity extends AppCompatActivity {

    private HeaderView mTitleView;
    private GridView mGridView;
    private KProgressHUD mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        setContentView(R.layout.ac_fb_photo_picker);
        setTitleView();
        mGridView = (GridView) findViewById(R.id.gv_photo_picker);
        initGridView();
    }

    private void initGridView() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FbPhoto fbPhoto = (FbPhoto) mGridView.getAdapter().getItem(position);
                String photoUrl = fbPhoto.url;
                CommonUtil.showToast(FbPhotoPickerActivity.this, "You have got the photo's url: " + photoUrl);
            }
        });

        String currentAlbum = getIntent().getStringExtra("album_id");
        Log.d("zhumr", "currentAlbum = " + currentAlbum);
        showProgressDialog();
        FacebookRequestUtil.getAlbumPhotos(currentAlbum, new FacebookRequestUtil.ObjectCallback() {
            @Override
            public void callback(Object object) {
                if (object != null) {
                    List<FbPhoto> photoList = (List<FbPhoto>) object;
                    FbPhotoAdapter fbPhotoAdapter = new FbPhotoAdapter(FbPhotoPickerActivity.this, photoList);
                    mGridView.setAdapter(fbPhotoAdapter);
                } else {
                    CommonUtil.showToast(FbPhotoPickerActivity.this, "FacebookRequestError!");
                }
                dismissProgressDialog();
            }
        });
    }

    private void setTitleView() {
        mTitleView = (HeaderView) findViewById(R.id.titleview);
        mTitleView.setTitle("Facebook Photos");
        TextView rightButton = UIUtil.getTextButton(this, "Cancel", R.color.titleMenuColorEdit);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mTitleView.setRightView(rightButton);
    }

    private class FbPhotoAdapter extends BaseAdapter {

        private Context mContext;
        private List<FbPhoto> fbPhotoList = null;

        public FbPhotoAdapter(Context context, List<FbPhoto> fbPhotoList) {
            this.mContext = context;
            this.fbPhotoList = fbPhotoList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = new PhotoItemView(mContext);
            }
            FbPhoto fbPhoto = getItem(position);
            ((PhotoItemView) convertView).setModel(fbPhoto);

            return convertView;
        }

        @Override
        public int getCount() {
            return fbPhotoList.size();
        }

        @Override
        public FbPhoto getItem(int position) {
            return fbPhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    private void showProgressDialog() {
        if (mProgressbar == null) {
            mProgressbar = KProgressHUD.create(this);
            mProgressbar.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(false);
        }

        if (mProgressbar != null && !mProgressbar.isShowing())
            mProgressbar.show();
    }

    private void dismissProgressDialog() {
        if (mProgressbar != null && mProgressbar.isShowing())
            mProgressbar.dismiss();
    }
}
