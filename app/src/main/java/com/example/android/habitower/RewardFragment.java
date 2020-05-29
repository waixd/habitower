package com.example.android.habitower;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.android.habitower.HomeFragment.mfloor_tf;


public class RewardFragment extends Fragment{



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_reward,container,false);
        Button exp_boost = (Button) view.findViewById(R.id.exp_button);

        exp_boost.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {
                if (Integer.parseInt(mfloor_tf.getText().toString()) >= 10) {
                    HomeFragment.exp_boost();
                    Toast toast = Toast.makeText(getActivity(),
                            "ACTIVATE!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getActivity(),
                            "You are not yet 50 floor !!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });
        return view;
    }





}



