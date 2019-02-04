package com.timewasteanalyzer.util;


import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


public class UtilityTest {

    @Test
    public void emptyString() {
        assertTrue(Utility.INSTANCE.isEmpty(""));
    }

    @Test
    public void nullString() {
        assertTrue(Utility.INSTANCE.isEmpty(null));
    }

    @Test
    public void validString() {
        assertFalse(Utility.INSTANCE.isEmpty("some text"));
    }

}
