package com.timewasteanalyzer.usage.model;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


public class AppUsage {

    private Drawable appIcon;

    private String appName;
    private String packageName;

    private long msInForeground;

    private int launchCount;
    private int percent;

    public AppUsage(Context context, String packageName) {
        this.packageName = packageName;

        // Determine application name and icon
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packageName, 0);
            this.appName = (String) pm.getApplicationLabel(ai);
            this.appIcon = pm.getApplicationIcon(ai);

        } catch (final PackageManager.NameNotFoundException e) {
            // TODO handle exception
        } catch (Exception e) {
            // TODO handle exception
        }
    }

    public long getMsInForeground() {
        return msInForeground;
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
        return percent;
    }

    public int getLaunchCount() {
        return launchCount;
    }

    public String getForegroundTimeString() {
        int seconds = (int) (msInForeground / 1000) % 60;
        int minutes = (int) ((msInForeground / (1000 * 60)) % 60);
        int hours = (int) ((msInForeground / (1000 * 60 * 60)) % 24);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void addTimeInForeground(long diff) {
        this.msInForeground += diff;
    }

    public void updatePercentage(long total) {
        this.percent = (int) (msInForeground * 100.0 / total + 0.5);
    }
}
