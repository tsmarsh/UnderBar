package com.tailoredshapes.underbar.ocho;

import com.tailoredshapes.underbar.ocho.exceptions.UnderBarred;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import static com.tailoredshapes.underbar.ocho.UnderBar.list;
import static com.tailoredshapes.underbar.ocho.function.ExceptionalFunctions.*;

public class Die {
    public static <T> T die(Throwable e, String msg, Object... args) {
        throw new UnderBarred(args.length == 0 ? msg : String.format(msg, args), e);
    }

    public static <T> T die(String msg, Object... args) {
        throw new UnderBarred(args.length == 0 ? msg : String.format(msg, args));
    }

    public static void dieIf(boolean condition, Supplier<String> message) {
        if (condition) {
            die(message.get());
        }
    }

    public static void dieUnless(boolean condition, Supplier<String> message) {
        dieIf(!condition, message);
    }

    public static <T> T dieIfNull(T t, Supplier<String> message) {
        if (t == null) {
            die(message.get());
        }
        return t;
    }

    public static <T> Collection<T> dieIfEmpty(Collection<T> ts, Supplier<String> message) {
        dieIf(ts.isEmpty(), message);
        return ts;
    }

    public static <T> Collection<T> dieIfEmpty(T[] tarray, Supplier<String> message) {
        return dieIfEmpty(list(tarray), message);
    }

    public static <T> T dieIfNull(T t) {
        return dieIfNull(t, () -> "unexpected null");
    }

    public static void dieIfNotNull(Object o, Supplier<String> message) {
        dieUnless(o == null, message);
    }

    public static <K, V> V dieIfMissing(Map<K, V> map, K key, Supplier<String> message) {
        V result = map.get(key);
        if (result != null)
            return result;
        if (map.containsKey(key)) {
            die("Key present, but value is null. " + message.get());
        }
        return die("Key missing: " + message.get());
    }

    public static <K, V> V dieIfMissing(Map<K, V> map, K key) {
        return dieIfMissing(map, key, () -> "");
    }


    public static void rethrow(RunnableWithOops runnable, Supplier<String> errorMessage) {
        try {
            runnable.run();
        } catch (Throwable e) {
            die(e, errorMessage.get());
        }
    }

    public static void rethrow(RunnableThatMight runnable, Supplier<String> errorMessage) {
        try {
            runnable.run();
        } catch (Throwable e) {
            die(e, errorMessage.get());
        }
    }

    public static void rethrow(RunnableWithOops runnable) {
        try {
            runnable.run();
        } catch (Throwable e) {
            die(e, "exception caught");
        }
    }

    public static void rethrow(RunnableThatMight runnable) {
        try {
            runnable.run();
        } catch (Throwable e) {
            die(e, "exception caught");
        }
    }

    public static <T> T rethrow(SupplierWithOops<T> supplier) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            return die(e, "exception caught");
        }
    }

    public static <T, E extends Throwable> T rethrow(SupplierThatMight<T, E> supplier) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            return die(e, "exception caught");
        }
    }

    public static <T> T rethrow(SupplierWithOops<T> supplier, Supplier<String> errorMessage) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            return die(e, errorMessage.get());
        }
    }

    public static <T, E extends Throwable> T rethrow(SupplierThatMight<T, E> supplier, Supplier<String> errorMessage) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            return die(e, errorMessage.get());
        }
    }

    public static<T> T wip() {
        return die("wip");
    }

    public static <T> T wip(String reason) {
        return die("wip: " + reason);
    }
}
