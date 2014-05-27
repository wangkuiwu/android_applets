package com.skw.actionbar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;  
import android.view.MenuInflater; 

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                //openSearch();
                Toast.makeText(getApplicationContext(), "search menu", 0).show();
                return true;
            case R.id.action_settings:
                //openSettings();
                Toast.makeText(getApplicationContext(), "setting menu", 0).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
