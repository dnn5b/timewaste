package com.timewasteanalyzer.usage.control

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UsageRepositoryTest {

    private lateinit var tut: UsageRepository

    @Mock
    private lateinit var mockedContext: Context

    @Mock
    private lateinit var mockedStatsManager: UsageStatsManager

    @Mock
    private lateinit var usageEvents: UsageEvents

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(mockedContext.getSystemService(any())).thenReturn(mockedStatsManager)
        Mockito.`when`(mockedStatsManager.queryEvents(any(), any())).thenReturn(usageEvents)

        tut = UsageRepository.getInstance(mockedContext)
    }

    @Test
    fun queryUsageStatisticsForType() {
//        Mockito.`when`(usageEvents.hasNextEvent()).thenReturn(true, true, false)

        tut.queryUsageStatisticsForCurrentType(FilterType.WEEK)

        var result = tut.mUsageList
    }

    // Workaround for null values in Mockito. Kotlin doesn't accept that.
    fun <T> any(): T = Mockito.any<T>()
}