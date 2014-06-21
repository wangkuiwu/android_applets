package com.skw.bartest;

import android.app.Activity;
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
    }

    public void onJumpClicked(View view) {
        Intent intent = new Intent("com.skw.bartest.JumpAction");
        startActivity(intent);
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
                Toast.makeText(getApplicationContext(), "search", 0).show();
                return true;
            case R.id.setting:
                Toast.makeText(getApplicationContext(), "setting", 0).show();
                return true;
            case R.id.share:
                Toast.makeText(getApplicationContext(), "share", 0).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
