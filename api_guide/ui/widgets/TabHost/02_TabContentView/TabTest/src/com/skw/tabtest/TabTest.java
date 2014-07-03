package com.skw.tabtest;

import android.app.TabActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class TabTest extends TabActivity 
    implements TabHost.OnTabChangeListener {

    private static final String TAG_HOME    = "home";
    private static final String TAG_ABOUT   = "about";
    private static final String TAG_CONTACT = "contact";

    private TabHost mTabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabHost = getTabHost();
        Resources res = getResources();
        LayoutInflater.from(this).inflate(R.layout.main, mTabHost.getTabContentView(), true);

        // Create an Intent to launch an Activity for the tab (to be reused)
        TabSpec spec = mTabHost.newTabSpec(TAG_HOME)
                .setIndicator("HOME", res.getDrawable(R.drawable.ic_tab_home))
                .setContent(R.id.tab_layout_home);
        mTabHost.addTab(spec);

        // Do the same for the other tabs

        spec = mTabHost.newTabSpec(TAG_ABOUT)
                .setIndicator("ABOUT", res.getDrawable(R.drawable.ic_tab_about))
                .setContent(R.id.tab_layout_about);
        mTabHost.addTab(spec);
        
         
        spec = mTabHost.newTabSpec(TAG_CONTACT)
                .setIndicator("CONTACT", res.getDrawable(R.drawable.ic_tab_contact))
                .setContent(R.id.tab_layout_contact);
        mTabHost.addTab(spec);
        
        //set tab which one you want open first time 0 or 1 or 2
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onTabChanged(String tabId) {
        if (tabId.equals(TAG_HOME)) {
            Toast.makeText(this, "HOME selected!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "select tab:"+tabId, Toast.LENGTH_SHORT).show();
        }
    }
}
