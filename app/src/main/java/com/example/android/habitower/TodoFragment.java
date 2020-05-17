package com.example.android.habitower;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.android.habitower.data.BodyActionContract;
import com.example.android.habitower.data.BodyActionDBHelper;




public class TodoFragment extends Fragment {

    public BodyActionDBHelper mDbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mDbHelper = new BodyActionDBHelper(getActivity());

        View view = inflater.inflate(R.layout.fragment_todo, container, false);


        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                BodyActionContract.BodyActionEntry._ID,
                BodyActionContract.BodyActionEntry.COLUMN_BODY_NAME,
                BodyActionContract.BodyActionEntry.COLUMN_BODY_TIME,
                BodyActionContract.BodyActionEntry.COLUMN_BODY_RESET,
                BodyActionContract.BodyActionEntry.COLUMN_BODY_CALORIES};

        // Perform a query on the pets table
        Cursor cursor = db.query(
                BodyActionContract.BodyActionEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView displayView = (TextView) view.findViewById(R.id.text_view_body_action);


        try {
            // Create a header in the Text View that looks like this:
            //
            // The pets table contains <number of rows in Cursor> body.
            // _id - name - time - distance - calories
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.

            String id = BodyActionContract.BodyActionEntry._ID;
            displayView.append(id + " - " +
                    BodyActionContract.BodyActionEntry.COLUMN_BODY_NAME + " - " +
                    BodyActionContract.BodyActionEntry.COLUMN_BODY_TIME + " - " +
                    BodyActionContract.BodyActionEntry.COLUMN_BODY_RESET + " - " +
                    BodyActionContract.BodyActionEntry.COLUMN_BODY_CALORIES + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BodyActionContract.BodyActionEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BodyActionContract.BodyActionEntry.COLUMN_BODY_NAME);
            int timeColumnIndex = cursor.getColumnIndex(BodyActionContract.BodyActionEntry.COLUMN_BODY_TIME);
            int distanceColumnIndex = cursor.getColumnIndex(BodyActionContract.BodyActionEntry.COLUMN_BODY_RESET);
            int caloriesColumnIndex = cursor.getColumnIndex(BodyActionContract.BodyActionEntry.COLUMN_BODY_CALORIES);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentTime = cursor.getString(timeColumnIndex);
                String currentDistance = cursor.getString(distanceColumnIndex);
                int currentCalories = cursor.getInt(caloriesColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentTime + " - " +
                        currentDistance + " - " +
                        currentCalories));

            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

        insertDemoAction();
        return view;
    }

/** demo action for testing  **/
    private void insertDemoAction() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(BodyActionContract.BodyActionEntry.COLUMN_BODY_NAME, "Demo Data");
        values.put(BodyActionContract.BodyActionEntry.COLUMN_BODY_TIME, 30);
        values.put(BodyActionContract.BodyActionEntry.COLUMN_BODY_RESET, "Daily");
        values.put(BodyActionContract.BodyActionEntry.COLUMN_BODY_CALORIES, 170);

        db.insert(BodyActionContract.BodyActionEntry.TABLE_NAME, null, values);
    }
}






