package com.timewasteanalyzer.permission;


import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.widget.Toast;

import com.timewasteanalyzer.R;


public class PermissionRequester {

    private Activity mActivity;

    public PermissionRequester(Activity activity) {
        mActivity = activity;
    }

    /**
     *
     * @return
     */
    public boolean checkForPermission() {
        boolean granted;

        AppOpsManager appOps = (AppOpsManager) mActivity.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), mActivity
                .getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            // If mode is default further permission check is needed
            granted = (mActivity.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) ==
                    PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }

        if (!granted) {
            // If the permission is not granted yet the settings are shown
            Toast.makeText(mActivity, R.string.grant_permission_message, Toast.LENGTH_LONG)
                 .show();
            mActivity.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }

        return granted;
    }

}
