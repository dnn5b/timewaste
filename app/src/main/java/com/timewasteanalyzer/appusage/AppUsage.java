package com.timewasteanalyzer.appusage;


public class AppUsage {

    String appName, packageName;
    long timeInForeground;
    int launchCount;

    public AppUsage(String pName) {
        this.packageName = pName;
    }

    @Override
    public String toString() {

        return packageName + ": opened " + launchCount + " times (" + timeInForeground / 1000 + "s)\n\n" + "";
    }

    public void addTimeInForeground(long newTimeInForeground) {
        timeInForeground += newTimeInForeground;
    }

    public void increaseLaunchCount() {
        launchCount++;
    }
}
