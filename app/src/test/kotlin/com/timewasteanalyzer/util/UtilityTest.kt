package com.timewasteanalyzer.util


import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test


class UtilityTest {

    @Test
    fun equalsEmptyStrings() {
        assertTrue(Utility.equals("", ""))
    }

    @Test
    fun equalsStrings() {
        assertTrue(Utility.equals("a", "a"))
    }

    @Test
    fun equalsFirstStringEmpty() {
        assertFalse(Utility.equals("", "a"))
    }

    @Test
    fun equalsFirstStringNull() {
        assertFalse(Utility.equals(null, "a"))
    }

    @Test
    fun equalsSecondStringEmpty() {
        assertFalse(Utility.equals("a", ""))
    }

    @Test
    fun equalsSecondStringNull() {
        assertFalse(Utility.equals("a", null))
    }

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
