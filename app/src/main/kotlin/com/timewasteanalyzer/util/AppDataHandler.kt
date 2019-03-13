package com.timewasteanalyzer.util


import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable


/**
 * Created by Dennis Jonietz on 13.03.2019.
 */
class AppDataHandler(context: Context, packageName: String) {

    private var mAppName: String? = null
    private var mAppIcon: Drawable? = null

    init {
        val packageManager = context.packageManager
        try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            mAppName = packageManager.getApplicationLabel(appInfo) as String
            mAppIcon = packageManager.getApplicationIcon(appInfo)

        } catch (e: PackageManager.NameNotFoundException) {
            // TODO handle exception
        } catch (e: Exception) {
            // TODO handle exception
        }
    }

    fun getmAppIcon(): Drawable? {
        return mAppIcon
    }

    fun getmAppName(): String? {
        return mAppName
    }
}
