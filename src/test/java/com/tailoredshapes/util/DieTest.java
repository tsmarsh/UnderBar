package com.tailoredshapes.util;

import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static com.tailoredshapes.util.Die.*;
import static com.tailoredshapes.util.UnderBar.*;
import static org.junit.Assert.*;


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
        bombIfEmpty(list(), () -> "foo");
    }

    @Test
    public void bombIfEmptyTest2() throws Exception {
        bombIfEmpty(list(1), () -> "foo");
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
        dieIfMissing(map(), "foo", () -> "nope");
    }

    @Test
    public void dieIfMissingTest2() throws Exception {
        dieIfMissing(map("foo", "bar"), "foo", () -> "nope");
    }

    @Test(expected = RuntimeException.class)
    public void rethrowTest() throws Exception {
        rethrow(() -> {throw new Exception();});
    }


    @Test(expected = RuntimeException.class)
    public void unimplementedTest() throws Exception {
        unimplemented();
    }

}