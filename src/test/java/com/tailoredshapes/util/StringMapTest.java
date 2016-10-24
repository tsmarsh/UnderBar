package com.tailoredshapes.util;

import org.junit.Test;

import java.util.Map;

import static com.tailoredshapes.util.Dates.date;
import static org.junit.Assert.*;
import static com.tailoredshapes.util.UnderBar.*;

/**
 * Created by tmarsh on 10/24/16.
 */
public class StringMapTest {
    
    @Test
    public void parseJsonMapsTest() throws Exception {

    }

    @Test
    public void putTest() throws Exception {
        StringMap smap = smap();
        smap.put("foo", "bar");

        assertEquals("bar", smap.string("foo"));
    }

    @Test
    public void sizeTest() throws Exception {
        StringMap smap = smap();
        assertEquals(0, smap.size());
        smap.put("foo", "bar");
        assertEquals(1, smap.size());
    }

    @Test
    public void withTest() throws Exception {
        assertEquals("bar", smap().with("foo", "bar").string("foo"));
    }

    @Test
    public void long_Test() throws Exception {
        StringMap smap = smap("foo", 1l);

        assertEquals(1l, smap.long_("foo"));
    }

    @Test
    public void double_Test() throws Exception {
        StringMap smap = smap("foo", 1.1);

        assertEquals(1.1, smap.double_("foo"), 0);
    }

    @Test
    public void integerTest() throws Exception {
        StringMap smap = smap("foo", 1);

        assertEquals(1, smap.integer("foo"));
    }

    @Test
    public void integerValueTest() throws Exception {
        StringMap smap = smap("foo", 1.1);

        assertEquals(1, smap.integerValue("foo"), 0);
    }

    @Test
    public void parseIntegerTest() throws Exception {
        StringMap smap = smap("foo", "5", "bar", null);

        assertEquals(5, smap.parseInteger("foo", 3));
        assertEquals(3, smap.parseInteger("bar", 3));
    }

    @Test
    public void parseLongTest() throws Exception {
        StringMap smap = smap("foo", "5", "bar", null);

        assertEquals(5, smap.parseLong("foo", 3));
        assertEquals(3, smap.parseLong("bar", 3));
    }

    @Test
    public void integerMaybeTest() throws Exception {
        StringMap smap = smap("foo", optional(1));

        assertEquals(optional(1), smap.integerMaybe("foo"));
    }

    @Test
    public void dateMaybeTest() throws Exception {
        StringMap smap = smap("foo", optional(date("1979-10-10")));

        assertEquals(optional(date("1979-10-10")), smap.dateMaybe("foo"));
    }

    @Test
    public void doubleMaybeTest() throws Exception {
        StringMap smap = smap("foo", optional(1.1));

        assertEquals(optional(1.1), smap.doubleMaybe("foo"));
    }

    @Test
    public void longMaybeTest() throws Exception {
        StringMap smap = smap("foo", optional(1L));

        assertEquals(optional(1L), smap.longMaybe("foo"));
    }

    @Test
    public void boolMaybeTest() throws Exception {
        StringMap smap = smap("foo", optional(false));

        assertEquals(optional(false), smap.boolMaybe("foo"));
    }

    @Test
    public void longValueTest() throws Exception {
        StringMap smap = smap("foo", 1L);

        assertEquals(1L, smap.long_("foo"));
    }

    @Test
    public void stringTest() throws Exception {
        StringMap smap = smap("foo", "bar");

        assertEquals("bar", smap.string("foo"));
    }

    @Test
    public void stringMaybeTest() throws Exception {
        StringMap smap = smap("foo", optional("bar"));

        assertEquals(optional("bar"), smap.longMaybe("foo"));
    }

    @Test
    public void dateTest() throws Exception {
        StringMap smap = smap("foo", date("1979-10-10"));

        assertEquals(date("1979-10-10"), smap.date("foo"));
    }

    @Test
    public void mapTest() throws Exception {
        StringMap smap = smap("foo", 1L);

        assertEquals(list("foo: 1"), smap.map((k,v) -> String.format("%s: %s", k,v)));
    }

    @Test
    public void putIntoTest() throws Exception {
        StringMap other = smap("eggs", "spam");
        assertEquals(smap("foo", "bar", "eggs", "spam"), smap("foo", "bar").putInto(other));
    }


