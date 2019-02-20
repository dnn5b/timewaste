package com.timewasteanalyzer.settings

import android.content.Context
import android.content.SharedPreferences
import com.timewasteanalyzer.util.SingletonHolder


class PreferencesRepository private constructor(context: Context) {

    private var mSharedPrefs: SharedPreferences = context.getSharedPreferences("timeanalyzer_preferences", Context.MODE_PRIVATE)

    companion object : SingletonHolder<PreferencesRepository, Context>(::PreferencesRepository)

    fun saveStringToPrefs(setting: SETTING, stringToSave: String) {
        mSharedPrefs.edit().putString(setting.value, stringToSave).commit()
    }

    fun saveBooleanToPrefs(setting: SETTING, booleanToSave: Boolean) {
        mSharedPrefs.edit().putBoolean(setting.value, booleanToSave).commit()
    }

    fun getStringFromPref(setting: SETTING): String {
        return mSharedPrefs.getString(setting.value, "default String from prefs")
    }

    fun getBooleanFromPref(setting: SETTING): Boolean {
        return mSharedPrefs.getBoolean(setting.value, false)
    }

}