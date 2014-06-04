package com.skw.sendtext;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.ComponentName;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;  
import android.view.MenuInflater; 
import android.view.View;
import android.view.View.OnClickListener;

public class SendText extends Activity 
    implements View.OnClickListener {

    private ShareActionProvider mShareActionProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button mSend = (Button) findViewById(R.id.bt_send);
        mSend.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);


        MenuItem item = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        setShareIntent();

        return super.onCreateOptionsMenu(menu);
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "This is my text to share.");
            intent.setType("text/plain");
            mShareActionProvider.setShareIntent(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(getApplicationContext(), "search menu", 0).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "setting menu", 0).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override 
    public void onClick(View v) {
        jump();
    }

    private void jump() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
