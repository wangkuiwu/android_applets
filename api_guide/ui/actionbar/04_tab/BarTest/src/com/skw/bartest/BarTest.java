package com.skw.bartest;

import android.app.Activity;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;  
import android.view.MenuInflater; 

public class BarTest extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 获取ActionBar
        ActionBar bar = getActionBar();
        // 设置ActionBar模式
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // 创建Fragment
        Fragment fragmentA = new FragA();
        Fragment fragmentB = new FragB();
        Fragment fragmentC = new FragC();

        // 新建3个Tab，并将这三个TAB添加到ActionBar中。
        // (01) setText()是设置标题
        // (02) setIcon()是设置图标
        // (02) setTabListener()是设置Tab监听器
        Tab tabA = bar.newTab().setText("A Tab").setIcon(R.drawable.ic_action_call).setTabListener(new MyTabListener(fragmentA));
        Tab tabB = bar.newTab().setText("B Tab").setIcon(R.drawable.ic_action_mail).setTabListener(new MyTabListener(fragmentB));
        Tab tabC = bar.newTab().setText("C Tab").setIcon(R.drawable.ic_action_video).setTabListener(new MyTabListener(fragmentC));

        bar.addTab(tabA);
        bar.addTab(tabB);
        bar.addTab(tabC);
    }

    class MyTabListener implements TabListener{
        private Fragment fragment; 

        public MyTabListener(Fragment fragment){ 
            this.fragment=fragment; 
        } 
        @Override 
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }
        @Override 
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            if (ft!=null) {
                ft.add(R.id.frag, fragment);
            }
        }
        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (ft!=null) {
                ft.remove(fragment);
            }
        }
    } 

    public void sendMessage(View v) {
        Toast.makeText(this, "FragA send", 0).show();
    }
}
