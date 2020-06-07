package com.example.android.habitower;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android.habitower.data.BodyActionContract;
import com.example.android.habitower.data.BodyActionDBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {


    public BodyActionDBHelper mDbHelper;
    private BottomNavigationView BottomNav;
    TextView textView;
    public static boolean reset = true;
    public static String floor_stored = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        init();
        textView = findViewById(R.id.exp_id);
        BottomNav = findViewById(R.id.navigation);
        BottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mDbHelper = new BodyActionDBHelper(this);


    }

    /** set menu for optionMenu
     *
     */

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_insert_data:
                // Save body action to database
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
                // Exit activity
                return true;
            case R.id.action_delete_all_entries:
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM body");//delete all rows in a tabl
                db.execSQL("DELETE FROM sqlite_sequence where name ='body'");// e
                Toast.makeText(this, "Delete All data, refreshing", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                return true;
            case R.id.action_insert_sample_data:
                insertBodyAction();
                Intent aIntent = getIntent();
                finish();
                startActivity(aIntent);
                return true;
            case R.id.share:
                return false;
        }
        return super.onOptionsItemSelected(item);
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

    /** set listener for the navigation bar
     *
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.menu_home:
                showNav(R.id.menu_home);
                return true;
            case R.id.menu_calendar:
                showNav(R.id.menu_calendar);
                return true;
            case R.id.menu_reward:
                showNav(R.id.menu_reward);
                return true;
            case R.id.menu_about:
                showNav(R.id.menu_about);
                return true;
        }
        return false;
    };

    /** method that navigate　
     *

     */
    private void showNav(int navid){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (navid){
            case R.id.menu_home:
                HomeFragment fragment1 = new HomeFragment();
                fragmentTransaction.replace(R.id.container, fragment1, "Home");
                fragmentTransaction.commit();
                break;
            case R.id.menu_calendar:
                CalendarFragment fragment2 = new CalendarFragment();
                fragmentTransaction.replace(R.id.container, fragment2, "Calendar");
                fragmentTransaction.commit();
                break;
            case R.id.menu_reward:
                RewardFragment fragment3 = new RewardFragment();
                fragmentTransaction.replace(R.id.container, fragment3, "Reward");
                fragmentTransaction.commit();
                break;
            case R.id.menu_about:
                AboutFragment fragment4 = new AboutFragment();
                fragmentTransaction.replace(R.id.container, fragment4, "About us");
                fragmentTransaction.commit();
                break;
        }
    }

    /***Initial the fragment
     *
     */
    private void init(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment fragment = new HomeFragment();
        CalendarFragment fragment2 = new CalendarFragment();
        RewardFragment fragment3 = new RewardFragment();
        AboutFragment fragment4 = new AboutFragment();
        fragmentTransaction.add(R.id.container, fragment).add(R.id.container, fragment2).add(R.id.container,fragment3).add(R.id.container,fragment4);
        fragmentTransaction.hide(fragment).hide(fragment2).hide(fragment3).hide(fragment4);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        showNav(R.id.menu_home);
    }





}



