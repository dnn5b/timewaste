package com.timewasteanalyzer.usage.timeline


import android.content.Context
import android.graphics.drawable.Drawable
import com.timewasteanalyzer.R
import org.threeten.bp.LocalDateTime


class TimelineItemData(val context: Context, var mType: Type, val mStartDate: LocalDateTime) {

    enum class Type(val value: Int) {
        USAGE(0), BREAK(1)
    }

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
        val currentBlockEnd = mStartDate.plusSeconds(mDuration / 1000)
        if (currentEventDate != null && currentEventDate.isBefore(currentBlockEnd.plusMinutes(5))) {
            // If the passed date is in a 5min block from current end
            result = true
        }
        return result
    }

    fun getColor(): Int {
        return if (mType == Type.USAGE) {
            getColorForUsage()
        } else {
            R.color.grey_light
        }
    }

    private fun getColorForUsage(): Int {
        return when {
            mDuration < 60000 -> // Below 1min
                R.color.timeline_low
            mDuration < 300000 -> // Below 5min
                R.color.timeline_medium
            else -> R.color.timeline_high
        }
    }

}