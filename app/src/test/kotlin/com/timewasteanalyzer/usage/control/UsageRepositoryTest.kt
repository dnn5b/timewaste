package com.timewasteanalyzer.usage.control

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.any
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

    private var events: List<UsageEvents.Event> = ArrayList()

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

        tut.queryUsageStatisticsForType(FilterType.WEEK)

        var result = tut.usageList

    }
}