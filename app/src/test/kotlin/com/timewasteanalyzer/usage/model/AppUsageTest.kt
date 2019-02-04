package com.timewasteanalyzer.usage.model


import android.content.Context
import junit.framework.Assert.assertEquals

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class AppUsageTest {

    private var tut: AppUsage? = null

    @Mock
    internal var mockedContext: Context? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        tut = AppUsage(mockedContext!!, "")
    }

    @Test
    fun foregroundTimeString_seconds() {
        tut!!.addTimeInForeground(5000)

        assertEquals("00:00:05", tut!!.foregroundTimeString)
    }

    @Test
    fun foregroundTimeString_minutes() {
        tut!!.addTimeInForeground(65000)

        assertEquals("00:01:05", tut!!.foregroundTimeString)
    }

    @Test
    fun foregroundTimeString_hours() {
        tut!!.addTimeInForeground(1051565000)

        assertEquals("04:06:05", tut!!.foregroundTimeString)
    }

    @Test
    fun updatePercentage10() {
        tut!!.addTimeInForeground(11561)
        tut!!.updatePercentage(115610)

        assertEquals(10, tut!!.percent)
    }

    @Test
    fun updatePercentage50() {
        tut!!.addTimeInForeground(10)
        tut!!.updatePercentage(20)

        assertEquals(50, tut!!.percent)
    }

    @Test
    fun updatePercentage100() {
        tut!!.addTimeInForeground(115618)
        tut!!.updatePercentage(115618)

        assertEquals(100, tut!!.percent)
    }
}
