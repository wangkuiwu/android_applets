package com.skw.textviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * TextView文本变化的监听测试程序
 *
 * @desc 通过TextWatcher监听文本变化
 */
public class TextWatcherActivity extends Activity {
    private static final String TAG = "##skywang-TextWatcherActivity";

    private TextView mTextView;
    private Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_watcher);

        mTextView = (TextView) findViewById(R.id.tv);
        mTextView.addTextChangedListener(new MyTextWatcher());

        mButton = (Button) findViewById(R.id.btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int num = (int)(Math.random()*100);
                mTextView.append(String.valueOf(num));
            }
        });
    }

    private class MyTextWatcher implements TextWatcher {

        // cs中从start开始的count个字符即将被after个字符替换
        @Override
        public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
        }

        // cs中从start开始的before个字符刚刚被count个字符替换
        @Override
        public void onTextChanged(CharSequence cs, int start, int before, int count) {
            Log.d(TAG, "onTextChanged, cs="+cs+", start="+start+", before="+before+", count="+count);
        }

        @Override
        public void afterTextChanged(Editable arg0) {
        }
    }
}
