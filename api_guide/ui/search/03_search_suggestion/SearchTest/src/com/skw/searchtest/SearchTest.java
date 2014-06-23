package com.skw.searchtest;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.SearchRecentSuggestions;

public class SearchTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onClickSearch(View view) {
        switch(view.getId()) {
            case R.id.search:
                Toast.makeText(this, "Click Search", Toast.LENGTH_SHORT).show();
                onSearchRequested();
                break;
            case R.id.clear:
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                        MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
                suggestions.clearHistory();
                break;
            default:
                break;
        }
    }
}
