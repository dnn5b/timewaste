package com.timewasteanalyzer.dateduration

import org.threeten.bp.Duration


class DurationFormat(durationMillis: Long) {

    private var mDuration: Duration = Duration.ofMillis(durationMillis)

    /**
     * Returns the {mDuration} in format 'dd.MM.yyyy'.
     */
    fun getShort(): String {
        return String.format("%02d:%02d:%02d",
                mDuration.seconds / 3600,
                (mDuration.seconds % 3600) / 60,
                (mDuration.seconds % 60))
    }

    /**
     * Returns the {mDuration} in hours, minutes and seconds if there are any. E.g. '2h', '2h 5min' or '5min'.
     */
    fun getShortText(): String {
        val builder = StringBuilder()

        // Hours
        val hours = mDuration.seconds / 3600
        if (hours > 0) {
            builder.append(hours).append("h")
        }

        // Minutes
        val minutes = mDuration.seconds % 3600 / 60
        if (minutes > 0) {
            if (builder.isNotEmpty()) builder.append(" ")
            builder
                .append(minutes)
                .append("min")
        }

        // Seconds
        val seconds = mDuration.seconds % 60
        if (seconds > 0) {
            if (builder.isNotEmpty()) builder.append(" ")
            builder.append(seconds).append("s")
        }

        return builder.toString()
    }

}