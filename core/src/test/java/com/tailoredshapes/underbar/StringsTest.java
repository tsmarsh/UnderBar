package com.tailoredshapes.underbar;

import org.junit.Test;

import static com.tailoredshapes.underbar.Strings.*;
import static com.tailoredshapes.underbar.UnderBar.list;
import static com.tailoredshapes.underbar.UnderBar.map;
import static org.junit.Assert.*;


public class StringsTest {
    @Test
    public void commaSepTest() throws Exception {
        assertEquals("1,2,3", commaSep(list(1, 2, 3)));
    }

    @Test
    public void joinTest() throws Exception {
        assertEquals("key2:3+key:value", join("+", map("key", "value", "key2", 3), (k, v) -> k + ":" + v));
    }

    @Test
    public void toStringTest() throws Exception {
        assertEquals("1", Strings.toString(1));
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