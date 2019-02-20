package com.timewasteanalyzer.usage.model


import android.content.Context
import android.content.pm.PackageManager
import com.timewasteanalyzer.usage.list.ListItemData
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals

import org.junit.Before
import org.junit.Test


class ListItemDataTest {

    private var tut: ListItemData? = null

    @MockK
    lateinit var mockedContext: Context

    @MockK
    lateinit var mockedPackageManager: PackageManager

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        every { mockedContext.getPackageManager() } returns mockedPackageManager
        tut = ListItemData(mockedContext, "")
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

        assertEquals(10, tut!!.mPercent)
    }

    @Test
    fun updatePercentage50() {
        tut!!.addTimeInForeground(10)
        tut!!.updatePercentage(20)

        assertEquals(50, tut!!.mPercent)
    }

    @Test
    fun updatePercentage100() {
        tut!!.addTimeInForeground(115618)
        tut!!.updatePercentage(115618)

        assertEquals(100, tut!!.mPercent)
    }
}
