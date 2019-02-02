package com.timewasteanalyzer.usage.control;


import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import com.timewasteanalyzer.usage.model.AppUsage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class UsageRepository {

    private long phoneUsageToday;
    private long usageQueryTodayBeginTime;
    List<AppUsage> mAppUsageList;

    private Context mContext;

    public UsageRepository(Context context) {
        mContext = context;
    }

    public String getHeading() {
        return "TODO";
    }

    public List<AppUsage> getUsageList() {
        if (mAppUsageList == null) {
            mAppUsageList = new ArrayList<>();
        }
        return mAppUsageList;
    }

    public void getUsageStatistics() {
        UsageEvents.Event currentEvent;
        List<UsageEvents.Event> allEvents = new ArrayList<>();
        HashMap<String, AppUsage> map = new HashMap<String, AppUsage>();

        long currTime = System.currentTimeMillis();
        long startTime = currTime - 1000 * 3600 * 24; // querying last day

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) mContext.getSystemService(Context
                .USAGE_STATS_SERVICE);

        assert mUsageStatsManager != null;
        UsageEvents usageEvents = mUsageStatsManager.queryEvents(startTime, currTime);

        //capturing all events in a array to compare with next element
        while (usageEvents.hasNextEvent()) {
            currentEvent = new UsageEvents.Event();
            usageEvents.getNextEvent(currentEvent);
            if (currentEvent.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND || currentEvent.getEventType() ==
                    UsageEvents.Event.MOVE_TO_BACKGROUND) {
                allEvents.add(currentEvent);
                String key = currentEvent.getPackageName();
                // taking it into a collection to access by package name
                if (map.get(key) == null)
                    map.put(key, new AppUsage(mContext, key));
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
                map.get(E1.getPackageName())
                   .increaseLaunchCount();
            }

            //for UsageTime of apps in time range
            if (E0.getEventType() == 1 && E1.getEventType() == 2 && E0.getClassName()
                                                                      .equals(E1.getClassName())) {
                long diff = E1.getTimeStamp() - E0.getTimeStamp();
                phoneUsageToday += diff; //global Long var for total usagetime in the timerange
                map.get(E0.getPackageName())
                   .addTimeInForeground(diff);
                map.get(E0.getPackageName())
                   .updatePercentage(phoneUsageToday);
            }
        }
        updateUsageList(map.values());
    }

    private void updateUsageList(Collection<AppUsage> values) {
        mAppUsageList.clear();
        mAppUsageList.addAll(values);
        mAppUsageList.sort((usage1, usage2) -> {
            long time1 = usage1.getMsInForeground();
            long time2 = usage2.getMsInForeground();
            return time1 < time2 ? 1 : -1;
        });
    }

    public String getTodayHeading() {
        int seconds = (int) (phoneUsageToday / 1000) % 60;
        int minutes = (int) ((phoneUsageToday / (1000 * 60)) % 60);
        int hours = (int) ((phoneUsageToday / (1000 * 60 * 60)) % 24);

        return new StringBuilder().append("Today: ")
                                  .append(String.format("%02d:%02d:%02d", hours, minutes, seconds))
                                  .toString();
    }
}