package com.tailoredshapes.stringmap;

import org.json.simple.JSONObject;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.UUID;

import static com.tailoredshapes.stringmap.StringMap.*;
import static com.tailoredshapes.underbar.Dates.date;
import static com.tailoredshapes.underbar.UnderBar.*;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.*;

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

        assertEquals(list("foo: 1"), smap.map((k, v) -> String.format("%s: %s", k, v)));
    }

    @Test
    public void putIntoTest() throws Exception {
        StringMap other = smap("eggs", "spam");
        assertEquals(smap("foo", "bar", "eggs", "spam"), smap("foo", "bar").putInto(other));
    }


    @Test
    public void hasTest() throws Exception {
        assertTrue(smap("eggs", "spam").has("eggs"));
        assertFalse(smap("eggs", "spam").has("ham"));
    }

    @Test
    public void hasValueTest() throws Exception {
        assertTrue(smap("eggs", optional("spam")).hasValue("eggs"));
        assertFalse(smap("eggs", optional()).hasValue("eggs"));
    }

    @Test
    public void boolTest() throws Exception {
        assertTrue(smap("eggs", true).bool("eggs"));
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
        assertEquals(smap("foo", 5), smap("eggs", "{\"foo\": 5}").parseJson("eggs"));
    }

    @Test
    public void toJSONStringTest() throws Exception {
        assertEquals("{\"eggs\":4}", smap("eggs", 4).toJSONString());
    }


    @Test
    public void filterKeysTest() throws Exception {
        assertEquals(
                smap("foo", 1, "eggs", 3),
                smap("foo", 1, "bar", 2, "eggs", 3)
                        .filterKeys(negate((k) -> k.equals("bar"))));
    }

    @Test
    public void rejectKeysTest() throws Exception {
        assertEquals(
                smap("foo", 1, "eggs", 3),
                smap("foo", 1, "bar", 2, "eggs", 3)
                        .rejectKeys("bar"));

        assertEquals(
                smap("foo", 1),
                smap("foo", 1, "bar", 2, "eggs", 3)
                        .rejectKeys("bar", "eggs"));

        assertEquals(
                smap("foo", 1),
                smap("foo", 1, "bar", 2, "eggs", 3)
                        .rejectKeys(list("bar", "eggs")));
    }

    @Test
    public void selectKeysTest() throws Exception {
        assertEquals(
                smap("bar", 2),
                smap("foo", 1, "bar", 2, "eggs", 3)
                        .selectKeys("bar"));

        assertEquals(
                smap("bar", 2, "eggs", 3),
                smap("foo", 1, "bar", 2, "eggs", 3)
                        .selectKeys("bar", "eggs"));

        assertEquals(
                smap("bar", 2, "eggs", 3),
                smap("foo", 1, "bar", 2, "eggs", 3)
                        .selectKeys(list("bar", "eggs")));
    }

    @Test
    public void toMapTest() throws Exception {
        assertEquals(map("bar", 2), smap("bar", 2).toMap());
    }


    @Test
    public void removeTest() throws Exception {
        assertEquals(
                2,
                (int) smap("foo", 1, "bar", 2, "eggs", 3)
                        .remove("bar"));
    }

    @Test
    public void parseDateTest() throws Exception {
        assertEquals(date("1979-10-10"), smap("eggs", "1979-10-10").parseDate("eggs"));
    }

    @Test
    public void hasContentTest() throws Exception {
        assertFalse(smap("foo", "  ").hasContent("foo"));
        assertTrue(smap("foo", "FOO").hasContent("foo"));
    }

    @Test
    public void stringOptionalTest() throws Exception {
        assertEquals(
                optional("foo"),
                smap("bar", "foo").stringOptional("bar"));
    }

    @Test
    public void stringishTest() throws Exception {
        assertEquals(optional(), smap().stringish("foo"));
        assertEquals(optional("bar"), smap("foo", "bar").stringish("foo"));
        assertEquals(optional("bar"), smap("foo", optional("bar")).stringish("foo"));
    }

    @Test
    public void longishTest() throws Exception {
        assertEquals(optional(), smap().longish("foo"));
        assertEquals(optional(1L), smap("foo", 1L).longish("foo"));
        assertEquals(optional(1L), smap("foo", optional(1L)).longish("foo"));
    }

    @Test
    public void dateishTest() throws Exception {
        assertEquals(optional(), smap().dateish("foo"));
        assertEquals(optional(date("1979-10-10")), smap("foo", date("1979-10-10")).dateish("foo"));
        assertEquals(optional(date("1979-10-10")), smap("foo", optional(date("1979-10-10"))).dateish("foo"));
    }

    @Test
    public void doublishTest() throws Exception {
        assertEquals(optional(), smap().doublish("foo"));
        assertEquals(optional(1.0), smap("foo", 1.0).doublish("foo"));
    }

    @Test
    public void boolishTest() throws Exception {
        assertEquals(optional(), smap().longish("foo"));
        assertEquals(optional(true), smap("foo", true).boolish("foo"));
    }

    @Test
    public void booleanOptionalTest() throws Exception {
        assertEquals(optional(true), smap("foo", true).booleanOptional("foo"));
        assertEquals(optional(), smap("foo", null).booleanOptional("foo"));
    }

    @Test
    public void mergeTest() throws Exception {
        assertEquals(
                smap("foo", 1, "bar", 2),
                smap("foo", 1).merge(smap("bar", 2)));
        assertEquals(
                smap("foo", 3, "bar", 2),
                smap("foo", 1).merge(smap("bar", 2, "foo", 3)));
    }


    @Test
    public void asStringTest() throws Exception {
        assertEquals("1", smap("foo", 1).asString("foo"));
    }

    @Test
    public void uuidTest() throws Exception {
        UUID v1 = randomUUID();
        assertEquals(v1, smap("foo", v1).uuid("foo"));
    }

    @Test
    public void parseUUIDTest() throws Exception {
        UUID v1 = randomUUID();
        assertEquals(v1, smap("foo", v1.toString()).parseUUID("foo"));
    }

    @Test
    public void parseUUIDMaybeTest() throws Exception {
        UUID v1 = randomUUID();
        assertEquals(optional(v1), smap("foo", v1.toString()).parseUUIDMaybe("foo"));
        assertEquals(optional(), smap("foo", null).parseUUIDMaybe("foo"));

    }

    @Test
    public void numberTest() throws Exception {
        assertEquals(1, smap("foo", 1).number("foo"));
        assertEquals(1L, smap("foo", 1L).number("foo"));
    }

    @Test
    public void asUrlTest() throws Exception {
        assertEquals("?arg3=bar&arg1=foo", smap("arg1", "foo", "arg3", "bar").asUrl());
    }

    @Test
    public void isEmptyTest() throws Exception {
        assertFalse(smap("foo", 1).isEmpty());
        assertTrue(smap().isEmpty());
        assertFalse(smap("foo", "bar").isEmpty("foo")); //Suspect, why not just use hasContent?
        assertTrue(smap("foo", " ").isEmpty("foo"));
    }


    @Test
    public void parseDoubleTest() throws Exception {
        assertEquals(2.2, smap("foo", "2.2").parseDouble("foo", 0), 0);
        assertEquals(0, smap("foo", null).parseDouble("foo", 0), 0);
    }

    @Test
    public void parseBooleanTest() throws Exception {
        assertTrue(smap("foo", "True").parseBoolean("foo", false));
        assertFalse(smap("foo", "f").parseBoolean("foo", true));
        assertTrue(smap("foo", null).parseBoolean("foo", true));
    }

    @Test
    public void isNullTest() throws Exception {
        assertTrue(smap("foo", null).isNull("foo"));
        assertFalse(smap("foo", "bar").isNull("foo"));
    }

    @Test
    public void jsonObjectTest() throws Exception {
        JSONObject jsonObject = new JSONObject();

        assertEquals(jsonObject, smap("foo", jsonObject).jsonObject("foo"));
    }

    @Test
    public void getListTest() throws Exception {
        assertEquals(list(), smap("foo", list()).getList("foo"));
    }

    @Test
    public void shallowCopyTest() throws Exception {
        assertEquals(
                smap("foo", 1, "bar", 3, "eggs", smap("foo", 2)),
                smap("foo", 1, "bar", 3, "eggs", smap("foo", 2)).shallowCopy());
    }

    @Test
    public void parseDateMaybeTest() throws Exception {
        assertEquals(
                optional(date("1979-10-10")),
                smap("foo", date("1979-10-10")).parseDateMaybe("foo"));
        assertEquals(
                optional(),
                smap("foo", null).parseDateMaybe("foo"));
    }

    @Test
    public void keysTest() throws Exception {
        assertEquals(
                list("bar", "foo"),
                sort(smap("foo", 1, "bar", 2).keys()));
    }

    @Test
    public void typeTest() throws Exception {
        assertEquals(String.class, smap("foo", "bar").type("foo"));
    }

    @Test
    public void uuidMaybeTest() throws Exception {
        UUID uuid = randomUUID();
        assertEquals(optional(uuid), smap("foo", optional(uuid)).uuidMaybe("foo"));
        assertEquals(null, smap("foo", null).uuidMaybe("foo"));
    }

    @Test
    public void parseIsoDateTimeTest() throws Exception {
        assertEquals(date("2016-10-10T10:10:10.400Z"), smap("foo", "2016-10-10T10:10:10.400Z").parseIsoDateTime("foo"));
    }

    @Test
    public void pathTest() throws Exception {
        assertEquals(Paths.get("/"), smap("foo", "/").path("foo"));
    }


    @Test
    public void doubleToLongTest() throws Exception {
        assertEquals(100L, smap("foo", 100.3).doubleToLong("foo"));
    }

    @Test
    public void objectTest() throws Exception {
        Object v1 = new Object();
        assertEquals(v1, smap("foo", v1).object("foo"));
    }

    @Test
    public void smapFromPairsCreatesAStringMapFromPairsWithAFunction() throws Exception {
        assertEquals(smap("a", 1, "b", 2),
                smapFromPairs(list(list("a", 1), list("b", 2)),
                        (l) -> entry((String) l.get(0), l.get(1))));
    }

    @Test
    public void smapFromKeysCreatesAStingMapFromKeysAndAFunction() throws Exception {
        assertEquals(smap("a", 1, "b", 1),
                smapFromKeys(list("a", "b"),
                        (l) -> 1));
    }


    @Test
    public void superStringMapTest() throws Exception {
        StringMap map = smap(
                "a", 1,
                "b", 2,
                "c", 3,
                "d", 4,
                "e", 5,
                "f", 6,
                "g", 7,
                "h", 8,
                "i", 9,
                "j", 10,
                "k", 11,
                "l", 12,
                "m", 13,
                "n", 14,
                "o", 15,
                "p", 16,
                "q", 17,
                "r", 18,
                "s", 19,
                "t", 20,
                "u", 21);
        assertEquals(21, map.size());
    }
}