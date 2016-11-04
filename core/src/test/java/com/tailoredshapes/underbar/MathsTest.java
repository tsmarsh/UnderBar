package com.tailoredshapes.underbar;

import org.junit.Test;


import static com.tailoredshapes.underbar.Maths.*;
import static com.tailoredshapes.underbar.UnderBar.list;
import static org.junit.Assert.*;

/**
 * Created by tmarsh on 11/3/16.
 */
public class MathsTest {
    @Test
    public void xorTest() throws Exception {
        assertFalse(xor(true, true));
        assertTrue(xor(false, true));
        assertTrue(xor(true, false));
        assertFalse(xor(false, false));
    }

    @Test
    public void averageTest() throws Exception {
        assertEquals(1, average(list(1L, 1L, 1L)), 0);
        assertEquals(1, averageInt(list(1, 1, 1)), 0);
        assertEquals(1.0, averageDouble(list(1.0, 1.0, 1.0)), 0);
    }



    @Test
    public void sumIt() throws Exception {
        assertEquals(6, sum(list(1L, 2L, 3L)));
        assertEquals(6L, sumInt(list(1, 2, 3)));
        assertEquals(6.0, sumDouble(list(1.0, 2.0, 3.0)), 0);
    }

    @Test
    public void maxMinTest() throws Exception {
        assertEquals(3L, (long) max(list(1L, 2L, 3L)));
        assertEquals(1L, (long) min(list(1L, 2L, 3L)));
    }

    @Test
    public void getMedianValue() throws Exception {
        assertEquals(5.0, median(list(1L, 3L, 5L, 2L, 6L)), 0);
        assertEquals(Double.NaN, median(list()), 0);
    }

}