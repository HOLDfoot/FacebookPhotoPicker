package com.example.facebookphotopicker.manager;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by ZhuMingren on 2017/6/6.
 */

public class SignManager {

    public static CallbackManager callbackManager;

    public static void startFacebookLogin(final Context context, final SigninCallback signinCallback) {
        callbackManager = CallbackManager.Factory.create();
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("zhumr", "loginResult = ");
                Log.d("zhumr", "getRecentlyGrantedPermissions = " + loginResult.getRecentlyGrantedPermissions());
                if (signinCallback != null) {
                    signinCallback.onComplete(HttpState.SUCCESS);
                }
            }

            @Override
            public void onCancel() {
                Log.d("zhumr", "loginResult = onCancel");
                if (signinCallback != null) {
                    signinCallback.onComplete(HttpState.NOTICE);
                }
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("zhumr", "loginResult = onError " + error.getLocalizedMessage());
                if (signinCallback != null) {
                    signinCallback.onComplete(HttpState.FAIL);
                }
            }
        });
        Log.d("zhumr", "onClick logInWithReadPermissions = ");
        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile", "user_photos"));
    }

    public static CallbackManager getFacebookCallbackManager() {
        if (callbackManager != null) {
            return callbackManager;
        } else {
            return null;
        }
    }

    public static void releaseCallbackManager() {
        if (callbackManager != null) {
            callbackManager = null;
        }
    }

    public interface SigninCallback{
        void onComplete(HttpState httpState);
    }
    public enum HttpState{
        SUCCESS,NOTICE,FAIL
    }
}
