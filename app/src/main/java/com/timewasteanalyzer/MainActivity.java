package com.timewasteanalyzer;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.timewasteanalyzer.permission.PermissionRequester;
import com.timewasteanalyzer.usage.control.FilterType;
import com.timewasteanalyzer.usage.control.UsageRepository;
import com.timewasteanalyzer.usage.list.UsageAdapter;


public class MainActivity extends AppCompatActivity {

    private UsageRepository repo = new UsageRepository(this);

    private TextView mTabHeading;
    private RecyclerView.Adapter mUsageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHeading = findViewById(R.id.usage_heading);

        setupRecyclerView();
        setupBottomNavigation();

        if (new PermissionRequester(this).checkForPermission()) {
            repo.getUsageStatistics();
        }
    }

    private void setupRecyclerView() {
        RecyclerView usageScrollView = findViewById(R.id.usage_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        usageScrollView.setLayoutManager(layoutManager);

        // Layout size won't change so performance can be improved with fix size
        usageScrollView.setHasFixedSize(true);

        // Add adapter containing the current list of usages
        mUsageAdapter = new UsageAdapter(repo.getUsageList());
        usageScrollView.setAdapter(mUsageAdapter);
    }

    private void setupBottomNavigation() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_today:
                    updateView(FilterType.DAY);
                    return true;
                case R.id.navigation_week:
                    updateView(FilterType.WEEK);
                    return true;
                case R.id.navigation_all:
                    updateView(FilterType.ALL);
                    return true;
            }
            return false;
        });
    }

    private void updateView(FilterType filterType) {
        String headingText = "";
        switch (filterType) {
            case DAY:
                headingText = repo.getTodayHeading();
                break;
            case WEEK:
                headingText = getString(R.string.title_week);
                break;
            case ALL:
                headingText = getString(R.string.title_all);
                break;
        }
        mTabHeading.setText(headingText);
        mUsageAdapter.notifyDataSetChanged();
    }

}
