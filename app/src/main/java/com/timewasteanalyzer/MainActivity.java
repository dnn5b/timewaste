package com.timewasteanalyzer;


import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.timewasteanalyzer.appusage.AppUsage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.PACKAGE_USAGE_STATS;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private long phoneUsageToday;
    private long usageQueryTodayBeginTime;
    List<AppUsage> smallInfoList; //global var

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today:
                    mTextMessage.setText("Today: " + phoneUsageToday / 1000 + "s\n\n" + Arrays.toString(smallInfoList.toArray()));
                    return true;
                case R.id.navigation_week:
                    mTextMessage.setText(R.string.title_week);
                    return true;
                case R.id.navigation_all:
                    mTextMessage.setText(R.string.title_all);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getUsageStatistics();

        checkForPermission();

    }

    private void checkForPermission() {

        if (ContextCompat.checkSelfPermission(this, PACKAGE_USAGE_STATS)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("1");

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                PACKAGE_USAGE_STATS)) {
                System.out.println("2");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                System.out.println("3");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    new String[]{PACKAGE_USAGE_STATS},
                    1337);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            System.out.println("4");
            // Permission has already been granted
            final UsageStatsManager usageStatsManager=(UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
            final int currentYear=Calendar.getInstance().get(Calendar.YEAR);
            final List<UsageStats> queryUsageStats=usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY,currentYear-2,currentYear);
        }
       /*
       int result = checkSelfPermission(Context.APP_OPS_SERVICE);

        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        appOps.checkPackage(android.os.Process.myPid(), context.getPackageName());
        int mode = appOps.checkOp( context.getPackageName(),android.os.Process.myPid(), context.getPackageName());
        int mode2= appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myPid(), context.getPackageName());
        return mode == MODE_ALLOWED;
        */

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1337: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    System.out.println("5");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    System.out.println("6");

                    final UsageStatsManager usageStatsManager=(UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
                    final int currentYear=Calendar.getInstance().get(Calendar.YEAR);
                    final List<UsageStats> queryUsageStats=usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY,currentYear-2,currentYear);
                    /*mTextMessage.setText(Arrays.toString(queryUsageStats.toArray()));*/
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    void getUsageStatistics() {

        UsageEvents.Event currentEvent;
        List<UsageEvents.Event> allEvents = new ArrayList<>();
        HashMap<String, AppUsage> map = new HashMap<String, AppUsage>();

        long currTime = System.currentTimeMillis();
        long startTime = currTime - 1000 * 3600 * 3; //querying past three hours

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);

        assert mUsageStatsManager != null;
        UsageEvents usageEvents = mUsageStatsManager.queryEvents(startTime, currTime);

        //capturing all events in a array to compare with next element

        while (usageEvents.hasNextEvent()) {
            currentEvent = new UsageEvents.Event();
            usageEvents.getNextEvent(currentEvent);
            if (currentEvent.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND || currentEvent.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                allEvents.add(currentEvent);
                String key = currentEvent.getPackageName();
                // taking it into a collection to access by package name
                if (map.get(key) == null)
                    map.put(key, new AppUsage(key));
            }
        }

        //iterating through the arraylist
        for (int i = 0; i < allEvents.size() - 1; i++) {
            UsageEvents.Event E0 = allEvents.get(i);
            UsageEvents.Event E1 = allEvents.get(i + 1);

            //for launchCount of apps in time range
            if (!E0.getPackageName()
                   .equals(E1.getPackageName()) && E1.getEventType() == 1) {
                // if true, E1 (launch event of an app) app launched
                map.get(E1.getPackageName()).increaseLaunchCount();
            }

            //for UsageTime of apps in time range
            if (E0.getEventType() == 1 && E1.getEventType() == 2 && E0.getClassName()
                                                                      .equals(E1.getClassName())) {
                long diff = E1.getTimeStamp() - E0.getTimeStamp();
                phoneUsageToday += diff; //global Long var for total usagetime in the timerange
                map.get(E0.getPackageName()).addTimeInForeground(diff);
            }
        }
        //transferred final data into modal class object
        smallInfoList = new ArrayList<>(map.values());
    }

}
