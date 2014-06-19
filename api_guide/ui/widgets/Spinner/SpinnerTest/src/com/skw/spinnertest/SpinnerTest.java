package com.skw.spinnertest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class SpinnerTest extends Activity {

    private Spinner mSpinPlanet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 设置Spinner
        mSpinPlanet = (Spinner) findViewById(R.id.spin_planet);        
        // 设置ArrayAdapter对应的数据和布局
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_planets, android.R.layout.simple_spinner_item);
        // 设置ArrayAdapter的下拉布局
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将该ArrayAdapter赋值给Spinner
        mSpinPlanet.setAdapter(adapter);
        // 设置选择监听函数
        mSpinPlanet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, 
                    int pos, long id) {
                // 获取array_planets获取对应的String数组
                String[] planets = getResources().getStringArray(R.array.array_planets);
                // 获取pos对应的字符串
                String planet = planets[pos];
                // 提示
                showToast("select "+planet);
            }

            // Spinner未选择的回调函数
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showToast("Nothing Selected!");
            }
        });
    }

    private void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
