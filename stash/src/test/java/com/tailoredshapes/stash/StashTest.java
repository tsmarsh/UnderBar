package com.tailoredshapes.stash;

import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.tailoredshapes.stash.Stash.*;
import static com.tailoredshapes.underbar.Dates.date;
import static com.tailoredshapes.underbar.UnderBar.*;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.*;

public class StashTest {

    @Test
    public void parseJsonMapsTest() throws Exception {

    }

    @Test
    public void putTest() throws Exception {
        Stash smap = stash();
        smap.update("foo", "bar");

        assertEquals("bar", smap.get("foo"));
    }

    @Test
    public void sizeTest() throws Exception {
        Stash smap = stash();
        assertEquals(0, smap.size());
        smap.update("foo", "bar");
        assertEquals(1, smap.size());
    }

    @Test
    public void assocTest() throws Exception {
        assertEquals("bar", stash().assoc("foo", "bar").get("foo"));
    }

    @Test
    public void dissocTest() throws Exception {
        assertEquals(stash("foo", 1), stash("foo", 1, "bar", 2).dissoc("bar"));
    }

    @Test
    public void long_Test() throws Exception {
        Stash smap = stash("foo", 1l);

        assertEquals(1l, smap.l("foo"));
    }

    @Test
    public void double_Test() throws Exception {
        Stash smap = stash("foo", 1.1);

        assertEquals(1.1, smap.d("foo"), 0);
    }

    @Test
    public void integerTest() throws Exception {
        Stash smap = stash("foo", 1);

        assertEquals(1, smap.i("foo"));
    }

    @Test
    public void integerValueTest() throws Exception {
        Stash smap = stash("foo", 1.1);

        assertEquals(1, smap.asInteger("foo"), 0);
    }

    @Test
    public void parseIntegerTest() throws Exception {
        Stash smap = stash("foo", "5", "bar", null);

        assertEquals(5, smap.parseInteger("foo", 3));
        assertEquals(3, smap.parseInteger("bar", 3));
    }

    @Test
    public void parseLongTest() throws Exception {
        Stash smap = stash("foo", "5", "bar", null);

        assertEquals(5, smap.parseLong("foo", 3));
        assertEquals(3, smap.parseLong("bar", 3));
    }

    @Test
    public void integerMaybeTest() throws Exception {
        Stash smap = stash("foo", optional(1));

        assertEquals(optional(1), smap.optional("foo"));
    }


    @Test
    public void longValueTest() throws Exception {
        Stash smap = stash("foo", 1L);

        assertEquals(1L, smap.l("foo"));
    }


    @Test
    public void mapTest() throws Exception {
        Stash smap = stash("foo", 1L);

        assertEquals(list("foo: 1"), smap.map((k, v) -> String.format("%s: %s", k, v)));
    }

    @Test
    public void putIntoTest() throws Exception {
        Stash other = stash("eggs", "spam");
        assertEquals(stash("foo", "bar", "eggs", "spam"), stash("foo", "bar").putInto(other));
    }


    @Test
    public void hasTest() throws Exception {
        assertTrue(stash("eggs", "spam").contains("eggs"));
        assertFalse(stash("eggs", "spam").contains("ham"));
    }

    @Test
    public void hasValueTest() throws Exception {
        assertTrue(stash("eggs", optional("spam")).containsValue("eggs"));
        assertFalse(stash("eggs", optional()).containsValue("eggs"));
    }

    @Test
    public void smapTest() throws Exception {
        assertEquals(stash("eggs", "ham"), stash("foo", stash("eggs", "ham")).get("foo"));
    }

    @Test
    public void makeSmapTest() throws Exception {
        assertEquals(stash("foo", 3), stash("eggs", hash("foo", 3)).asStash("eggs"));

    }


    @Test
    public void makeSmapsTest() throws Exception {
        assertEquals(list(stash("eggs", "ham")), stash("foo", list(hash("eggs", "ham"))).asStashes("foo"));
    }

    @Test
    public void parseJsonTest() throws Exception {
        assertEquals(stash("foo", 5), stash("eggs", "{\"foo\": 5}").parseJson("eggs"));
    }

    @Test
    public void toJSONStringTest() throws Exception {
        assertEquals("{\"eggs\":4}", stash("eggs", 4).toJSONString());
    }


    @Test
    public void filterKeysTest() throws Exception {
        assertEquals(
                stash("foo", 1, "eggs", 3),
                stash("foo", 1, "bar", 2, "eggs", 3)
                        .filterKeys(negate((k) -> k.equals("bar"))));
    }

    @Test
    public void rejectKeysTest() throws Exception {
        assertEquals(
                stash("foo", 1, "eggs", 3),
                stash("foo", 1, "bar", 2, "eggs", 3)
                        .rejectKeys("bar"));

        assertEquals(
                stash("foo", 1),
                stash("foo", 1, "bar", 2, "eggs", 3)
                        .rejectKeys("bar", "eggs"));

        assertEquals(
                stash("foo", 1),
                stash("foo", 1, "bar", 2, "eggs", 3)
                        .rejectKeys(list("bar", "eggs")));
    }

    @Test
    public void selectKeysTest() throws Exception {
        assertEquals(
                stash("bar", 2),
                stash("foo", 1, "bar", 2, "eggs", 3)
                        .selectKeys("bar"));

        assertEquals(
                stash("bar", 2, "eggs", 3),
                stash("foo", 1, "bar", 2, "eggs", 3)
                        .selectKeys("bar", "eggs"));

        assertEquals(
                stash("bar", 2, "eggs", 3),
                stash("foo", 1, "bar", 2, "eggs", 3)
                        .selectKeys(list("bar", "eggs")));
    }

