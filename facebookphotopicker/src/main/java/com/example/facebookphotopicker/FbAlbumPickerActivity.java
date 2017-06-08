package com.example.facebookphotopicker;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.example.facebookphotopicker.models.AlbumModel;
import com.example.facebookphotopicker.utils.CommonUtil;
import com.example.facebookphotopicker.utils.FacebookRequestUtil;
import com.example.facebookphotopicker.utils.UIUtil;
import com.example.facebookphotopicker.views.AlbumItemView;
import com.example.facebookphotopicker.views.HeaderView;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

public class FbAlbumPickerActivity extends AppCompatActivity {

    private HeaderView mTitleView;
    private List<AlbumModel> modelList = null;
    private ListView listView;
    private KProgressHUD mProgressbar;
    private boolean cancelAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        initView();
    }

    private void initView() {
        setContentView(R.layout.ac_fb_album_picker);
        initTitle();
        listView = (ListView) findViewById(R.id.lv_album_picker);
        initListView();
    }

    private void initListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("zhumr", "onItemClick ======");
                Intent intent = new Intent(FbAlbumPickerActivity.this, FbPhotoPickerActivity.class);
                AlbumModel albumModel = (AlbumModel) listView.getAdapter().getItem(position);
                intent.putExtra("album_id", albumModel.id);
                startActivity(intent);
            }
        });


        showProgressDialog();
        final List<AlbumModel> modelList = new ArrayList<>();
        FacebookRequestUtil.getAlbums(this, new FacebookRequestUtil.MultiCallback() {
            @Override
            public void callback(Object object, boolean isComplete) {
                Log.d("zhumr", "initListView callback isComplete = " + isComplete);
                if (cancelAll) return;
                if (object == null && isComplete) {
                    // Error
                    CommonUtil.showToast(FbAlbumPickerActivity.this, "FacebookRequestError!");
                    dismissProgressDialog();
                    cancelAll = true; // 一旦一个出错就取消全部回调的执行
                    return;
                }
                if (object != null) {
                    AlbumModel albumModel = (AlbumModel) object;
                    if (!TextUtils.isEmpty(albumModel.coverPhotoId)) {
                        modelList.add(albumModel);
                    }
                    Log.d("zhumr", "albumModel = " + albumModel.toString());
                }
                if (isComplete) {
                    listView.setAdapter(new MyAdapter(FbAlbumPickerActivity.this, modelList));
                    dismissProgressDialog();
                }
            }
        });
    }

    private void initTitle() {
        mTitleView = (HeaderView) findViewById(R.id.titleview);
        mTitleView.setTitle("Facebook Albums");

        TextView rightButton = UIUtil.getTextButton(this, "Cancel", R.color.titleMenuColorEdit);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mTitleView.setRightView(rightButton);
    }

    private class MyAdapter extends BaseAdapter {

        private List<AlbumModel> modelList = null;
        private Context mContext;

        public MyAdapter(FbAlbumPickerActivity context, List<AlbumModel> modelList) {
            super();
            this.modelList = modelList;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return modelList.size();
        }

        @Override
        public Object getItem(int position) {
            return modelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = new AlbumItemView(mContext);
            }
            AlbumModel albumModel = (AlbumModel) getItem(position);
            Log.d("zhumr", "getView albumModel = " + albumModel.toString());
            ((AlbumItemView) convertView).setModel(albumModel);

            return convertView;
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
