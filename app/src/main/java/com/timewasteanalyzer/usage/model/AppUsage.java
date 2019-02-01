package com.timewasteanalyzer.usage.model;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;


public class AppUsage {

    private String appName;
    private String packageName;
    private Time timeInForeground;
    private int launchCount;

    public AppUsage(Context context, String packageName) {
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        this.appName = (String) (ai != null ? pm.getApplicationLabel(ai) : "unknown");
        this.packageName = packageName;
    }

    public Time getTimeInForeground() {
        if (timeInForeground == null) {
            timeInForeground = new Time(0);
        }
        return timeInForeground;
    }

    public void increaseLaunchCount() {
        launchCount++;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getLaunchCount() {
        return launchCount;
    }
}
