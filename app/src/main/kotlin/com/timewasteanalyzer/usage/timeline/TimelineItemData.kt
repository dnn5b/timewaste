package com.timewasteanalyzer.usage.timeline


import android.content.Context
import android.graphics.drawable.Drawable
import com.timewasteanalyzer.R
import java.time.LocalDateTime


class TimelineItemData(val context: Context, val mStartDate: LocalDateTime) {

    var mAppIcons: MutableList<Drawable> = ArrayList()

    /** In milliseconds. */
    var mDuration: Long = 0

    var mPackageNames: MutableList<String> = ArrayList()

    fun addEntry(duration: Long, packageName: String) {
//    fun addEntry(duration: Long, icon: Drawable) {
        mDuration += duration
        mPackageNames.add(packageName)
//        mAppIcons.add(icon)
    }

    fun isInSameBlock(currentEventDate: LocalDateTime?): Boolean {
        var result = false
        var currentBlockEnd = mStartDate.plusSeconds(mDuration / 1000)
        if (currentEventDate != null && currentEventDate.isBefore(currentBlockEnd.plusMinutes(5))) {
            // If the passed date is in a 5min block from current end
            result = true
        }
        return result
    }

    fun getUsageInSeconds(): String {
        return (mDuration / 1000).toString()
    }

    fun getColor(): Int {
        return if (mPackageNames.isEmpty()) {
            R.color.grey
        } else {
            R.color.colorAccent
        }
    }

}