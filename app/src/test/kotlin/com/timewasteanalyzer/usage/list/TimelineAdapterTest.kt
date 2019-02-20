package com.timewasteanalyzer.usage.list

import com.timewasteanalyzer.usage.model.AppUsage
import io.mockk.mockkClass
import org.junit.Assert
import org.junit.Test

class TimelineAdapterTest {

    @Test
    fun getItemCount() {
        val list = ArrayList<AppUsage>()
        list.add(mockkClass(AppUsage::class))
        list.add(mockkClass(AppUsage::class))
        list.add(mockkClass(AppUsage::class))

        val tut = TimelineAdapter(list)

        Assert.assertEquals(3, tut.itemCount)
    }

}

