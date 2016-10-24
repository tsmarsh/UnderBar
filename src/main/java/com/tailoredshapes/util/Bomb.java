package com.tailoredshapes.util;

import com.diffplug.common.base.*;

import java.util.*;
import java.util.function.*;

import static com.tailoredshapes.util.UnderBar.list;
import static java.util.stream.Collectors.*;


public class Bomb {
    public static RuntimeException bomb(Throwable e, String msg, Object ... args) {
        throw new RuntimeException(args.length == 0 ? msg : String.format(msg, args), e);
    }

    public static RuntimeException bomb(String msg, Object ... args) {
        throw new RuntimeException(args.length == 0 ? msg : String.format(msg, args));
    }

    public static void bombIf(boolean condition, Supplier<String> message) {
        if(condition)
            throw bomb(message.get());
    }

    public static void bombUnless(boolean condition, Supplier<String> message) {
        bombIf(!condition, message);
    }

    public static <T> T bombNull(T t, Supplier<String> message) {
        if(t == null)
            throw bomb(message.get());
        return t;
    }

    public static <T> Collection<T> bombIfEmpty(Collection<T> ts, Supplier<String> message) {
        bombIf(ts.isEmpty(), message);
        return ts;
    }

    public static <T> Collection<T> bombIfEmpty(T[] tarray, Supplier<String> message) {
        return bombIfEmpty(list(tarray), message);
    }

    public static <T> T bombNull(T t) {
        return bombNull(t, () -> "unexpected null");
    }

    public static void bombNotNull(Object o, Supplier<String> message) {
        bombUnless(o == null, message);
    }

    private static <K, V> V bombMissing(Map<K, V> map, K key, Supplier<String> message, boolean checkValue) {
        V result = map.get(key);
        if(result != null)
            return result;
        if (!checkValue && map.containsKey(key))
            return null;
        String errorString = key + ". " + "Example available keys: [" +
                map.keySet().stream().limit(10).map(k -> String.format("%s = %s", k, Strings.toString(map.get(k)))).collect(joining(", ")) + "] \n" + message.get();
        if(map.containsKey(key))
            throw bomb("Key present, but value is null: " + errorString);
        throw bomb("Key missing: " + errorString);
    }


    public static <K, V> V bombMissing(Map<K, V> map, K key, Supplier<String> message) {
        return bombMissing(map, key, message, true);
    }

    public static <K, V> V bombMissing(Map<K, V> map, K key) {
        return bombMissing(map, key, () -> "");
    }

    public static <K, V> V bombMissingKey(Map<K, V> map, K key) {
        return bombMissingKey(map, key, () -> "");
    }

    public static <K, V> V bombMissingKey(Map<K, V> map, K key, Supplier<String> message) {
        return bombMissing(map, key, message, false);
    }

    public static void rethrow(Throwing.Runnable runnable) {
        try {
            runnable.run();
        } catch (RuntimeException re) {
            throw re;
        } catch (Throwable e) {
            throw bomb(e, "exception caught");
        }
    }

    public static void rethrow(Throwing.Runnable runnable, Supplier<String> errorMessage) {
        try {
            runnable.run();
        } catch (Throwable e) {
            throw bomb(e, errorMessage.get());
        }
    }

    public static <T> T rethrow(Throwing.Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (RuntimeException re) {
            throw re;
        } catch (Throwable e) {
            throw bomb(e, "exception caught");
        }
    }

    public static <T> T rethrow(Throwing.Supplier<T> supplier, Supplier<String> errorMessage) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            throw bomb(e, errorMessage.get());
        }
    }

    public static RuntimeException unimplemented() {
        return bomb("unimplemented");
    }

    public static RuntimeException unimplemented(String thing) {
        return bomb("unimplemented: " + thing);
    }
}
