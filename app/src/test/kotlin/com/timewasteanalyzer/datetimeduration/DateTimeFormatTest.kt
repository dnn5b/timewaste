package com.timewasteanalyzer.datetimeduration

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class DateTimeFormatTest {

    @Test
    fun getShortDateFromLong() {
        val validShortTest = DateTimeFormat(2321312321312L)

        assertEquals("24.07.2043", validShortTest.getShortTime())
    }

    @Test
    fun getShortDateFromLocalDate() {
        val validShortTest = DateTimeFormat(LocalDateTime.of(2015, 12, 12, 13, 37, 1))

        assertEquals("12.12.2015", validShortTest.getShortTime())
    }

}