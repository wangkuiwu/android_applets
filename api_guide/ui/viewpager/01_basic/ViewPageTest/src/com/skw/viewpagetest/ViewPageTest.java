package com.skw.viewpagetest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import java.util.List;
import java.util.ArrayList;

public class ViewPageTest extends Activity {
    /** Called when the activity is first created. */
    
    private ViewPager mViewPager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initViews() ;
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        List<View> views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.page_one, null));
        views.add(inflater.inflate(R.layout.page_two, null));
        views.add(inflater.inflate(R.layout.page_three, null));

        // 初始化Adapter
        PagerAdapter adapter = new MyPagerAdapter(views);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);
    }


    private class MyPagerAdapter extends PagerAdapter {

        // 界面列表
        private List<View> views;

        public MyPagerAdapter(List<View> views) {
            this.views = views;
        }   

        // 销毁arg1位置的界面
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }   

        // 获得当前界面数
        @Override
        public int getCount() {
            return views.size();
        }   

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        // 初始化arg1位置的界面
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0); 
            return views.get(arg1);
        }
    }
}
