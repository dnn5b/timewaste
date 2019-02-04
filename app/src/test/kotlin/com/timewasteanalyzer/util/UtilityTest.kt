package com.timewasteanalyzer.util


import org.junit.Test

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue


class UtilityTest {

    @Test
    fun emptyString() {
        assertTrue(Utility.isEmpty(""))
    }

    @Test
    fun nullString() {
        assertTrue(Utility.isEmpty(null))
    }

    @Test
    fun validString() {
        assertFalse(Utility.isEmpty("some text"))
    }

}
