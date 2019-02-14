package com.timewasteanalyzer.permission


import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.widget.Toast

import com.timewasteanalyzer.R


class PermissionRequester(private val mActivity: Activity) {

    fun checkForPermission(): Boolean {
        val granted: Boolean

        val appOps = mActivity.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), mActivity
                .packageName)

        granted = if (mode == AppOpsManager.MODE_DEFAULT) {
            // If mode is default further permission check is needed
            mActivity.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }

        if (!granted) {
            // If the permission is not granted yet the settings are shown
            Toast.makeText(mActivity, R.string.grant_permission_message, Toast.LENGTH_LONG)
                    .show()
            mActivity.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))

            // Workaround so Activity is initialized after granting the permission is to stop it now
            mActivity.finish()
        }

        return granted
    }

}
