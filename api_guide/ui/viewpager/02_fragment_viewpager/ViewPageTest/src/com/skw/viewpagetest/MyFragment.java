package com.skw.viewpagetest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class MyFragment extends Fragment {

    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
        
    public static final MyFragment newInstance(String message) {
        MyFragment f = new MyFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(EXTRA_MESSAGE, message);
        f.setArguments(bundle);
        return f;
    }   

    @Override
    public View onCreateView(LayoutInflater inflater, 
            ViewGroup container, Bundle savedInstanceState) {
        String message = getArguments().getString(EXTRA_MESSAGE);
        View view = inflater.inflate(R.layout.myfragment, container, false);
        TextView messageTextView = (TextView)view.findViewById(R.id.tv);
        messageTextView.setText(message);
            
        return view;
    }   
}

