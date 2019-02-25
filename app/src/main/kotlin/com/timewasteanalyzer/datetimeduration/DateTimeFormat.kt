package com.timewasteanalyzer.datetimeduration

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateTimeFormat {

    private var mDateTime: LocalDateTime

    constructor(dateTimeLong: Long) {
        mDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTimeLong), ZoneId.systemDefault())
    }

    constructor(dateTime: LocalDateTime) {
        mDateTime = dateTime
    }

    /**
     * Returns the {mDateTime} in format 'dd.MM.yyyy'.
     */
    fun getShortTime(): String {
        return mDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }

}