    @Test
    public void hasTest() throws Exception {
        assertTrue( smap("eggs", "spam").has("eggs"));
        assertFalse( smap("eggs", "spam").has("ham"));
    }

    @Test
    public void hasValueTest() throws Exception {
        assertTrue( smap("eggs", optional("spam")).hasValue("eggs"));
        assertFalse( smap("eggs", optional()).hasValue("eggs"));
    }

    @Test
    public void boolTest() throws Exception {
        assertTrue( smap("eggs", true).bool("eggs"));
    }


    @Test
    public void smapTest() throws Exception {
        assertEquals(smap("eggs", "ham"), smap("foo", smap("eggs", "ham")).smap("foo"));
    }

    @Test
    public void makeSmapTest() throws Exception {
        assertEquals(smap("foo", 3), smap("eggs", map("foo", 3)).makeSmap("eggs"));

    }

    @Test
    public void smapsTest() throws Exception {
        assertEquals(list(smap("eggs", "ham")), smap("foo", list(smap("eggs", "ham"))).smaps("foo"));
    }

    @Test
    public void makeSmapsTest() throws Exception {
        assertEquals(list(smap("eggs", "ham")), smap("foo", list(map("eggs", "ham"))).makeSmaps("foo"));
    }

    @Test
    public void parseJsonTest() throws Exception {

    }

    @Test
    public void toJSONStringTest() throws Exception {

    }

    @Test
    public void toStringTest() throws Exception {

    }

    @Test
    public void equalsTest() throws Exception {

    }

    @Test
    public void hashCodeTest() throws Exception {

    }

    @Test
    public void filterKeysTest() throws Exception {

    }

    @Test
    public void rejectKeysTest() throws Exception {

    }

    @Test
    public void selectKeysTest() throws Exception {

    }

    @Test
    public void selectKeys1Test() throws Exception {

    }

    @Test
    public void rejectKeys1Test() throws Exception {

    }

    @Test
    public void toMapTest() throws Exception {

    }

    @Test
    public void toMapDeeplyTest() throws Exception {

    }

    @Test
    public void removeTest() throws Exception {

    }

    @Test
    public void parseDateTest() throws Exception {

    }

    @Test
    public void hasContentTest() throws Exception {

    }

    @Test
    public void stringOptionalTest() throws Exception {

    }

    @Test
    public void stringishTest() throws Exception {

    }

    @Test
    public void longishTest() throws Exception {

    }

    @Test
    public void dateishTest() throws Exception {

    }

    @Test
    public void doublishTest() throws Exception {

    }

    @Test
    public void boolishTest() throws Exception {

    }

    @Test
    public void booleanOptionalTest() throws Exception {

    }

    @Test
    public void mergeTest() throws Exception {

    }

    @Test
    public void parseJSONTest() throws Exception {

    }

    @Test
    public void asStringTest() throws Exception {

    }

    @Test
    public void uuidTest() throws Exception {

    }

    @Test
    public void parseUUIDTest() throws Exception {

    }

    @Test
    public void parseUUIDMaybeTest() throws Exception {

    }

    @Test
    public void numberTest() throws Exception {

    }

    @Test
    public void asUrlTest() throws Exception {

    }

    @Test
    public void isEmptyTest() throws Exception {

    }

    @Test
    public void isEmpty1Test() throws Exception {

    }

    @Test
    public void parseDoubleTest() throws Exception {

    }

    @Test
    public void parseBooleanTest() throws Exception {

    }

    @Test
    public void isNullTest() throws Exception {

    }

    @Test
    public void jsonObjectTest() throws Exception {

    }

    @Test
    public void getListTest() throws Exception {

    }

    @Test
    public void shallowCopyTest() throws Exception {

    }

    @Test
    public void parseDateMaybeTest() throws Exception {

    }

    @Test
    public void keysTest() throws Exception {

    }

    @Test
    public void typeTest() throws Exception {

    }

    @Test
    public void uuidMaybeTest() throws Exception {

    }

    @Test
    public void uuidOrNullTest() throws Exception {

    }

    @Test
    public void parseIsoDateTimeTest() throws Exception {

    }

    @Test
    public void pathTest() throws Exception {

    }

    @Test
    public void parseJSON1Test() throws Exception {

    }

    @Test
    public void doubleToLongTest() throws Exception {

    }

    @Test
    public void objectTest() throws Exception {

    }

}