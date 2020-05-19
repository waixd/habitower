package com.example.android.habitower;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.habitower.data.BodyActionDBHelper;

import java.util.ArrayList;
import java.util.List;


public class CalendarFragment extends Fragment {
    ListView listView;
    List<String> names = new ArrayList<String>();
    public BodyActionDBHelper mDbHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        return view;


    }
}






