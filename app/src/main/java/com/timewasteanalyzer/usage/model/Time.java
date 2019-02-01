package com.timewasteanalyzer.usage.model;


public class Time {
// TODO Replace with duration?
    /**
     * In milliseconds.
     */
    private long mTime;

    public Time(long time) {
        this.mTime = time;
    }

    public long getTime() {
        return mTime;
    }

    public void addTime(long time) {
        mTime += time;
    }

    public String asSecondString() {
        return (mTime / 1000) + "s";
    }

    public String asTimeString() {
        int seconds = (int) (mTime / 1000) % 60;
        int minutes = (int) ((mTime / (1000 * 60)) % 60);
        int hours = (int) ((mTime / (1000 * 60 * 60)) % 24);

        return new StringBuilder().append(hours > 0 ? String.valueOf(hours) : "00")
                                  .append(":")
                                  .append(minutes > 0 ? String.valueOf(minutes) : "00")
                                  .append(":")
                                  .append(seconds > 0 ? String.valueOf(seconds) : "00")
                                  .append("h")
                                  .toString();

    }
}
