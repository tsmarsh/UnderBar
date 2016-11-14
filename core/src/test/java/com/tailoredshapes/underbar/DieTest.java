package com.tailoredshapes.underbar;

import org.junit.Test;

import static com.tailoredshapes.underbar.Die.*;
import static com.tailoredshapes.underbar.UnderBar.list;
import static com.tailoredshapes.underbar.UnderBar.map;


public class DieTest {
    @Test(expected = RuntimeException.class)
    public void dieTest() throws Exception {
        die(new RuntimeException(), "foop");
        die("foop");
    }


    @Test(expected = RuntimeException.class)
    public void dieIfTest() throws Exception {
        dieIf(true, () -> "foo");
    }

    @Test(expected = RuntimeException.class)
    public void dieUnlessTest() throws Exception {
        dieUnless(false, () -> "foo");
    }

    @Test
    public void dieUnlessTest2() throws Exception {
        dieUnless(true, () -> "foo");
    }

    @Test(expected = RuntimeException.class)
    public void dieIfNullTest() throws Exception {
        dieIfNull(null);
    }

    @Test
    public void dieIfNullTest2() throws Exception {
        dieIfNull("foo");
    }

    @Test(expected = RuntimeException.class)
    public void bombIfEmptyTest() throws Exception {
        dieIfEmpty(list(), () -> "foo");
    }

    @Test
    public void bombIfEmptyTest2() throws Exception {
        dieIfEmpty(list(1), () -> "foo");
    }


    @Test(expected = RuntimeException.class)
    public void dieIfNotNullTest() throws Exception {
        dieIfNotNull("foo", () -> "foo");
    }

    @Test
    public void dieIfNotNullTest2() throws Exception {
        dieIfNotNull(null, () -> "foo");
    }


    @Test(expected = RuntimeException.class)
    public void dieIfMissingTest() throws Exception {
        dieIfMissing(UnderBar.hash(), "foo", () -> "nope");
    }

    @Test
    public void dieIfMissingTest2() throws Exception {
        dieIfMissing(map("foo", "bar"), "foo", () -> "nope");
    }

    @Test(expected = RuntimeException.class)
    public void rethrowTest() throws Exception {
        rethrow(() -> {
            throw new Exception();
        });
    }


    @Test(expected = RuntimeException.class)
    public void unimplementedTest() throws Exception {
        unimplemented();
    }

}