package com.tailoredshapes.underbar;

import com.tailoredshapes.underbar.function.ExceptionalFunctions;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import static com.tailoredshapes.underbar.UnderBar.list;
import static java.util.stream.Collectors.joining;


public class Die {
    public static RuntimeException die(Throwable e, String msg, Object... args) {
        throw new RuntimeException(args.length == 0 ? msg : String.format(msg, args), e);
    }

    public static RuntimeException die(String msg, Object... args) {
        throw new RuntimeException(args.length == 0 ? msg : String.format(msg, args));
    }

    public static void dieIf(boolean condition, Supplier<String> message) {
        if (condition)
            throw die(message.get());
    }

    public static void dieUnless(boolean condition, Supplier<String> message) {
        dieIf(!condition, message);
    }

    public static <T> T dieIfNull(T t, Supplier<String> message) {
        if (t == null)
            throw die(message.get());
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

    private static <K, V> V dieIfMissing(Map<K, V> map, K key, Supplier<String> message, boolean checkValue) {
        V result = map.get(key);
        if (result != null)
            return result;
        if (!checkValue && map.containsKey(key))
            return null;
        String errorString = key + ". " + "Example available keys: [" +
                map.keySet().stream().limit(10).map(k -> String.format("%s = %s", k, UnderString.toString(map.get(k)))).collect(joining(", ")) + "] \n" + message.get();
        if (map.containsKey(key))
            throw die("Key present, but value is null: " + errorString);
        throw die("Key missing: " + errorString);
    }


    public static <K, V> V dieIfMissing(Map<K, V> map, K key, Supplier<String> message) {
        return dieIfMissing(map, key, message, true);
    }

    public static <K, V> V dieIfMissing(Map<K, V> map, K key) {
        return dieIfMissing(map, key, () -> "");
    }

    public static <K, V> V dieIfMissingKey(Map<K, V> map, K key) {
        return dieIfMissingKey(map, key, () -> "");
    }

    public static <K, V> V dieIfMissingKey(Map<K, V> map, K key, Supplier<String> message) {
        return dieIfMissing(map, key, message, false);
    }


    public static void rethrow(ExceptionalFunctions.RunnableWithOops runnable, Supplier<String> errorMessage) {
        try {
            runnable.run();
        } catch (Throwable e) {
            throw die(e, errorMessage.get());
        }
    }

    public static void rethrow(ExceptionalFunctions.RunnableThatMight runnable, Supplier<String> errorMessage) {
        try {
            runnable.run();
        } catch (Throwable e) {
            throw die(e, errorMessage.get());
        }
    }

    public static void rethrow(ExceptionalFunctions.RunnableWithOops runnable) {
        try {
            runnable.run();
        } catch (Throwable e) {
            throw die(e, "exception caught");
        }
    }

    public static void rethrow(ExceptionalFunctions.RunnableThatMight runnable) {
        try {
            runnable.run();
        } catch (Throwable e) {
            throw die(e, "exception caught");
        }
    }

    public static <T> T rethrow(ExceptionalFunctions.SupplierWithOops<T> supplier) {
        try {
            return supplier.get();
        } catch (RuntimeException re) {
            throw re;
        } catch (Throwable e) {
            throw die(e, "exception caught");
        }
    }

    public static <T, E extends Throwable> T rethrow(ExceptionalFunctions.SupplierThatMight<T, E> supplier) {
        try {
            return supplier.get();
        } catch (RuntimeException re) {
            throw re;
        } catch (Throwable e) {
            throw die(e, "exception caught");
        }
    }

    public static <T> T rethrow(ExceptionalFunctions.SupplierWithOops<T> supplier, Supplier<String> errorMessage) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            throw die(e, errorMessage.get());
        }
    }

    public static <T, E extends Throwable> T rethrow(ExceptionalFunctions.SupplierThatMight<T, E> supplier, Supplier<String> errorMessage) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            throw die(e, errorMessage.get());
        }
    }

    public static RuntimeException unimplemented() {
        return die("unimplemented");
    }

    public static RuntimeException unimplemented(String thing) {
        return die("unimplemented: " + thing);
    }
}
