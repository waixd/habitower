package com.example.android.habitower;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.android.habitower.data.BodyActionContract;
import com.example.android.habitower.data.BodyActionDBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {
    public static final String SHARED_PREFS = "sharedPrefs";
    List<String> names = new ArrayList<>();
    public BodyActionDBHelper mDbHelper;
    public static TextView mexp_tf, mtask_tf, mfloor_tf;
    TextView mboost_tf;
    ImageView job_image;
    int exp_value;
    int floor_value;
    int task_value;
    CheckBox cb;
    public static String exp_key = "EXP";
    public static String floor_key = "Floor";
    public static String task_key = "Task";
    public static String boost_key = "boost";
    public static String exp_sp, floor_sp, task_sp=  "";
    public static int exp_boost_index ;
    public static int bg_index;
    public static int ava_index;
    ListView listView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mDbHelper = new BodyActionDBHelper(getActivity());
        View view = inflater.inflate(R.layout.main_page, container, false);
        listView =  view.findViewById(R.id.listView1);
        List<String> nameList = readContacts(getActivity());
        CustomAdapter adapter = new CustomAdapter(getActivity(), nameList);
        listView.setAdapter(adapter);
        mexp_tf =  view.findViewById(R.id.exp_id);
        mfloor_tf =  view.findViewById(R.id.floor_id);
        mtask_tf =  view.findViewById(R.id.task_id);
        mboost_tf = view.findViewById(R.id.exp_double);
        job_image = view.findViewById(R.id.job_icon);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_MULTI_PROCESS);
        editor = sharedPreferences.edit();

        loadData();
        if (bg_index == 1) {
            view.setBackgroundResource(R.drawable.bg_2);
        } else if (bg_index == 0){
            view.setBackgroundResource(0);
        }
        defaultAvatar();
        defaultEXPBoost();
        updateViews();



        final Button button2 = view.findViewById(R.id.select_all);
        button2.setOnClickListener(v -> {
            selectALL();
    });

        /** submit button **/
        final Button button = view.findViewById(R.id.submit_all);
        button.setOnClickListener(v -> {
            int check = CustomAdapter.returnCheck();
            if (check == 0){
                makeTextAndShow(getActivity(), "Please tick a box!" ,Toast.LENGTH_SHORT);
                CustomAdapter.resetCheck();
            } else {
            gainEXP();
            updateTask();
            CustomAdapter.resetCheck();
            //*uncheck all checkbox after click the button**//
                resetCheck();
            }
            saveData();
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }


    public void resetCheck(){
        for (int i = 0; i < listView.getChildCount(); i++) {
            //Replace R.id.checkbox with the id of CheckBox in your layout
            cb = (CheckBox) listView.getChildAt(i).findViewById(R.id.checkBox1);
            cb.setChecked(false);
        }
    }

    public void selectALL(){
        for (int i = 0; i < listView.getChildCount(); i++) {
            //Replace R.id.checkbox with the id of CheckBox in your layout
            cb = (CheckBox) listView.getChildAt(i).findViewById(R.id.checkBox1);
            cb.setChecked(true);
        }
    }
    //** count exp for user **//
    public void gainEXP(){
        exp_value = Integer.parseInt((String) mexp_tf.getText());
        if (exp_value >= 9){
            exp_value = 0;
            mexp_tf.setText(exp_value+"");
            updateFloor();
        }
        else {
            updateEXP();
            }
    }
    //*update EXP value for user
       public void updateEXP() {
        /**check if exp boost is activated**/
        exp_value = Integer.parseInt((String) mexp_tf.getText());
        if (mboost_tf.getText().toString().equals("Boost: ON")) {
            exp_value += 2;
                if (exp_value < 10) {
                    mexp_tf.setText(exp_value + "");
                } else {
                    updateFloor();
                }
            }
            /** for exp boost not activated**/
            else {
                exp_value += 1;
                mexp_tf.setText(exp_value + "");
            }
        }

    public static void exp_boost() {
        if (exp_boost_index == 0) {
            exp_boost_index = 1;
        } else if (exp_boost_index == 1) {
            exp_boost_index = 0;
        }
    }

    public static void updateAvatar() {
        if (ava_index == 0) {
            ava_index = 1;

        } else {
            ava_index = 0;
        }
    }

    public void defaultAvatar() {
        if (ava_index == 1) {
            job_image.setImageResource(R.drawable.lipig);
        } else if (ava_index == 0) {
            job_image.setImageResource(R.drawable.pig);
        }
    }


    public static void updateBG() {
        if (bg_index == 0) {
            bg_index = 1;
        } else {
            bg_index = 0;
        }
    }
    //** update task no. for user **//
    public void updateTask(){
       int count = CustomAdapter.returnCheck();
       task_value = Integer.parseInt((String) mtask_tf.getText());
       task_value += count;
       mtask_tf.setText(task_value+"");

    }

    public void defaultEXPBoost() {
        if (exp_boost_index == 1) {
            mboost_tf.setText("Boost: ON");
        } else if (exp_boost_index == 0) {
            mboost_tf.setText("Boost: OFF");
        }
    }

    //** count floor for user **//
    public void updateFloor(){
        floor_value = Integer.parseInt((String) mfloor_tf.getText());
        floor_value += 1;
        mfloor_tf.setText(floor_value+"");
        mexp_tf.setText("0");
    }

    /** save user data into sharedPreferences**/
    public void saveData() {
        editor.putString(exp_key, mexp_tf.getText().toString());
        editor.putString(floor_key, mfloor_tf.getText().toString());
        editor.putString(task_key, mtask_tf.getText().toString());
        editor.apply();
    }
    /** load user data from sharedPreferences**/
    public void loadData() {
        exp_sp = sharedPreferences.getString(exp_key, "0");
        floor_sp = sharedPreferences.getString(floor_key, "1");
        task_sp = sharedPreferences.getString(task_key, "1");
        exp_boost_index = sharedPreferences.getInt(boost_key, 0);
        bg_index = sharedPreferences.getInt("bg_key", 0);
        ava_index = sharedPreferences.getInt("ava_key", 0);
        exp_value =  Integer.parseInt(exp_sp);
        floor_value = Integer.parseInt(floor_sp);
        task_value = Integer.parseInt(task_sp);
    }

    /** update view from sharedPreferences**/
    public void updateViews() {
        mexp_tf.setText(exp_sp);
        mfloor_tf.setText(floor_sp);
        mtask_tf.setText(task_sp);

    }


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

    private List<String> readContacts(FragmentActivity activity) {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        String[] projection = {
                BodyActionContract.BodyActionEntry._ID,
                BodyActionContract.BodyActionEntry.COLUMN_BODY_NAME,
                BodyActionContract.BodyActionEntry.COLUMN_BODY_TIME,
                BodyActionContract.BodyActionEntry.COLUMN_BODY_RESET,
                BodyActionContract.BodyActionEntry.COLUMN_BODY_CALORIES};

        Cursor cursor = db.query(
                BodyActionContract.BodyActionEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        try {
            // Create a header in the Text View that looks like this:
            //
            // The pets table contains <number of rows in Cursor> body.
            // _id - name - time - distance - calories
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.

            String id = BodyActionContract.BodyActionEntry._ID;

            // Figure out the index of each column
            int nameColumnIndex = cursor.getColumnIndex(BodyActionContract.BodyActionEntry.COLUMN_BODY_NAME);
            int timeColumnIndex = cursor.getColumnIndex(BodyActionContract.BodyActionEntry.COLUMN_BODY_TIME);
            int distanceColumnIndex = cursor.getColumnIndex(BodyActionContract.BodyActionEntry.COLUMN_BODY_RESET);
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                String currentName = cursor.getString(nameColumnIndex);
                String currentTime = cursor.getString(timeColumnIndex);
                String currentDistance = cursor.getString(distanceColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                names.add(currentName + "\n" + currentTime + " mins / " + currentDistance);

            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

        return names;
    }


    private void insertBodyAction() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(BodyActionContract.BodyActionEntry.COLUMN_BODY_NAME, "Static Bicycle");
        values.put(BodyActionContract.BodyActionEntry.COLUMN_BODY_TIME, 30);
        values.put(BodyActionContract.BodyActionEntry.COLUMN_BODY_RESET, "daily");
        values.put(BodyActionContract.BodyActionEntry.COLUMN_BODY_CALORIES, 170);

        db.insert(BodyActionContract.BodyActionEntry.TABLE_NAME, null, values);

    }




}






