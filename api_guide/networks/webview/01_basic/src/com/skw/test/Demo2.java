package com.skw.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * WebView测试程序
 * @desc 加载assets中HTML
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
@SuppressLint("SetJavaScriptEnabled")
public class Demo2 extends Activity {
    private static final String TAG = "##skywang-Demo1";

    private WebView mWebView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo1);

        Log.d(TAG, "onCreate open baidu");

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);      // 启用JS功能
        mWebView.loadUrl("file:///android_asset/demo2.html");   // 加载网页
    }
}
