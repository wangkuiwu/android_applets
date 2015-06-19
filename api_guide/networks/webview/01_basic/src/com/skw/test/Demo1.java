package com.skw.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * WebView测试程序
 * @desc 
 * 1. 加载www.baidu.com
 * 2. 点击www.baidu.com中的按钮跳转的链接都在当前WebView中打开
 * 3. 对返回键进行拦截处理。若有上级界面，则跳转到上级页面
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
@SuppressLint("SetJavaScriptEnabled")
public class Demo1 extends Activity {

    private WebView mWebView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo2);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);  // 启用JS功能

        mWebView.setWebViewClient(new MyWebViewClient());   // 设置"链接的打开方式(默认是用浏览器打开)"为当前WebView
        mWebView.loadUrl("http://www.baidu.com/");          // 加载百度
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
