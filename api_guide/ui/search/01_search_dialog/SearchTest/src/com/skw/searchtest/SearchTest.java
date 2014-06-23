package com.skw.searchtest;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SearchTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onClickSearch(View view) {
        Toast.makeText(this, "Click Search", Toast.LENGTH_SHORT).show();
        onSearchRequested();
    }
}
