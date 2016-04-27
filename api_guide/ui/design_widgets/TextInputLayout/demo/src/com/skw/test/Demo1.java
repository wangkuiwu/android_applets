package com.skw.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.View.OnClickListener;

public class Demo1 extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout mTvLayoutEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo1);

        mTvLayoutEmail = (TextInputLayout) findViewById(R.id.textInputEmail);
        mTvLayoutEmail.setErrorEnabled(false);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnError).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                break;
            case R.id.btnError:
                mTvLayoutEmail.setError("error");
                break;
            case R.id.textInputEmail:
                break;
            case R.id.textInputPassword:
                break;
        }
    }
}
