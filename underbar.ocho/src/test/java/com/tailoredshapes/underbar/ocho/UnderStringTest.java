package com.tailoredshapes.underbar.ocho;

import com.tailoredshapes.underbar.ocho.UnderString;
import org.junit.Test;

import static com.tailoredshapes.underbar.ocho.UnderString.*;
import static com.tailoredshapes.underbar.ocho.UnderBar.list;
import static com.tailoredshapes.underbar.ocho.UnderBar.map;
import static org.junit.Assert.*;


public class UnderStringTest {

    @Test
    public void reverseTest() throws Exception {
        assertEquals("54321", reverse("12345"));
    }


    @Test
    public void commaSepTest() throws Exception {
        assertEquals("1,2,3", commaSep(list(1, 2, 3)));
    }

    @Test
    public void joinTest() throws Exception {
        assertEquals("1+2+3", join("+", list(1,2,3)));
    }

    @Test
    public void joinNoSeperatorTest() throws Exception {
        assertEquals("123", join(list("1","2","3")));
    }

    @Test
    public void joinArray() throws Exception {
        assertEquals("123", join("1","2","3"));
        assertEquals("123", join(1,2,3));
    }


    @Test
    public void toStringTest() throws Exception {
        assertEquals("1", UnderString.toString(1));
    }

    @Test
    public void urlEncodeTest() throws Exception {
        assertEquals("foo%7Cbar%2Bspam", urlEncode("foo|bar+spam"));
    }

    @Test
    public void hasContentTest() throws Exception {
        assertTrue(hasContent("  floops  "));
        assertFalse(hasContent("    "));
    }

}