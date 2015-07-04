package com.example.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
 * FacebookSdk登录之后，获取email信息。需要额外添加Facebook权限。
 */
public class Demo3 extends FragmentActivity {
    private static final String TAG = "##skywang-Demo3";

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

        setContentView(R.layout.demo3);

        mTvInfo = (TextView) findViewById(R.id.tv_info);

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        // 添加读取email的权限
        loginButton.setReadPermissions("email");
        // loginButton.setReadPermissions(Arrays.asList("user_friends", "email"));
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "log in success: permissions="+ AccessToken.getCurrentAccessToken().getPermissions());
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
                                String email = me.getString("email");
                                mHandler.sendMessage(mHandler.obtainMessage(0, email));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            // 设置参数，获取id, name和email
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
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
