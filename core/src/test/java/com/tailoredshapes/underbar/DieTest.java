package com.tailoredshapes.underbar;

import com.tailoredshapes.underbar.exceptions.UnderBarred;
import com.tailoredshapes.underbar.function.ExceptionalFunctions;
import org.junit.Test;

import static com.tailoredshapes.underbar.Die.*;
import static com.tailoredshapes.underbar.UnderBar.*;
import static org.junit.Assert.assertEquals;


public class DieTest {
    @Test(expected = UnderBarred.class)
    public void dieTest() throws Exception {
        die(new RuntimeException(), "foop");
        die("foop");
    }


    @Test(expected = UnderBarred.class)
    public void dieIfTest() throws Exception {
        dieIf(true, () -> "foo");
    }

    @Test(expected = UnderBarred.class)
    public void dieUnlessTest() throws Exception {
        dieUnless(false, () -> "foo");
    }

    @Test
    public void dieUnlessTest2() throws Exception {
        dieUnless(true, () -> "foo");
    }

    @Test(expected = UnderBarred.class)
    public void dieIfNullTest() throws Exception {
        dieIfNull(null);
    }

    @Test
    public void dieIfNullTest2() throws Exception {
        dieIfNull("foo");
    }

    @Test(expected = UnderBarred.class)
    public void bombIfEmptyTest() throws Exception {
        dieIfEmpty(list(), () -> "foo");
    }

    @Test
    public void bombIfEmptyTest2() throws Exception {
        dieIfEmpty(list(1), () -> "foo");
    }


    @Test(expected = UnderBarred.class)
    public void dieIfNotNullTest() throws Exception {
        dieIfNotNull("foo", () -> "foo");
    }

    @Test
    public void dieIfNotNullTest2() throws Exception {
        dieIfNotNull(null, () -> "foo");
    }


    @Test(expected = UnderBarred.class)
    public void dieIfMissingTest() throws Exception {
        dieIfMissing(hash(), "foo", () -> "nope");
    }

    @Test
    public void dieIfMissingTest2() throws Exception {
        assertEquals("bar", dieIfMissing(UnderBar.hash("foo", "bar"), "foo", () -> "nope"));
    }

    @Test(expected = UnderBarred.class)
    public void rethrowTest() throws Exception {
        rethrow(() -> {
            die("Nope");
        });
    }

    @Test(expected = UnderBarred.class)
    public void rethrowWithMessage() throws Exception {
        rethrow(() -> {
            throw new Exception();
        }, () -> "with a message");
    }

    @Test(expected = UnderBarred.class)
    public void rethrowWithRunnable() throws Exception {
        rethrow(new ExceptionalFunctions.RunnableWithOops() {
            @Override
            public void run() throws Throwable {
                die("derp");
            }
        });
    }

    @Test(expected = UnderBarred.class)
    public void rethrowWithRunnableWithMessage() throws Exception {
        rethrow((ExceptionalFunctions.RunnableWithOops) () -> die("derp"), () -> "with message");
    }

    @Test(expected = UnderBarred.class)
    public void rethrowWithRunnableThatMightWithMessage() throws Exception {
        rethrow(new ExceptionalFunctions.RunnableThatMight<IllegalArgumentException>() {
            @Override
            public void run() throws IllegalArgumentException {
                throw new IllegalArgumentException();
            }
        }, () -> "with message");
    }

    @Test(expected = UnderBarred.class)
    public void rethrowWithRunnableThatMight() throws Exception {
        rethrow(new ExceptionalFunctions.RunnableThatMight<IllegalArgumentException>() {
            @Override
            public void run() throws IllegalArgumentException {
                throw new IllegalArgumentException();
            }
        });
    }

    @Test(expected = UnderBarred.class)
    public void rethrowWithSupplierThatMightWithMessage() throws Exception {
        rethrow(new ExceptionalFunctions.SupplierThatMight<String, IllegalArgumentException>() {
            @Override
            public String get() throws IllegalArgumentException {
                throw new IllegalArgumentException();
            }
        }, () -> "with message");
    }

    @Test(expected = UnderBarred.class)
    public void rethrowWithSupplierThatMight() throws Exception {
        rethrow(new ExceptionalFunctions.SupplierThatMight<String, IllegalArgumentException>() {
            @Override
            public String get() throws IllegalArgumentException {
                throw new IllegalArgumentException();
            }
        });
    }

    @Test(expected = UnderBarred.class)
    public void workInProgressTest() throws Exception {
        wip();
    }

    @Test(expected = UnderBarred.class)
    public void workInProgressWithExcuseTest() throws Exception {
        wip("because it was hard");
    }

    @Test(expected = UnderBarred.class)
    public void shouldDieIfEmpty() throws Exception {
        dieIfEmpty(array(), () -> "Nope");
    }
}