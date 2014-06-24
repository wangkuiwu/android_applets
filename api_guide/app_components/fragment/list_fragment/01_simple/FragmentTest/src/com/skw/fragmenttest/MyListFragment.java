package com.skw.fragmenttest;

import android.app.ListFragment;
import android.widget.ListView; 
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.widget.Toast;
import android.widget.SimpleAdapter;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class MyListFragment extends ListFragment {
    private static final String TAG = "##MyListFragment##";
    
    private ListView selfList;
    
    String[] cities = {
         "Shenzhen",
         "Beijing",
         "Shanghai",
         "Guangzhou",
         "Wuhan",
         "Tianjing",
         "Changsha",
         "Xi'an",
         "Chongqing",
         "Guilin",
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.list_fragment, container, false);
    }
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        // 设置ListFragment默认的ListView，即@id/android:list
        this.setListAdapter(new ArrayAdapter<String>(getActivity(), 
                android.R.layout.simple_list_item_1, cities));
        
    }
   
    public void onListItemClick(ListView parent, View v, 
            int position, long id) {
        Log.d(TAG, "onListItemClick");
        Toast.makeText(getActivity(), "You have selected " + cities[position],
                Toast.LENGTH_SHORT).show();
    }    
}
