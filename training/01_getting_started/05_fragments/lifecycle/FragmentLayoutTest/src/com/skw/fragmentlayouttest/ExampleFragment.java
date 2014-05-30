package com.skw.fragmentlayouttest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.app.Fragment;
import android.util.Log;

public class ExampleFragment extends Fragment {

    private static final String TAG = "##ExampleFragment##";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.example_fragment, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }   

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }   

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }   
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }   

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }   

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    } 

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    } 

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    } 
}
