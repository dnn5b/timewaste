package com.timewasteanalyzer.usage.timeline


import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable


class TimelineItemData(context: Context, val packageName: String) {

    var mAppIcons: MutableList<Drawable> = ArrayList()

    /** In milliseconds. */
    var mDuration: Long = 0

    fun addEntry(duration: Long, icon: Drawable) {
        mDuration += duration
        mAppIcons.add(icon)
    }

}