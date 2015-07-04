package com.example.test;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

/**
 * FacebookSdk登录之后，获取基本的用户信息(id，用户名，链接，照片)，这些信息不需要额外的添加权限。
 *
 * 打印AccessToken和Permission
 */
public class Demo2 extends FragmentActivity {
    private static final String TAG = "##skywang-Demo2";

    private TextView mTvInfo;
    private CallbackManager mCallbackManager;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String text = (String) msg.obj;
            mTvInfo.setText(text);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate use Login Button");
        super.onCreate(savedInstanceState);

        // 在setContentView之前调用sdkInitialize()
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.demo2);

        mTvInfo = (TextView) findViewById(R.id.tv_info);

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
                                StringBuilder sb = new StringBuilder();
                                String id = me.getString("id");
                                String name = me.getString("name");
                                String link = me.getString("link");
                                JSONObject jPic = me.getJSONObject("picture").getJSONObject("data");
                                // JSONObject jPic = me.get("picture").getAsJsonObject();
                                String picUri =  jPic.getString("url");
                                Log.d(TAG, "onCompleted, response="+response);
                                Log.d(TAG, "onCompleted, id="+id+", name="+name+", link="+link);
                                mHandler.sendMessage(mHandler.obtainMessage(0, 
                                      sb.append("id=").append(id).append("\n")
                                        .append("name=").append(name).append("\n")
                                        .append("link=").append(link).append("\n")
                                        .append("pic=").append(picUri).append("\n")
                                        .toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            // 设置参数，获取id, name等信息
            parameters.putString("fields", "id,name,link,picture");
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
