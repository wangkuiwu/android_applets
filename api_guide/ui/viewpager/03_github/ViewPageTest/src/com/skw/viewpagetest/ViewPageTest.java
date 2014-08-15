package com.skw.viewpagetest;

import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.graphics.Color;

import com.skw.viewpagetest.astuetz.PagerSlidingTabStrip;

import java.util.List;
import java.util.ArrayList;

public class ViewPageTest extends FragmentActivity {
    /** Called when the activity is first created. */
    
    private DisplayMetrics dm;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip tabs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        dm = getResources().getDisplayMetrics();
        initViews() ;
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        List<Fragment> flist = new ArrayList<Fragment>();
        // 初始化Fragment
        flist.add(MyFragment.newInstance("Page 1"));
        flist.add(MyFragment.newInstance("Page 2"));
        flist.add(MyFragment.newInstance("Page 3"));

        // 初始化Adapter
        PagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), flist);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);
        setTabsValue() ;
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#45c01a"));
        // 设置文本颜色
        tabs.setTextColor(Color.parseColor("#45c01a"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private final String[] TITLES = { "Page1", "Page2", "Page 3"};

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }   

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }   
         
        @Override
        public int getCount() {
            return this.fragments.size();
        }   
    }   
}
