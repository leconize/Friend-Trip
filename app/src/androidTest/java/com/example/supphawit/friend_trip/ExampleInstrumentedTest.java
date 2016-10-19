package com.example.supphawit.friend_trip;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(TestCreateUser.class)
public class ExampleInstrumentedTest {
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        //assertEquals("com.example.supphawit.friend_trip", appContext.getPackageName());
    }
}
