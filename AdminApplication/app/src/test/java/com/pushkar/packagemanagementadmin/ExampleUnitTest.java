package com.pushkar.packagemanagementadmin;

import com.pushkar.packagemanagementadmin.utils.ParserHelper;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_24_login() throws ParseException {
        assertTrue(ParserHelper.timedOut("01-07-2020-05-42-59"));
        assertEquals(false,ParserHelper.timedOut("01-07-2020-19-51-59"));
    }
}