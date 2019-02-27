package com.timewasteanalyzer.dateduration

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration
import java.time.temporal.ChronoUnit

class DurationFormatTest {

    @Test
    fun getShortDateFromLong() {
        val duration = Duration
                .of(3, ChronoUnit.HOURS)
                .plus(4, ChronoUnit.MINUTES)
                .plus(5, ChronoUnit.SECONDS)

        val formatted = DurationFormat(duration.toMillis())

        assertEquals("03:04:05", formatted.getShort())
    }

    @Test
    fun getShortText_hoursOnly() {
        assertEquals("3h", DurationFormat(10800000).getShortText())
    }

    @Test
    fun getShortText_hoursAndMinutes() {
        assertEquals("3h 12min", DurationFormat(11520000).getShortText())
    }

    @Test
    fun getShortText_minutesOnly() {
        assertEquals("8min", DurationFormat(480000).getShortText())
    }

    @Test
    fun getShortText_minutesAndSeconds() {
        assertEquals("18min 46s", DurationFormat(1126000).getShortText())
    }

    @Test
    fun getShortText_secondsOnly() {
        assertEquals("59s", DurationFormat(59000).getShortText())
    }

    @Test
    fun getShortText_HoursMinutesSeconds() {
        assertEquals("8h 18min 16s", DurationFormat(29896000).getShortText())
    }
}