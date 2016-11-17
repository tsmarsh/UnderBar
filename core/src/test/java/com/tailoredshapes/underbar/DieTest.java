package com.tailoredshapes.underbar;

import com.tailoredshapes.underbar.exceptions.UnderBarred;
import com.tailoredshapes.underbar.function.ExceptionalFunctions;
import org.junit.Test;

import static com.tailoredshapes.underbar.Die.*;
import static com.tailoredshapes.underbar.UnderBar.hash;
import static com.tailoredshapes.underbar.UnderBar.list;
import static com.tailoredshapes.underbar.UnderBar.map;
import static org.junit.Assert.assertEquals;


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
        dieIfMissing(hash(), "foo", () -> "nope");
    }

    @Test
    public void dieIfMissingTest2() throws Exception {
        assertEquals("bar", dieIfMissing(UnderBar.hash("foo", "bar"), "foo", () -> "nope"));
    }

    @Test(expected = RuntimeException.class)
    public void rethrowTest() throws Exception {
        rethrow(() -> {
            throw die("Nope");
        });
    }

    @Test(expected = RuntimeException.class)
    public void rethrowWithMessage() throws Exception {
        rethrow(() -> {
            throw new Exception();
        }, () -> "with a message");
    }

    @Test(expected = RuntimeException.class)
    public void rethrowWithRunnable() throws Exception {
        rethrow(new ExceptionalFunctions.RunnableWithOops() {
            @Override
            public void run() throws Throwable {
                throw die("derp");
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void rethrowWithRunnableWithMessage() throws Exception {
        rethrow(new ExceptionalFunctions.RunnableWithOops() {
            @Override
            public void run() throws Throwable {
                throw die("derp");
            }
        }, () -> "with message");
    }

    @Test(expected = RuntimeException.class)
    public void rethrowWithRunnableThatMightWithMessage() throws Exception {
        rethrow(new ExceptionalFunctions.RunnableThatMight<IllegalArgumentException>() {
            @Override
            public void run() throws IllegalArgumentException {
                throw new IllegalArgumentException();
            }
        }, () -> "with message");
    }

    @Test(expected = RuntimeException.class)
    public void rethrowWithRunnableThatMight() throws Exception {
        rethrow(new ExceptionalFunctions.RunnableThatMight<IllegalArgumentException>() {
            @Override
            public void run() throws IllegalArgumentException {
                throw new IllegalArgumentException();
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void rethrowWithSupplierThatMightWithMessage() throws Exception {
        rethrow(new ExceptionalFunctions.SupplierThatMight<String, IllegalArgumentException>() {
            @Override
            public String get() throws IllegalArgumentException {
                throw new IllegalArgumentException();
            }
        }, () -> "with message");
    }

    @Test(expected = RuntimeException.class)
    public void rethrowWithSupplierThatMight() throws Exception {
        rethrow(new ExceptionalFunctions.SupplierThatMight<String, IllegalArgumentException>() {
            @Override
            public String get() throws IllegalArgumentException {
                throw new IllegalArgumentException();
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void unimplementedTest() throws Exception {
        unimplemented();
    }

    @Test(expected = RuntimeException.class)
    public void unimplementedWithMessageTest() throws Exception {
        unimplemented("because it was hard");
    }
}