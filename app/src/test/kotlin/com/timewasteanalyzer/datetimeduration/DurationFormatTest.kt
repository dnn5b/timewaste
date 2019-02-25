package com.timewasteanalyzer.datetimeduration

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

}