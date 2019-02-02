package com.timewasteanalyzer.usage.model;


import android.content.Context;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


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
    public void updatePercentage10() {
        tut.addTimeInForeground(11561);
        tut.updatePercentage(115610);

        Assert.assertEquals(10, tut.getPercent());
    }

    @Test
    public void updatePercentage50() {
        tut.addTimeInForeground(10);
        tut.updatePercentage(20);

        Assert.assertEquals(50, tut.getPercent());
    }

    @Test
    public void updatePercentage100() {
        tut.addTimeInForeground(115618);
        tut.updatePercentage(115618);

        Assert.assertEquals(100, tut.getPercent());
    }
}
