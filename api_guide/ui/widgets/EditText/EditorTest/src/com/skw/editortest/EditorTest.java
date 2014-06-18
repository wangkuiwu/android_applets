package com.skw.editortest;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class EditorTest extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initSendAction();
    }

    private void initSendAction() {
        EditText editText = (EditText) findViewById(R.id.et_msg);
        editText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(EditorTest.this, "click send!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
