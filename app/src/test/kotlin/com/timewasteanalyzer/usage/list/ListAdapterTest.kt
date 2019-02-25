package com.timewasteanalyzer.usage.list

import io.mockk.mockkClass
import org.junit.Assert
import org.junit.Test

class ListAdapterTest {

    @Test
    fun getItemCount() {
        val list = ArrayList<ListItemData>()
        list.add(mockkClass(ListItemData::class))
        list.add(mockkClass(ListItemData::class))
        list.add(mockkClass(ListItemData::class))

        val tut = ListAdapter(list)

        Assert.assertEquals(3, tut.itemCount)
    }

}

