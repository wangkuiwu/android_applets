package com.skw.test;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Relative测试程序
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class Demo1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo1);

        ImageView imgView = (ImageView)findViewById(R.id.img_view);
        Uri imgUri = Uri.parse("android.resource://com.skw.test/drawable/ic_launcher");
        imgView.setImageURI(imgUri);
    }
}
