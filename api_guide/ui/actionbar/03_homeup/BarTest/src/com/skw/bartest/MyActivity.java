package com.skw.bartest;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;  
import android.view.MenuInflater; 

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myactivity);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(getApplicationContext(), "sub search", 0).show();
                return true;
            case R.id.setting:
                Toast.makeText(getApplicationContext(), "sub setting", 0).show();
                return true;
            case R.id.share:
                Toast.makeText(getApplicationContext(), "sub share", 0).show();
                return true;
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "home!", 0).show();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
