package com.example.facebookphotopicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.facebookphotopicker.manager.SignManager;

public class FacebookSignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_signin);
        View view = findViewById(R.id.btn_signin_facebook);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignin(v);
            }
        });
    }

    public void onClickSignin(View view) {
        Log.d("zhumr", "onClick SignIn");
        SignManager.startFacebookLogin(this, new SignManager.SigninCallback() {
            @Override
            public void onComplete(SignManager.HttpState httpState) {
                if (httpState == SignManager.HttpState.SUCCESS) {
                    Log.d("zhumr", "onClick SignIn SUCCESS");
                    startActivity(new Intent(FacebookSignInActivity.this, FbAlbumPickerActivity.class));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("zhumr", "onActivityResult");
        SignManager.getFacebookCallbackManager().onActivityResult(requestCode, resultCode, data);
        SignManager.releaseCallbackManager();
    }
}
