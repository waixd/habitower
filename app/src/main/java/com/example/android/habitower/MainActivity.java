package com.example.android.habitower;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.*;
import com.example.android.habitower.data.BodyActionDBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    public BodyActionDBHelper mDbHelper;
    private BottomNavigationView BottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        init();
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
                Toast.makeText(this, "Delete All data, refresh", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                        case R.id.menu_todo:
                            showNav(R.id.menu_todo);
                            return true;
                        case R.id.menu_about:
                            showNav(R.id.menu_about);
                            return true;
                    }
                    return false;
                };

    /** method that navigate　導航用
     *

     */
    private void showNav(int navid){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (navid){
            case R.id.menu_home:
                HomeFragment fragment1 = new HomeFragment();
                fragmentTransaction.replace(R.id.container, fragment1, "HOME");
                fragmentTransaction.commit();
                break;
            case R.id.menu_todo:
                TodoFragment fragment2 = new TodoFragment();
                fragmentTransaction.replace(R.id.container, fragment2, "TODO");
                fragmentTransaction.commit();
                break;
            case R.id.menu_about:
                AboutFragment fragment3 = new AboutFragment();
                fragmentTransaction.replace(R.id.container, fragment3, "About us");
                fragmentTransaction.commit();
                break;
        }
    }

    /***Initial the fragment 初始化用
     *
     */
    private void init(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment fragment1 = new HomeFragment();
        TodoFragment fragment2 = new TodoFragment();
        AboutFragment fragment3 = new AboutFragment();
        fragmentTransaction.add(R.id.container, fragment1).add(R.id.container, fragment2).add(R.id.container,fragment3);
        fragmentTransaction.hide(fragment1).hide(fragment2).hide(fragment3);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        showNav(R.id.menu_home);
    }






}

