package com.timewasteanalyzer.usage.list


import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable


class ListItemData(context: Context, val packageName: String) {

    var mAppIcon: Drawable? = null
        private set

    var mAppName: String? = null
        private set

    var msInForeground: Long = 0
        private set

    var mLaunchCount: Int = 0
        private set

    var mPercent: Int = 0
        private set

    val foregroundTimeString: String
        get() {
            val seconds = (msInForeground / 1000).toInt() % 60
            val minutes = (msInForeground / (1000 * 60) % 60).toInt()
            val hours = (msInForeground / (1000 * 60 * 60) % 24).toInt()

            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }

    init {
        // Determine application name and icon
        val pm = context.packageManager
        val ai: ApplicationInfo
        try {
            ai = pm.getApplicationInfo(packageName, 0)
            this.mAppName = pm.getApplicationLabel(ai) as String
            this.mAppIcon = pm.getApplicationIcon(ai)

        } catch (e: PackageManager.NameNotFoundException) {
            // TODO handle exception
        } catch (e: Exception) {
            // TODO handle exception
        }
    }

    fun increaseLaunchCount() {
        mLaunchCount++
    }

    fun addTimeInForeground(diff: Long) {
        this.msInForeground += diff
    }

    fun updatePercentage(total: Long) {
        this.mPercent = (msInForeground * 100.0 / total + 0.5).toInt()
    }
}
