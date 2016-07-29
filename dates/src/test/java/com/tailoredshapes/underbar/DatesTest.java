package com.tailoredshapes.underbar;

import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.tailoredshapes.underbar.Dates.*;
import static org.junit.Assert.assertEquals;


/**
 * Created by tmarsh on 10/24/16.
 */
public class DatesTest {
    @Test
    public void dateReturnsANewDate() throws Exception {
        ZonedDateTime now = ZonedDateTime.now();

        String date = now.format(DateTimeFormatter.ISO_INSTANT);

        Date expected = Date.from(now.toInstant());

        assertEquals(expected, date(date));
    }

    @Test
    public void instantReturnsANewInstant() throws Exception {
        ZonedDateTime now = ZonedDateTime.now();

        String date = now.format(DateTimeFormatter.ISO_INSTANT);

        assertEquals(now.toInstant(), instant(date));
    }

    @Test
    public void isoDateStringReturnsAISOFormatedString() throws Exception {
        ZonedDateTime now = ZonedDateTime.now();

        String date = now.format(DateTimeFormatter.ISO_INSTANT);

        assertEquals(date, isoString(date(date)));
        assertEquals(date, isoString(now.toInstant()));
    }
}