    @Test
    public void toMapTest() throws Exception {
        assertEquals(hash("bar", 2), stash("bar", 2).toMap());
    }


    @Test
    public void removeTest() throws Exception {
        assertEquals(
                2,
                (int) stash("foo", 1, "bar", 2, "eggs", 3)
                        .remove("bar"));
    }

    @Test
    public void parseDateTest() throws Exception {
        ZonedDateTime now = ZonedDateTime.now();

        String date = now.format(DateTimeFormatter.ISO_INSTANT);

        assertEquals(date(date), stash("eggs", date).parseDate("eggs"));
    }

    @Test
    public void hasContentTest() throws Exception {
        assertFalse(stash("foo", "  ").hasContent("foo"));
        assertTrue(stash("foo", "FOO").hasContent("foo"));
    }

    @Test
    public void optionalTest() throws Exception {
        assertEquals(
                optional("foo"),
                stash("bar", optional("foo")).optional("bar"));
    }

    @Test
    public void maybeTest() throws Exception {
        assertEquals(optional(), stash().maybe("foo"));
        assertEquals(optional("bar"), stash("foo", "bar").maybe("foo"));
        assertEquals(optional("bar"), stash("foo", optional("bar")).maybe("foo"));
    }


    @Test
    public void mergeTest() throws Exception {
        assertEquals(
                stash("foo", 1, "bar", 2),
                stash("foo", 1).merge(stash("bar", 2)));
        assertEquals(
                stash("foo", 3, "bar", 2),
                stash("foo", 1).merge(stash("bar", 2, "foo", 3)));
    }


    @Test
    public void asStringTest() throws Exception {
        assertEquals("1", stash("foo", 1).asString("foo"));
    }


    @Test
    public void parseUUIDTest() throws Exception {
        UUID v1 = randomUUID();
        assertEquals(v1, stash("foo", v1.toString()).parseUUID("foo"));
    }

    @Test
    public void numberTest() throws Exception {
        assertEquals(1, stash("foo", 1).number("foo"));
        assertEquals(1L, stash("foo", 1L).number("foo"));
    }

    @Test
    public void asUrlTest() throws Exception {
        assertEquals("?arg3=bar&arg1=foo", stash("arg1", "foo", "arg3", "bar").toUrlParameters());
    }

    @Test
    public void isEmptyTest() throws Exception {
        assertFalse(stash("foo", 1).isEmpty());
        assertTrue(stash().isEmpty());
        assertFalse(stash("foo", "bar").isEmpty("foo")); //Suspect, why not just use hasContent?
        assertTrue(stash("foo", " ").isEmpty("foo"));
    }


    @Test
    public void parseDoubleTest() throws Exception {
        assertEquals(2.2, stash("foo", "2.2").parseDouble("foo", 0), 0);
        assertEquals(0, stash("foo", null).parseDouble("foo", 0), 0);
    }

    @Test
    public void parseBooleanTest() throws Exception {
        assertTrue(stash("foo", "True").parseBoolean("foo", false));
        assertFalse(stash("foo", "f").parseBoolean("foo", true));
        assertTrue(stash("foo", null).parseBoolean("foo", true));
    }

    @Test
    public void isNullTest() throws Exception {
        assertTrue(stash("foo", null).isNull("foo"));
        assertFalse(stash("foo", "bar").isNull("foo"));
    }

    @Test
    public void cloneTest() throws Exception {
        assertEquals(
                stash("foo", 1, "bar", 3, "eggs", stash("foo", 2)),
                stash("foo", 1, "bar", 3, "eggs", stash("foo", 2)).clone());
    }


    @Test
    public void keysTest() throws Exception {
        assertEquals(
                list("bar", "foo"),
                sort(stash("foo", 1, "bar", 2).keys()));
    }

    @Test
    public void typeTest() throws Exception {
        assertEquals(String.class, stash("foo", "bar").type("foo"));
    }


    @Test
    public void parseIsoDateTimeTest() throws Exception {
        assertEquals(date("2016-10-10T10:10:10.400Z"), stash("foo", "2016-10-10T10:10:10.400Z").parseDate("foo"));
    }


    @Test
    public void smapFromPairsCreatesAStringMapFromPairsWithAFunction() throws Exception {
        assertEquals(stash("a", 1, "b", 2),
                stashFromPairs(list(list("a", 1), list("b", 2)),
                        (l) -> entry((String) l.get(0), l.get(1))));
    }

    @Test
    public void smapFromKeysCreatesAStingMapFromKeysAndAFunction() throws Exception {
        assertEquals(stash("a", 1, "b", 1),
                stashFromKeys(list("a", "b"),
                        (l) -> 1));
    }


    @Test
    public void superStringMapTest() throws Exception {
        Stash map = stash(
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

    @Test
    public void ifLetTest() throws Exception {
        assertEquals(5, (int)stash("a", 3).ifLet("a", k->5, ()->0));
        assertEquals(0, (int)stash("b", 3).ifLet("a", k->5, ()->0));

        assertEquals(5, (int)stash("b", stash("a", 3))
                .ifLet("b",
                        (Stash b)-> b.ifLetPun("a", (v) -> 5),
                        ()->0));

        assertNull(stash("b", stash("a", 3))
                .ifLet("b",
                        (Stash b)-> b.ifLetPun("b", (v) -> 5), ()->0));

        assertEquals(optional(5), stash("a", 3).ifLet("a", k->5));
        assertEquals(optional(), stash("b", 3).ifLet("a", k->5));
    }
}