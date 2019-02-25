package com.timewasteanalyzer.datetimeduration

import java.time.Duration

class DurationFormat {

    private var mDuration: Duration

    constructor(durationMillis: Long) {
        mDuration = Duration.ofMillis(durationMillis)
    }

    /**
     * Returns the {mDuration} in format 'dd.MM.yyyy'.
     */
    fun getShort(): String {
        return String.format("%02d:%02d:%02d",
                mDuration.seconds / 3600,
                (mDuration.seconds % 3600) / 60,
                (mDuration.seconds % 60))
    }

}