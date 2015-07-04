package com.example.test;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.ImageDownloader;
import com.facebook.internal.ImageRequest;
import com.facebook.internal.ImageResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * FacebookSdk登录之后，获取用户的照片
 *
 * 打印AccessToken和Permission
 */
public class Demo4 extends FragmentActivity {
    private static final String TAG = "##skywang-Demo4";

    private ImageView mImgPhoto;
    private TextView mTvInfo;
    private CallbackManager mCallbackManager;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bm = (Bitmap)msg.obj;
            mImgPhoto.setImageBitmap(bm) ;  
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate use Login Button");
        super.onCreate(savedInstanceState);

        // 在setContentView之前调用sdkInitialize()
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.demo4);
        mImgPhoto = (ImageView) findViewById(R.id.img_photo);

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "log in success");
                // App code
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "login cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "login error");
                // App code
            }
        });    
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserInfo();
    }

    /**
     * 获取用户信息
     */
    private void fetchUserInfo() {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Log.d(TAG, "fetchUserInfo accessToken="+accessToken);
        if (accessToken != null) {
            // 打印AccessToken和Permission
            Log.d(TAG, "fetchUserInfo : accessToken="+accessToken.getToken()+", permissions="+ accessToken.getPermissions());

            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            try {
                                // 获取图片的地址(picUri)
                                JSONObject jPic = me.getJSONObject("picture").getJSONObject("data");
                                final String picUri =  jPic.getString("url");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 根据地址获取到图片Bitmap
                                        Bitmap bm = null;
                                        try {
                                            InputStream is = new URL(picUri).openStream();
                                            bm = BitmapFactory.decodeStream(is);
                                            is.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        mHandler.sendMessage(mHandler.obtainMessage(0, bm));
                                    }
                                }).start();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            // 设置参数，获取图片
            parameters.putString("fields", "picture");
            request.setParameters(parameters);
            Log.d(TAG, "fetchUserInfo execute!");
            GraphRequest.executeBatchAsync(request);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
