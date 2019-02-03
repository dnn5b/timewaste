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

    private List<AppUsage> mAppUsageList;
    private UsageStatsManager mUsageStatsManager;
    private Context mContext;

    private HashMap<String, AppUsage> appUsageMap;
    private List<UsageEvents.Event> allEvents;

    private long phoneUsageTotal;

    private static UsageRepository sInstance;

    /**
     * Private constructor only called in Singleton initliazer {@link #getInstance(Context)}.
     *
     * @param context the current {@link Context}
     */
    private UsageRepository(Context context) {
        mContext = context;
        mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }

    /**
     * Singleton initializer.
     *
     * @param context the current {@link Context}.
     * @return the singleton instance of this class
     */
    public static UsageRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UsageRepository(context);

        }
        return sInstance;
    }

    /**
     * Queries the usage data for the passed {@link FilterType}.
     *
     * @param filterType the current {@link FilterType}
     */
    public void queryUsageStatisticsForType(FilterType filterType) {
        resetFormerData();

        long now = System.currentTimeMillis();
        long startTime = 0;
        switch (filterType) {
            case DAY:
                // TODO filter from 0:00 instead of 24h
                startTime = now - 1000 * 3600 * 24; // querying last day
                break;

            case WEEK:
                // TODO test whether last weeks events are queried like this
                startTime = now - 1000 * 3600 * 24 * 7;
                break;

            case ALL:
                // TODO test whether all events are queried like this
                startTime = 0;
                break;
        }

        // Query events and initialize repository data
        assert mUsageStatsManager != null;
        UsageEvents queriedUsage = mUsageStatsManager.queryEvents(startTime, now);
        initializeDataBasedOn(queriedUsage);

        // Iterate through usage list
        for (int i = 0; i < allEvents.size() - 1; i++) {
            UsageEvents.Event event = allEvents.get(i);
            UsageEvents.Event event2 = allEvents.get(i + 1);

            // Calculate opened counter
            if (!event.getPackageName()
                      .equals(event2.getPackageName()) && event2.getEventType() == 1) {
                // if true, E1 (launch event of an app) app launched
                appUsageMap.get(event2.getPackageName())
                           .increaseLaunchCount();
            }

            // UsageTime of apps in time range
            if (event.getEventType() == 1 && event2.getEventType() == 2 && event.getClassName()
                                                                                .equals(event2.getClassName())) {
                // Update total phone usage
                long diff = event2.getTimeStamp() - event.getTimeStamp();
                phoneUsageTotal += diff;

                // Update current usage
                AppUsage currentUsage = appUsageMap.get(event.getPackageName());
                currentUsage.addTimeInForeground(diff);
                currentUsage.updatePercentage(phoneUsageTotal);
            }
        }
        updateUsageList(appUsageMap.values());
    }

    private void resetFormerData() {
        phoneUsageTotal = 0;
        appUsageMap = new HashMap();
        allEvents = new ArrayList<>();
    }

    /**
     * Resets the {@link #appUsageMap} and {@link #allEvents}. Afterwards the data is updated based on the passed
     * {@link UsageEvents}.
     *
     * @param usageEvents the queried {@link UsageEvents}
     */
    private void initializeDataBasedOn(UsageEvents usageEvents) {
        UsageEvents.Event currentEvent;
        while (usageEvents.hasNextEvent()) {
            currentEvent = new UsageEvents.Event();
            usageEvents.getNextEvent(currentEvent);
            if (currentEvent.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND || currentEvent.getEventType() ==
                    UsageEvents.Event.MOVE_TO_BACKGROUND) {
                allEvents.add(currentEvent);

                // Save usages into map
                String key = currentEvent.getPackageName();
                if (appUsageMap.get(key) == null)
                    appUsageMap.put(key, new AppUsage(mContext, key));
            }
        }
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

    public String getTotalTypeForFilter() {
        return getTodayHeading();
    }

    public List<AppUsage> getUsageList() {
        if (mAppUsageList == null) {
            mAppUsageList = new ArrayList<>();
        }
        return mAppUsageList;
    }

    public String getTodayHeading() {
        int seconds = (int) (phoneUsageTotal / 1000) % 60;
        int minutes = (int) ((phoneUsageTotal / (1000 * 60)) % 60);
        int hours = (int) ((phoneUsageTotal / (1000 * 60 * 60)) % 24);

        return new StringBuilder().append("Today: ")
                                  .append(String.format("%02d:%02d:%02d", hours, minutes, seconds))
                                  .toString();
    }
}