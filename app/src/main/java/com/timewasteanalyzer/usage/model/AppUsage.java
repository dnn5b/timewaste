package com.timewasteanalyzer.usage.model;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


public class AppUsage {

    private Drawable appIcon;

    private String appName;
    private String packageName;
    private Time timeInForeground;

    private int launchCount;
    private int percent;

    public AppUsage(Context context, String packageName) {
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packageName, 0);
            this.appName = (String) pm.getApplicationLabel(ai);
            this.appIcon = pm.getApplicationIcon(ai);

        } catch (final PackageManager.NameNotFoundException e) {

        }
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

    public Drawable getAppIcon() {
        return appIcon;
    }

    public int getPercent() {
        //return percent;
        return 50;
    }

    public int getLaunchCount() {
        return launchCount;
    }
}
