package com.example.android.habitower;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import static com.example.android.habitower.HomeFragment.SHARED_PREFS;
import static com.example.android.habitower.HomeFragment.ava_index;
import static com.example.android.habitower.HomeFragment.bg_index;
import static com.example.android.habitower.HomeFragment.boost_key;
import static com.example.android.habitower.HomeFragment.exp_boost_index;
import static com.example.android.habitower.HomeFragment.floor_key;
import static com.example.android.habitower.HomeFragment.mfloor_tf;
import static com.example.android.habitower.MainActivity.floor_stored;
import static com.example.android.habitower.MainActivity.reset;


public class RewardFragment extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private int count = 0;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward, container, false);
        Button exp_boost = (Button) view.findViewById(R.id.exp_button);
        Button bg_button = (Button) view.findViewById(R.id.bg_button);
        Button ava_button = (Button) view.findViewById(R.id.avatar_button);
        Button unknown_button = (Button) view.findViewById(R.id.job_button);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_MULTI_PROCESS);
        editor = sharedPreferences.edit();
        bg_index = sharedPreferences.getInt("bg_key", 0);


        /** check the current background**/
        if (bg_index == 1) {
            view.setBackgroundResource(R.drawable.bg_2);
        } else {
            view.setBackgroundResource(0);
        }

        /*set listener to exp button*/
        exp_boost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Integer.parseInt(mfloor_tf.getText().toString()) >= 10) {
                    do_EXP_Boost();
                } else {
                    makeTextAndShow(getActivity(), "You are not yet 10 floor !!" ,Toast.LENGTH_SHORT);
                }
            }
        });


        /*set listener to bg button*/
        bg_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Integer.parseInt(mfloor_tf.getText().toString()) >= 20) {
                do_BG_change();
            } else {
                    makeTextAndShow(getActivity(), "You are not yet 20 floor !!" ,Toast.LENGTH_SHORT);
            }
        }
    });
        /*set listener to avatar button*/
        ava_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Integer.parseInt(mfloor_tf.getText().toString()) >= 30) {
                    do_avater_change();
                } else {
                    makeTextAndShow(getActivity(), "You are not yet 30 floor !!" ,Toast.LENGTH_SHORT);
                }
            }
        });

        unknown_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (reset) {
                    if (count < 5 ) {
                        count++;
                        makeTextAndShow(getActivity(), "Grant tester right for click " + (5 - count) + " times", Toast.LENGTH_SHORT);
                        if (count == 5) {
                            makeTextAndShow(getActivity(), "get to 100 floor! You can activate all reward now!!", Toast.LENGTH_SHORT);
                            floor_stored = mfloor_tf.getText().toString();
                            mfloor_tf.setText(100 + "");
                            editor.putString(floor_key, mfloor_tf.getText().toString());
                            editor.apply();
                            count = 0;
                            reset = false;
                        }
                    }
                } else if (!reset ) {
                    if (count < 5) {
                        count++;
                        makeTextAndShow(getActivity(), "click " + (5 - count) + " times" + "to return to original floor", Toast.LENGTH_SHORT);
                        if (count == 5) {
                            makeTextAndShow(getActivity(), "back to original floor!", Toast.LENGTH_SHORT);
                            mfloor_tf.setText(floor_stored);
                            editor.putString(floor_key, floor_stored);
                            editor.apply();
                            count = 0;
                            reset = true;

                        }
                    }
                }
            }
        });






       /* return the reward page*/
        return view;
    }




    public void do_avater_change() {
        if (ava_index == 0) {
            HomeFragment.updateAvatar();
            editor.putInt("ava_key", 1);
            editor.apply();
            makeTextAndShow(getActivity(), "Switch avatar!" ,Toast.LENGTH_SHORT);
            Intent aIntent = getActivity().getIntent();
            getActivity().finish();
            startActivity(aIntent);
        } else if (ava_index == 1) {
            HomeFragment.updateAvatar();
            editor.putInt("ava_key", 0);
            editor.apply();
            makeTextAndShow(getActivity(), "Switch back to original avatar!" ,Toast.LENGTH_SHORT);
            Intent aIntent = getActivity().getIntent();
            getActivity().finish();
            startActivity(aIntent);
        }
    }

    public void do_BG_change(){
        if (bg_index == 0) {
            getView().setBackgroundResource(R.drawable.bg_2);
            HomeFragment.updateBG();
            editor.putInt("bg_key", 1);
            editor.apply();
            makeTextAndShow(getActivity(), "Switch background!" ,Toast.LENGTH_SHORT);
        } else if (bg_index == 1) {
            getView().setBackgroundResource(0);
            HomeFragment.updateBG();
            editor.putInt("bg_key", 0);
            editor.apply();
            makeTextAndShow(getActivity(), "Back to original background!" ,Toast.LENGTH_SHORT);
        }
      }

    public void do_EXP_Boost() {
        if (exp_boost_index == 0) {
            HomeFragment.exp_boost();
            editor.putInt(boost_key, 1);
            editor.apply();
            makeTextAndShow(getActivity(), "Turn ON!" ,Toast.LENGTH_SHORT);
        } else if (exp_boost_index == 1){
            HomeFragment.exp_boost();
            editor.putInt(boost_key, 0);
            editor.apply();
            makeTextAndShow(getActivity(), "Turn OFF!" ,Toast.LENGTH_SHORT);
        }
    }


/** generate toast*/
    static Toast toast;
    private static void makeTextAndShow(final Context context, final String t_text, final int duration) {
        if (toast == null) {
            toast = android.widget.Toast.makeText(context, t_text, duration);
        } else {
            toast.setText(t_text);
            toast.setDuration(duration);
        }
        toast.show();
    }
}



