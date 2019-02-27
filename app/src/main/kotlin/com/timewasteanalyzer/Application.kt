package com.timewasteanalyzer

import com.jakewharton.threetenabp.AndroidThreeTen


class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

}
