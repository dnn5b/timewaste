package com.timewasteanalyzer.usage.timeline


import android.content.Context
import android.graphics.drawable.Drawable
import com.timewasteanalyzer.R
import com.timewasteanalyzer.util.AppDataHandler
import org.threeten.bp.LocalDateTime


class TimelineItemData(val context: Context, var mType: Type, val mStartDate: LocalDateTime) {

    enum class Type(val value: Int) {
        USAGE(0), BREAK(1)
    }

    /** In milliseconds. */
    var mDuration: Long = 0

    /** All apps used in this slot. */
    val mApps: HashMap<AppDataHandler, Long> = HashMap()

    fun addEntry(duration: Long, packageName: String) {
        mDuration += duration

        val appDataHandler = AppDataHandler(context, packageName)
        if (mApps.containsKey(appDataHandler)) {
            val newDuration = mApps[appDataHandler]?.plus(duration)
            mApps[appDataHandler] = newDuration!!
        } else {
            mApps[appDataHandler] = duration
        }
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

    fun getTop5Apps(): Array<Drawable?> {
        return mApps.keys
                .take(5)
                .distinctBy { appData -> appData.getmAppName() }
                .map { appData -> appData.getmAppIcon() }
                .distinct()
                .toTypedArray()
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