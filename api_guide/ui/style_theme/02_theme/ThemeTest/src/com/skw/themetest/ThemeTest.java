package com.skw.themetest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class ThemeTest extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, CustomTheme.class);
        startActivity(intent);
    }
}
