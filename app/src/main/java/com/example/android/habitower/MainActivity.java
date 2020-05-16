package com.example.android.habitower;
/**
 * Copyright (C) 2017 The Android Open Source Project
 * This app "You Stay Young" is for people who want to check
 * if their habits are healthy for body and soul
 * Is created with android studio 2.3.1
 * as exercise for Android Basics by Google Nanodegree Program
 * "Habit Tracker " by Dimitra Christina Nikolaidou
 */



import androidx.annotation.NonNull;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.*;

import com.google.android.material.bottomnavigation.BottomNavigationView;
/**
 * Displays list of body actions that were entered and stored in the app.
 */
import android.view.MenuItem;
import android.widget.TextView;

public class CatalogActivity extends AppCompatActivity {


    private BottomNavigationView BottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        init();
        BottomNav = findViewById(R.id.navigation);
        BottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
                        case R.id.menu_insert:
                            showNav(R.id.menu_insert);
                            return true;
                        case R.id.menu_about:
                            showNav(R.id.menu_about);
                            return true;
                    }
                    return false;
                };

    /** method that navigate　導航用
     *
      * @param navid
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
            case R.id.menu_insert:
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
        AboutFragment fragment3 = new AboutFragment();
        fragmentTransaction.add(R.id.container, fragment1).add(R.id.container,fragment3);
        fragmentTransaction.hide(fragment1).hide(fragment3);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        showNav(R.id.menu_home);
    }





}

