package com.example.android.habitower;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import static com.example.android.habitower.HomeFragment.bg_index;

public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_aboutus,container,false);

        if (bg_index == 1) {
            view.setBackgroundResource(R.drawable.bg_2);
        } else if (bg_index == 0){
            view.setBackgroundResource(0);
        }
        return view;
    }


}


