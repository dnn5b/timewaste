package com.timewasteanalyzer.usage.model;


import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;


public class AppUsageTest {

    private AppUsage tut;

    @Mock
    Context mockedContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        tut = new AppUsage(mockedContext, "");
    }

    @Test
    public void foregroundTimeString_seconds() {
        tut.addTimeInForeground(5000);

        assertEquals("00:00:05", tut.getForegroundTimeString());
    }

    @Test
    public void foregroundTimeString_minutes() {
        tut.addTimeInForeground(65000);

        assertEquals("00:01:05", tut.getForegroundTimeString());
    }

    @Test
    public void foregroundTimeString_hours() {
        tut.addTimeInForeground(1051565000);

        assertEquals("04:06:05", tut.getForegroundTimeString());
    }

    @Test
    public void updatePercentage10() {
        tut.addTimeInForeground(11561);
        tut.updatePercentage(115610);

        assertEquals(10, tut.getPercent());
    }

    @Test
    public void updatePercentage50() {
        tut.addTimeInForeground(10);
        tut.updatePercentage(20);

        assertEquals(50, tut.getPercent());
    }

    @Test
    public void updatePercentage100() {
        tut.addTimeInForeground(115618);
        tut.updatePercentage(115618);

        assertEquals(100, tut.getPercent());
    }
}
