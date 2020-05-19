package com.example.android.habitower;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.android.habitower.data.BodyActionContract;
import com.example.android.habitower.data.BodyActionDBHelper;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    public static final String SHARED_PREFS = "sharedPrefs";
    ListView listView;
    List<String> names = new ArrayList<>();
    public BodyActionDBHelper mDbHelper;
    private TextView mexp_tf, mtask_tf, mfloor_tf;
    public static final String TEXT = "text";
    public static final String TEXT2 = "text";
    private String text = "0";
    private String text2 = "1";
    int num = 0;
    int floor = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mDbHelper = new BodyActionDBHelper(getActivity());

        View view = inflater.inflate(R.layout.main_page, container, false);
        listView =  view.findViewById(R.id.listView1);
        List<String> nameList = readContacts(getActivity());
        CustomAdapter adapter = new CustomAdapter(getActivity(), nameList);
        listView.setAdapter(adapter);
        insertBodyAction();
        mexp_tf =  view.findViewById(R.id.exp_id);
        mfloor_tf =  view.findViewById(R.id.floor_id);
        final Button button = view.findViewById(R.id.submit_all);
        button.setOnClickListener(v -> {
            if (mexp_tf.getText() == "9"){
                num = 0;
                mexp_tf.setText(num+"");
                updateFloor();
                saveData();
                updateViews();

            } else {
                num += 1;
                mexp_tf.setText(num+"");

            }
            saveData();
            // Code here executes on main thread after user presses button
        });

        updateViews();
        loadData();
        return view;
    }

    public void updateFloor(){
        floor += 1;
        mfloor_tf.setText(floor+"");
    }

    public void saveData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, mexp_tf.getText().toString());
        editor.putString(TEXT2, mfloor_tf.getText().toString());
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
        text2 = sharedPreferences.getString(TEXT2, "");
    }

    public void updateViews() {
        mexp_tf.setText(text);
        mfloor_tf.setText(text2);
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
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                String currentName = cursor.getString(nameColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                names.add(currentName);

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






