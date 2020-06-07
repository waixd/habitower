package com.example.android.habitower;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.android.habitower.data.BodyActionContract;
import com.example.android.habitower.data.BodyActionDBHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    public static final String SHARED_PREFS = "sharedPrefs";
    List<String> names = new ArrayList<>();
    public BodyActionDBHelper mDbHelper;
    public static TextView mexp_tf, mtask_tf, mfloor_tf;
    TextView mboost_tf;
    TextView tvShare;
    ImageView job_image;
    RelativeLayout linearLayout3;
    int exp_value;
    int floor_value;
    int task_value;
    CheckBox cb;
    public static String exp_key = "EXP";
    public static String floor_key = "Floor";
    public static String task_key = "Task";
    public static String boost_key = "boost";
    public static String exp_sp, floor_sp, task_sp = "";
    public static int exp_boost_index;
    public static int bg_index;
    public static int ava_index;
    ListView listView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        mDbHelper = new BodyActionDBHelper(getActivity());
        View view = inflater.inflate(R.layout.main_page, container, false);
        listView = view.findViewById(R.id.listView1);
        List<String> nameList = readContacts(getActivity());
        CustomAdapter adapter = new CustomAdapter(getActivity(), nameList);
        listView.setAdapter(adapter);
        listView.setEnabled(true);
        mexp_tf = view.findViewById(R.id.exp_id);
        mfloor_tf = view.findViewById(R.id.floor_id);
        mtask_tf = view.findViewById(R.id.task_id);
        mboost_tf = view.findViewById(R.id.exp_double);
        job_image = view.findViewById(R.id.job_icon);
        linearLayout3 = view.findViewById(R.id.linearLayout3);
        tvShare = view.findViewById(R.id.tvShare);
        shareTextChange();
        setHasOptionsMenu(true);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_MULTI_PROCESS);
        editor = sharedPreferences.edit();

        loadData();
        if (bg_index == 1) {
            view.setBackgroundResource(R.drawable.bg_2);
        } else if (bg_index == 0) {
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
            if (check == 0) {
                makeTextAndShow(getActivity(), "Please tick a box!", Toast.LENGTH_SHORT);
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

    private void shareTextChange() {
        tvShare.setText("I had completed " + mtask_tf.getText().toString() + " tasks  " + " \n " + " and arrived to the " + mfloor_tf.getText().toString() + "th Floors :)");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_insert_data:

                return false;
            case R.id.action_delete_all_entries:

                return false;
            case R.id.action_insert_sample_data:
                return false;
            case R.id.share:
                if (isStoragePermissionGranted()) {
                    //File write logic here
                    shareFacebook(getActivity(), "Hello testing");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    public void shareFacebook(Activity activity, String text) {
        Bitmap bitmap1 = loadBitmapFromView(linearLayout3, linearLayout3.getWidth(), linearLayout3.getHeight());
        saveBitmap(bitmap1);
    }

    File imagePath;

    public void saveBitmap(Bitmap bitmap) {
        File directory = new File("/sdcard/Testing/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        imagePath = new File(directory, "testing" + ".jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Log.e("ImageSave", "Saveimage");
            Uri uri = Uri.fromFile(imagePath);
            boolean facebookAppFound = false;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("*/*");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
            final File photoFile = new File(Environment.getExternalStorageDirectory(), "test.jpg");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            PackageManager pm = getActivity().getPackageManager();
            List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
            for (final ResolveInfo app : activityList) {
                if ((app.activityInfo.packageName).contains("com.facebook.katana")) {
                    final ActivityInfo activityInfo = app.activityInfo;
                    final ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                    shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    shareIntent.setComponent(name);
                    facebookAppFound = true;
                    break;
                }
            }
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (!facebookAppFound) {
                Toast.makeText(getActivity(), "Please install facebook...", Toast.LENGTH_SHORT).show();
                return;
            }
            getActivity().startActivity(shareIntent);
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }


    public void resetCheck() {
        for (int i = 0; i < listView.getChildCount(); i++) {
            //Replace R.id.checkbox with the id of CheckBox in your layout
            cb = (CheckBox) listView.getChildAt(i).findViewById(R.id.checkBox1);
            cb.setChecked(false);
        }
    }

    public void selectALL() {
        for (int i = 0; i < listView.getChildCount(); i++) {
            //Replace R.id.checkbox with the id of CheckBox in your layout
            cb = (CheckBox) listView.getChildAt(i).findViewById(R.id.checkBox1);
            cb.setChecked(true);
        }
    }

    //** count exp for user **//
    public void gainEXP() {
        exp_value = Integer.parseInt((String) mexp_tf.getText());
        if (exp_value >= 10) {
            exp_value = 0;
            mexp_tf.setText(exp_value + "");
            updateFloor();
        } else {
            updateEXP();
        }
        if (mboost_tf.getText().toString().equals("Boost: ON")) {
            makeTextAndShow(getActivity(), "Gain " + expCount() * 2 + " EXP!!", Toast.LENGTH_SHORT);
        } else {
            makeTextAndShow(getActivity(), "Gain " + expCount() + " EXP!!", Toast.LENGTH_SHORT);
            }
    }

    public int expCount() {
        int count = 0;
        for (int i = 0; i < listView.getChildCount(); i++) {
            //Replace R.id.checkbox with the id of CheckBox in your layout
            cb = (CheckBox) listView.getChildAt(i).findViewById(R.id.checkBox1);
            if (cb.isChecked()) {
                count += 1;
            }
        }
        return count;
    }

    //*update EXP value for user
    public void updateEXP() {
        /**check if exp boost is activated**/
        exp_value = Integer.parseInt((String) mexp_tf.getText());
        if (mboost_tf.getText().toString().equals("Boost: ON")) {
            exp_value += expCount();
            mexp_tf.setText(exp_value + "");
            if (exp_value >= 10){
                updateFloor();
            }
        }
        /** for exp boost not activated**/
        else {

            exp_value += expCount();
            mexp_tf.setText(exp_value + "");
                if (exp_value >= 10){
                        updateFloor();
                }
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
    public void updateTask() {
        int count = CustomAdapter.returnCheck();
        task_value = Integer.parseInt((String) mtask_tf.getText());
        task_value += count;
        mtask_tf.setText(task_value + "");
        shareTextChange();
    }

    public void defaultEXPBoost() {
        if (exp_boost_index == 1) {
            mboost_tf.setText("Boost: ON");
        } else if (exp_boost_index == 0) {
            mboost_tf.setText("Boost: OFF");
        }
    }

    //** count floor for user **//
    public void updateFloor() {
        final MediaPlayer mp = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.level_up);
        mp.start();
        floor_value = Integer.parseInt((String) mfloor_tf.getText());
        floor_value += 1;
        mfloor_tf.setText(floor_value + "");
        mexp_tf.setText("0");
        shareTextChange();
        Toast levelUp = Toast.makeText(getActivity(), "Floor Up! You are doing GREAT!", Toast.LENGTH_LONG);
        levelUp.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        levelUp.show();
    }

    /**
     * save user data into sharedPreferences
     **/
    public void saveData() {
        editor.putString(exp_key, mexp_tf.getText().toString());
        editor.putString(floor_key, mfloor_tf.getText().toString());
        editor.putString(task_key, mtask_tf.getText().toString());
        editor.apply();
    }

    /**
     * load user data from sharedPreferences
     **/
    public void loadData() {
        exp_sp = sharedPreferences.getString(exp_key, "0");
        floor_sp = sharedPreferences.getString(floor_key, "1");
        task_sp = sharedPreferences.getString(task_key, "1");
        exp_boost_index = sharedPreferences.getInt(boost_key, 0);
        bg_index = sharedPreferences.getInt("bg_key", 0);
        ava_index = sharedPreferences.getInt("ava_key", 0);
        exp_value = Integer.parseInt(exp_sp);
        floor_value = Integer.parseInt(floor_sp);
        task_value = Integer.parseInt(task_sp);
    }

    /**
     * update view from sharedPreferences
     **/
    public void updateViews() {
        mexp_tf.setText(exp_sp);
        mfloor_tf.setText(floor_sp);
        mtask_tf.setText(task_sp);
        shareTextChange();
    }


    static Toast toast;

    private static void makeTextAndShow(final Context context, final String t_text, final int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, t_text, duration);
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
        values.put(BodyActionContract.BodyActionEntry.COLUMN_BODY_NAME, "Revision (Sample)");
        values.put(BodyActionContract.BodyActionEntry.COLUMN_BODY_TIME, 30);
        values.put(BodyActionContract.BodyActionEntry.COLUMN_BODY_RESET, "Daily");

        db.insert(BodyActionContract.BodyActionEntry.TABLE_NAME, null, values);

    }


}






