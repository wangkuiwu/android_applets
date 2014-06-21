package com.skw.bartest;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Fragment;

public class FragC extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_c, container, false);
    }
}
