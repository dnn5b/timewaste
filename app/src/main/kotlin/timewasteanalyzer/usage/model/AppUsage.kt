package com.timewasteanalyzer.usage.model


import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable


class AppUsage(context: Context, val packageName: String) {

    var appIcon: Drawable? = null
        private set

    var appName: String? = null
        private set

    var msInForeground: Long = 0
        private set

    var launchCount: Int = 0
        private set

    var percent: Int = 0
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
            this.appName = pm.getApplicationLabel(ai) as String
            this.appIcon = pm.getApplicationIcon(ai)

        } catch (e: PackageManager.NameNotFoundException) {
            // TODO handle exception
        } catch (e: Exception) {
            // TODO handle exception
        }

    }

    fun increaseLaunchCount() {
        launchCount++
    }

    fun addTimeInForeground(diff: Long) {
        this.msInForeground += diff
    }

    fun updatePercentage(total: Long) {
        this.percent = (msInForeground * 100.0 / total + 0.5).toInt()
    }
}
