package com.example.android.habitower;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.android.habitower.HomeFragment.SHARED_PREFS;
import static com.example.android.habitower.HomeFragment.boost_key;
import static com.example.android.habitower.HomeFragment.exp_boost_index;
import static com.example.android.habitower.HomeFragment.mfloor_tf;




public class RewardFragment extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward, container, false);
        Button exp_boost = (Button) view.findViewById(R.id.exp_button);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_MULTI_PROCESS);
        editor = sharedPreferences.edit();

        exp_boost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Integer.parseInt(mfloor_tf.getText().toString()) >= 1) {
                    do_EXP_Boost();
                } else {
                    Toast toast = Toast.makeText(getActivity(),
                            "You are not yet 50 floor !!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });
        return view;
    }

    public void do_EXP_Boost() {
        if (exp_boost_index == 0) {
            HomeFragment.exp_boost();
            editor.putInt(boost_key, 1);
            editor.apply();
            Toast toast = Toast.makeText(getActivity(),
                    "ACTIVATE!", Toast.LENGTH_SHORT);
            toast.show();
        } else if (exp_boost_index == 1){
            HomeFragment.exp_boost();
            editor.putInt(boost_key, 0);
            editor.apply();
            Toast toast = Toast.makeText(getActivity(),
                    "NOT ACTIVATE!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}



