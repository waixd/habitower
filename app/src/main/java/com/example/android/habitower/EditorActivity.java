package com.example.android.habitower;



import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.android.habitower.data.BodyActionContract.BodyActionEntry;
import com.example.android.habitower.data.BodyActionDBHelper;



/**
 * Allows user to create a new body action or edit an existing one.
 */
public class EditorActivity extends Activity {

    /**
     * EditText field to enter the body action's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the body action's time
     */
    private EditText mTimeEditText;

    /**
     * EditText field to enter the body action's distance
     */
    private Spinner mResetStreak;

    /**
     * EditText field to enter the body action's burned energy     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        android.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        //No Problem

        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.edit_bodyaction_name);
        mTimeEditText = findViewById(R.id.edit_bodyaction_time);
        mResetStreak = findViewById(R.id.edit_reset_streak);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save body action to database
                if (mNameEditText.getText().toString().trim().matches("")) {
                    Toast.makeText(this, "You did not enter an action", Toast.LENGTH_SHORT).show();

                }
                else if (mTimeEditText.getText().toString().trim().matches("")){
                    Toast.makeText(this, "You did not enter a time", Toast.LENGTH_SHORT).show();

                }    else {
                    insertBodyAction();
                    NavUtils.navigateUpFromSameTask(this);
                }
                    }
                // Exit activity
        return super.onOptionsItemSelected(item);
    }

    /**
     * Get user input from editor and save new body action into database.
     */
    private void insertBodyAction() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String timeString = mTimeEditText.getText().toString().trim();
        String resetString = mResetStreak.getSelectedItem().toString().trim();
        int time = Integer.parseInt(timeString);
        String reset_streak = resetString;

        // Create database helper
        BodyActionDBHelper mDbHelper = new BodyActionDBHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and body action attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(BodyActionEntry.COLUMN_BODY_NAME, nameString);
        values.put(BodyActionEntry.COLUMN_BODY_TIME, time);
        values.put(BodyActionEntry.COLUMN_BODY_RESET, reset_streak);

        // Insert a new row for body action in the database, returning the ID of that new row.
        long newRowId = db.insert(BodyActionEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving body action", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Body action saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }
}
