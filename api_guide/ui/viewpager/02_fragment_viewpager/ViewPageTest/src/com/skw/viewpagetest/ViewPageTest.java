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


import java.util.List;
import java.util.ArrayList;

public class ViewPageTest extends FragmentActivity {
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

        List<Fragment> flist = new ArrayList<Fragment>();
        // 初始化Fragment
        flist.add(MyFragment.newInstance("Page 1"));
        flist.add(MyFragment.newInstance("Page 2"));
        flist.add(MyFragment.newInstance("Page 3"));

        // 初始化Adapter
        PagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), flist);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
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
