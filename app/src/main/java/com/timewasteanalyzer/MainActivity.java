package com.timewasteanalyzer;


import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.timewasteanalyzer.usage.control.UsageRepository;
import com.timewasteanalyzer.usage.list.UsageAdapter;

import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.PACKAGE_USAGE_STATS;


public class MainActivity extends AppCompatActivity {

    private UsageRepository repo = new UsageRepository(this);

    private TextView mTabHeading;
    private RecyclerView.Adapter mUsageAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today:
                    mTabHeading.setText(repo.getTodayHeading());
                    mUsageAdapter.notifyDataSetChanged();
                    return true;
                case R.id.navigation_week:
                    mTabHeading.setText(R.string.title_week);
                    return true;
                case R.id.navigation_all:
                    mTabHeading.setText(R.string.title_all);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHeading = findViewById(R.id.usage_heading);

        RecyclerView usageScrollView = findViewById(R.id.usage_recyclerview);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        usageScrollView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        usageScrollView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mUsageAdapter = new UsageAdapter(repo.getUsageList());
        usageScrollView.setAdapter(mUsageAdapter);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        repo.getUsageStatistics();
        checkForPermission();
    }

    private void checkForPermission() {
        if (ContextCompat.checkSelfPermission(this, PACKAGE_USAGE_STATS) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("1");
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PACKAGE_USAGE_STATS)) {
                System.out.println("2");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                System.out.println("3");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{PACKAGE_USAGE_STATS}, 1337);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            System.out.println("4");
            // Permission has already been granted
            final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context
                    .USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
            final int currentYear = Calendar.getInstance()
                                            .get(Calendar.YEAR);
            final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager
                    .INTERVAL_YEARLY, currentYear - 2, currentYear);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1337: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    System.out.println("5");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    System.out.println("6");

                    final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context
                            .USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
                    final int currentYear = Calendar.getInstance()
                                                    .get(Calendar.YEAR);
                    final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager
                            .INTERVAL_YEARLY, currentYear - 2, currentYear);
                    /*mTabHeading.setText(Arrays.toString(queryUsageStats.toArray()));*/
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
