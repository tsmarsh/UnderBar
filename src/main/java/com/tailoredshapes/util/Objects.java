package com.tailoredshapes.util;

import com.tailoredshapes.util.function.*;
import org.json.simple.JSONArray;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.google.common.collect.Iterables.isEmpty;
import static com.tailoredshapes.util.Bomb.*;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;

public class Objects {
    public static final Random random = new Random();

    public static <T> T the(Iterable<T> ts) {
        Iterator<T> i = ts.iterator();
        bombUnless(i.hasNext(), () -> "input to 'the' cannot be empty");
        T result = i.next();
        bombIf(i.hasNext(), () -> "input to 'the' has length > 1: " + ts);
        return result;
    }

    public static <T> T the(T[] ts) {
        bombNull(ts, () -> "input to 'the' cannot be null");
        bombUnless(ts.length == 1, () -> "length of input to 'the' must be 1. " + Strings.commaSep(list(ts)));
        return ts[0];
    }

    public static <T> Optional<T> maybe(Iterable<T> ts) {
        Iterator<T> i = ts.iterator();
        if (!i.hasNext())
            return Optional.empty();
        T result = i.next();
        bombIf(i.hasNext(), () -> "input to 'the' has length > 1: " + ts);
        return Optional.of(result);
    }

    public static <T> void ifAbsent(Optional<T> maybe, Runnable noT) {
        if (!maybe.isPresent())
            noT.run();
    }

    public static <T> void optionally(Optional<T> maybe, Consumer<T> onT, Runnable noT) {
        maybe.ifPresent(t -> onT.accept(t));
        if (!maybe.isPresent())
            noT.run();
    }

    public static <T, R> R maybeNull(T maybeNull, Function<T, R> onT, Supplier<R> noT) {
        return optionally_(ofNullable(maybeNull), onT, noT);
    }

    public static <T, R> R optionally_(Optional<T> maybe, Function<T, R> onT, Supplier<R> noT) {
        One<R> one = one(null);
        maybe.ifPresent(t -> one.value = onT.apply(t));
        if (!maybe.isPresent())
            one.value = noT.get();
        return one.value;
    }

    public static <K, V, R> R optionallyGet_(Map<K, V> maybe, K k, Function<V, R> onT, Supplier<R> noT) {
        return maybeNull(maybe.get(k), onT, noT);
    }

    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }

    @SafeVarargs
    public static <T> List<T> list(T... ts) {
        return Arrays.asList(ts);
    }

    @SafeVarargs
    public static <T> List<T> list_(T... ts) {
        return Arrays.asList(ts);
    }

    public static <T> List<T> list(Iterable<T> ts) {
        ArrayList<T> result = new ArrayList<>();
        ts.forEach(result::add);
        return result;
    }

    @SafeVarargs
    public static <T> List<T> modifiableList(T... ts) {
        List<T> result = new ArrayList<>();
        result.addAll(list(ts));
        return result;
    }

    @SafeVarargs
    public static <T> Set<T> set(T... ts) {
        return new HashSet<>(Arrays.asList(ts));
    }

    public static <T> Set<T> set(Iterable<T> ts) {
        HashSet<T> result = new HashSet<>();
        ts.forEach(result::add);
        return result;
    }

    public static <K, V, S extends Comparable<S>> List<Map.Entry<K, V>> sortBy(Map<K, V> kv, BiFunction<K, V, S> comparator) {
        return sortBy(kv.entrySet(), entry -> comparator.apply(entry.getKey(), entry.getValue()));
    }

    public static class One<T> {
        public T value;

        public One(T value) {
            this.value = value;
        }
    }

    // stack allocation of heap pointer to "value" so that you can assign to value in lambdas
    // to avoid the (can't use non-final values in lambda) error.
    public static <T> One<T> one(T t) {
        return new One<>(t);
    }

    @SafeVarargs
    public static <T> T[] array(T... ts) {
        return ts;
    }

    public static <T> T firstExample(Iterable<T> ts) {
        Iterator<T> iterator = ts.iterator();
        bombUnless(iterator.hasNext(), () -> "can't take first of empty iterable");
        return iterator.next();
    }

    public static <T> T last(List<T> ts) {
        bombIf(isEmpty(ts), () -> "can't take last of empty list!");
        return ts.get(ts.size() - 1);
    }

    public static <T> T second(Iterable<T> ts) {
        Iterator<T> iterator = ts.iterator();
        iterator.next();
        return iterator.next();
    }

    public static <T> List<T> rest(List<T> ts) {
        return ts.subList(1, ts.size());
    }

    public static <T> List<T> take(int n, List<T> ts) {
        return ts.subList(0, Math.min(n, ts.size()));
    }

    public static boolean hasContent(Collection<?> coll) {
        return coll != null && coll.size() > 0;
    }


    @SafeVarargs
    public static <T> List<T> concat(Collection<T>... collections) {
        ArrayList<T> result = new ArrayList<>(collections[0]);
        for (int i = 1; i < collections.length; i++)
            result.addAll(collections[i]);
        return result;
    }

    @SafeVarargs
    public static <T> Set<T> union(Set<T>... s) {
        List<Set<T>> sets = list(s);
        return stream(sets).reduce((a, b) -> {
            a.addAll(b);
            return a;
        }).get();

    }

    @SafeVarargs
    public static <T> Set<T> intersection(Set<T>... sets) {
        Set<T> result = new HashSet<>(sets[0]);
        for (int i = 1; i < sets.length; i++)
            result.retainAll(sets[i]);
        return result;
    }

    @SafeVarargs
    public static <T> Set<T> difference(Set<T>... sets) {
        Set<T> result = new HashSet<>(sets[0]);
        for (int i = 1; i < sets.length; i++)
            result.removeAll(sets[i]);
        return result;
    }

    public static <K, V> Map<K, V> zipmap(Collection<? extends K> keys, Collection<? extends V> values) {
        bombUnless(keys.size() == values.size(), () -> "keys and values must be the same size. " + keys.size() + " != " + values.size());
        HashMap<K, V> result = new HashMap<>();
        Iterator<? extends V> vi = values.iterator();
        keys.forEach(k -> result.put(k, vi.next()));
        return result;
    }

    public static <K, V> List<Map.Entry<K, V>> zip(Collection<? extends K> keys, Collection<? extends V> values) {
        bombUnless(keys.size() == values.size(), () -> "keys and values must be the same size. " + keys.size() + " != " + values.size());
        List<Map.Entry<K, V>> result = emptyList();
        Iterator<? extends V> vi = values.iterator();
        keys.forEach(k -> result.add(entry(k, vi.next())));
        return result;
    }

    @SafeVarargs
    public static <K, V> Map<K, V> merge(Map<K, V>... ms) {
        List<Map<K, V>> maps = list(ms);
        Map<K, V> result = new HashMap<>(firstExample(maps));
        rest(maps).forEach(result::putAll);
        return result;
    }

    public static <K, V> Map<K, V> map() {
        return new HashMap<>();
    }

    public static <K, V> Map<K, V> synchronizedMap() {
        return Collections.synchronizedMap(map());
    }

    private static <K, V> Map<K, V> mapWith(Map<K, V> m, K k, V v) {
        m.put(k, v);
        return m;
    }

    public static <T, U> List<T> collectAll(Iterable<U> us, Function<U, Iterable<T>> toTs) {
        return stream(us).flatMap(u -> stream(toTs.apply(u))).collect(toList());
    }

    public static <T, L extends Iterable<T>> List<T> flatten(Iterable<L> listsOfT) {
        return collectAll(listsOfT, list -> list);
    }

    public static <T> List<T> reject(Iterable<T> ts, Predicate<T> predicate) {
        return filter(ts, negate(predicate));
    }

    public static <T> Predicate<T> negate(Predicate<T> pred) {
        return t -> !pred.test(t);
    }

    public static <T> List<T> compact(Iterable<T> ts) {
        return filter(ts, t -> t != null);
    }

    public static <T> List<T> compactOptionals(Iterable<Optional<T>> ts) {
        return compact(map(ts, t -> t.orElse(null)));
    }

    public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    public static <T> int count(Iterable<T> ts, Predicate<T> matches) {
        return filter(ts, matches).size();
    }

    public static <T, K, V> Map<K, V> mapFromPairs(T[] ts, Function<T, Map.Entry<K, V>> toEntry) {
        return mapFromPairs(list(ts), toEntry);
    }

    public static <T, K, V> Map<K, V> mapFromPairs(Iterable<T> ts, Function<T, Map.Entry<K, V>> toEntry) {
        Map<K, V> result = new LinkedHashMap<>();
        Map<K, List<T>> results = new HashMap<>();
        ts.forEach(t -> {
            Map.Entry<K, V> entry = toEntry.apply(t);
            results.computeIfAbsent(entry.getKey(), k -> emptyList()).add(t);
            result.put(entry.getKey(), entry.getValue());
        });
        List<K> duplicateKeys = map(filter(results.entrySet(), entry -> entry.getValue().size() > 1), Map.Entry::getKey);
        bombUnless(duplicateKeys.isEmpty(), () -> "Duplicate keys encountered!  Keys/Produced-from: " + filterKeys(results, duplicateKeys::contains));
        return result;
    }

    public static <T> StringMap smapFromPairs(Iterable<T> ts, Function<T, Map.Entry<String, Object>> toEntry) {
        return new StringMap(mapFromPairs(ts, toEntry));
    }

    public static <V> StringMap smapFromKeys(Iterable<String> ks, Function<String, V> toV) {
        return smapFromPairs(ks, k -> entry(k, toV.apply(k)));
    }

    public static <K, V, U> Map<K, U> modifyValues(Map<K, V> m, Function<V, U> toU) {
        return m.entrySet().stream().map(entry -> entry(
                entry.getKey(),
                rethrow(
                        () -> toU.apply(entry.getValue()),
                        () -> {
                            V value = entry.getValue();
                            if (value.getClass().isArray() && value.getClass().getComponentType().equals(String.class))
                                return "failed to produce new value for string array key: '" + entry.getKey() + "' and value '" +
                                        list((String[]) value) + "'";
                            return "failed to produce new value for key '" + entry.getKey() + "' and value '" + value + "'";
                        }
                )
        )).collect(HashMap::new, (m1, entry) -> m1.put(entry.getKey(), entry.getValue()), HashMap::putAll);
    }

    public static <K, V, U> Map<U, V> modifyKeys(Map<K, V> m, Function<K, U> toU) {
        return m.entrySet().stream().map(entry -> entry(toU.apply(entry.getKey()), entry.getValue()))
                .collect(HashMap::new, (m1, entry) -> m1.put(entry.getKey(), entry.getValue()), HashMap::putAll);
    }

    public static <K, V> Map<K, V> filterKeys(Map<K, V> m, Predicate<K> predicate) {
        // This collect implementation is safe because we don't modify the keys, so don't need to protect against duplicate keys
        return m.entrySet().stream()
                .filter(entry -> predicate.test(entry.getKey()))
                .collect(HashMap::new, (m1, entry) -> m1.put(entry.getKey(), entry.getValue()), HashMap::putAll);
    }

    public static <K, V> Map<K, List<V>> groupBy(Iterable<V> vs, Function<V, K> toK) {
        return tap(map(), result -> vs.forEach(v ->
                result.computeIfAbsent(toK.apply(v), k -> emptyList()).add(v)));
    }

    public static <K, V> Map<K, V> indexBy(Iterable<V> vs, Function<V, K> toK) {
        return modifyValues(groupBy(vs, toK), Objects::the);
    }

    public static <K, V> Map<K, V> indexBy(V[] vs, Function<V, K> toK) {
        return indexBy(list(vs), toK);
    }

    public static <T> Optional<Long> indexOf(Iterable<T> ts, Function<T, Boolean> isItem) {
        long i = 0L;
        for (T t : ts) {
            if (isItem.apply(t))
                return optional(i);
            i++;
        }
        return optional();
    }

    public static class InOut<T> {
        public final List<T> in;
        public final List<T> out;

        public InOut(List<T> in, List<T> out) {
            this.in = in;
            this.out = out;
        }
    }

    public static <T> InOut<T> bifurcate(Iterable<T> ts, Function<T, Boolean> isIn) {
        Map<Boolean, List<T>> result = groupBy(ts, isIn);
        return new InOut<>(result.getOrDefault(true, list()), result.getOrDefault(false, list()));
    }


    public static <T> Stream<T> stream(Iterable<T> in) {
        return StreamSupport.stream(in.spliterator(), false);
    }

    public static <T, F> List<T> map(Iterable<F> fs, Function<F, T> toT) {
        return stream(fs).map(toT).collect(toList());
    }

    public static <T, F> List<T> mapWithIndex(Iterable<F> fs, BiFunction<F, Long, T> toT) {
        One<Long> index = one(0L);
        return stream(fs).map(f -> toT.apply(f, index.value++)).collect(toList());
    }

    public static <T, F> Set<T> set(Iterable<F> fs, Function<F, T> toT) {
        return set(map(fs, toT));
    }

    public static <T, F> List<T> map(F[] fs, Function<F, T> toT) {
        return map(Arrays.asList(fs), toT);
    }

    public static <T, K, V> List<T> map(Map<K, V> m, BiFunction<K, V, T> tOfKV) {
        return map(m.entrySet(), entry -> tOfKV.apply(entry.getKey(), entry.getValue()));
    }

    public static <T> List<T> filter(Iterable<T> ts, Predicate<T> predicate) {
        return stream(ts).filter(predicate).collect(toList());
    }

    public static <K, V> void forEach(Map<K, V> m, BiConsumer<K, V> onKV) {
        m.entrySet().forEach(entry -> onKV.accept(entry.getKey(), entry.getValue()));
    }

    public static <K, V> void forEach(List<Map.Entry<K, V>> items, BiConsumer<K, V> onKV) {
        items.forEach(entry -> onKV.accept(entry.getKey(), entry.getValue()));
    }

    public static void doTimes(int n, Consumer<Integer> onN) {
        for (int i = 0; i < n; i++) onN.accept(i);
    }

    public static void doTimes(int n, Runnable r) {
        for (int i = 0; i < n; i++) r.run();
    }

    public static <T> List<T> randomTimes(int n, Function<Integer, T> makeT, boolean includeZero) {
        return makeTimes(randUnder(n) + (includeZero ? 0 : 1), makeT);
    }

    public static <R> List<R> makeTimes(int n, Function<Integer, R> r) {
        return tap(emptyList(), result -> {
            for (int i = 0; i < n; i++) result.add(r.apply(i));
        });
    }

    public static <R> List<R> makeTimes(int n, Supplier<R> r) {
        return tap(emptyList(), result -> {
            for (int i = 0; i < n; i++) result.add(r.get());
        });
    }

    public static <T, F> Optional<T> map(Optional<F> fs, Function<F, T> tOfF) {
        return fs.map(tOfF);
    }

    public static <K, V> Map<K, V> map(K k1, V v1) {
        return mapWith(map(), k1, v1);
    }

    public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2) {
        return mapWith(map(k1, v1), k2, v2);
    }

    public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3) {
        return mapWith(map(k1, v1, k2, v2), k3, v3);
    }

    public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return mapWith(map(k1, v1, k2, v2, k3, v3), k4, v4);
    }

    public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return mapWith(map(k1, v1, k2, v2, k3, v3, k4, v4), k5, v5);
    }

    public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return mapWith(map(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5), k6, v6);
    }

    public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return mapWith(map(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6), k7, v7);
    }

    public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return mapWith(map(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7), k8, v8);
    }

    public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return mapWith(map(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8), k9, v9);
    }

    public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return mapWith(map(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9), k10, v10);
    }

    public static StringMap smap() {
        return new StringMap();
    }

    public static StringMap smap(String k1, Object v1) {
        return smap().put(k1, v1);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2) {
        return smap(k1, v1).put(k2, v2);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return smap(k1, v1, k2, v2).put(k3, v3);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4) {
        return smap(k1, v1, k2, v2, k3, v3).put(k4, v4);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4).put(k5, v5);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5).put(k6, v6);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6).put(k7, v7);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7).put(k8, v8);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8).put(k9, v9);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9).put(k10, v10);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10).put(k11, v11);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11).put(k12, v12);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12).put(k13, v13);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13).put(k14, v14);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14).put(k15, v15);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15, String k16, Object v16) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15).put(k16, v16);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15, String k16, Object v16, String k17, Object v17) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16).put(k17, v17);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15, String k16, Object v16, String k17, Object v17, String k18, Object v18) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17).put(k18, v18);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15, String k16, Object v16, String k17, Object v17, String k18, Object v18, String k19, Object v19) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18).put(k19, v19);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15, String k16, Object v16, String k17, Object v17, String k18, Object v18, String k19, Object v19, String k20, Object v20) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19).put(k20, v20);
    }

    public static StringMap smap(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15, String k16, Object v16, String k17, Object v17, String k18, Object v18, String k19, Object v19, String k20, Object v20, String k21, Object v21) {
        return smap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19, k20, v20).put(k21, v21);
    }

    public static <T, U> boolean allMatchFirst(Iterable<T> ts, Function<T, U> toU) {
        U first = toU.apply(firstExample(ts));
        for (T t : ts)
            if (!toU.apply(t).equals(first))
                return false;
        return true;
    }

    public static <T> boolean any(Iterable<T> ts, Predicate<T> pred) {
        return hasContent(filter(ts, pred));
    }

    public static <T> boolean all(Iterable<T> ts, Predicate<T> pred) {
        return !hasContent(reject(ts, pred));
    }

    public static <T> List<T> repeat(T t, int n) {
        return IntStream.range(0, n).mapToObj(i -> t).collect(toList());
    }

    public static <T, U extends Comparable<U>> List<T> sortBy(Iterable<T> ts, Function<T, U> toU) {
        return stream(ts).sorted((t1, t2) -> toU.apply(t1).compareTo(toU.apply(t2))).collect(toList());
    }

    public static <K, V, U extends Comparable<U>> SortedMap<K, V> sortBy(Map<K, V> m, Function<K, U> toU) {
        TreeMap<K, V> result = new TreeMap<>((t1, t2) -> toU.apply(t1).compareTo(toU.apply(t2)));
        result.putAll(m);
        return result;
    }

    public static <T extends Comparable> List<T> sort(Iterable<T> ts) {
        return sortBy(ts, identity());
    }

    public static void sleep(long millis) {
        rethrow(() -> Thread.sleep(millis));
    }

    public static <T> void withVoid(T t, Consumer<T> onT) {
        onT.accept(t);
    }

    public static <T, U> void withVoid(T t, U u, BiConsumer<T, U> onTandU) {
        onTandU.accept(t, u);
    }

    public static <T, U, V> void withVoid(T t, U u, V v, TriConsumer<T, U, V> onTandUandV) {
        onTandUandV.accept(t, u, v);
    }

    public static <T, R> R with(T t, Function<T, R> onT) {
        return onT.apply(t);
    }

    public static <T, U, R> R with(T t, U u, BiFunction<T, U, R> onT) {
        return onT.apply(t, u);
    }

    public static <T, U, V, R> R with(T t, U u, V v, TriFunction<T, U, V, R> onT) {
        return onT.apply(t, u, v);
    }

    public static <T, U, V, W, R> R with(T t, U u, V v, W w, QuadFunction<T, U, V, W, R> onT) {
        return onT.apply(t, u, v, w);
    }

    public static <T, U, V, W, X, R> R with(T t, U u, V v, W w, X x, PentaFunction<T, U, V, W, X, R> onT) {
        return onT.apply(t, u, v, w, x);
    }

    public static <T, U, V, W, X, Y, R> R with(T t, U u, V v, W w, X x, Y y, HexFunction<T, U, V, W, X, Y, R> onT) {
        return onT.apply(t, u, v, w, x, y);
    }

    public static <T, U, V, W, X, Y, Z, R> R with(T t, U u, V v, W w, X x, Y y, Z z, SeptaFunction<T, U, V, W, X, Y, Z, R> onT) {
        return onT.apply(t, u, v, w, x, y, z);
    }

    public static <T, U, V, W, X, Y, Z, A, R> R with(T t, U u, V v, W w, X x, Y y, Z z, A a, OctaFunction<T, U, V, W, X, Y, Z, A, R> onT) {
        return onT.apply(t, u, v, w, x, y, z, a);
    }

    public static <T> T tap(T t, Consumer<T> onT) {
        onT.accept(t);
        return t;
    }

    public static <T> T tap_(T t, Runnable doSomething) {
        doSomething.run();
        return t;
    }

    @Deprecated
    public static <T> List<T> dbg(List<T> t) {
        t.forEach(System.out::println);
        System.out.println("]");
        return t;
    }

    @Deprecated
    public static <T> T dbg(T t) {
        return t;
    }

    @Deprecated
    public static <T> T dbg(T t, String msg) {
        return t;
    }

    public static <T> JSONArray jsonArray(Collection<T> stringMaps) {
        JSONArray result = new JSONArray();
        result.addAll(stringMaps);
        return result;
    }

    protected static <K, V> Function<Map<K, V>, V> f(K k) {
        return m -> m.get(k);
    }

    private static final Map<Supplier<?>, Object> lazyCache = Collections.synchronizedMap(map());

    public static <T> Supplier<T> lazy(Supplier<T> makeT) {
        return () -> (T) lazyCache.computeIfAbsent(makeT, Supplier::get);
    }

    public static void clearLazyCache() {
        lazyCache.clear();
    }

    public static <T> Optional<T> optional(T t) {
        return Optional.of(t);
    }

    public static <T> Optional<T> optional() {
        return Optional.empty();
    }

    public static <T> T randomItem(List<T> ts) {
        return ts.get(randomIndex(ts));
    }

    public static <T> int randomIndex(List<T> ts) {
        return randUnder(ts.size());
    }

    public static int randUnder(int max) {
        return random.nextInt(max);
    }

    public static <T> List<T> shuffle(List<T> ts) {
        List<T> nl = new ArrayList<>(ts);
        Collections.shuffle(nl);
        return nl;
    }

    public static boolean oneIn(int max) {
        return randUnder(max) == 0;
    }

    public static double medianInt(List<Integer> values) {
        if (isEmpty(values))
            return Double.NaN;
        int count = values.size();
        int half = count / 2;
        return count % 2 == 1
                ? values.get(half).doubleValue()
                : (values.get(half - 1) + values.get(half)) / 2.0;
    }

    public static double median(List<Long> values) {
        if (isEmpty(values))
            return Double.NaN;
        int count = values.size();
        int half = count / 2;
        return count % 2 == 1
                ? values.get(half).doubleValue()
                : (values.get(half - 1) + values.get(half)) / 2.0;
    }

    public static boolean xor(boolean a, boolean b) {
        return (a || b) && !(a && b);
    }

    public static double average(Collection<Long> values) {
        if (isEmpty(values))
            return Double.NaN;
        return ((double) sum(values)) / values.size();
    }

    public static double averageInt(Collection<Integer> values) {
        if (isEmpty(values))
            return Double.NaN;
        return ((double) sumInt(values)) / values.size();
    }

    public static double averageDouble(Collection<Double> values) {
        if (isEmpty(values))
            return Double.NaN;
        return ((double) sumDouble(values)) / values.size();
    }

    public static <T> long sum(Iterable<T> ts, Function<T, Long> makeLong) {
        return sum(map(ts, makeLong));
    }

    public static long sum(Collection<Long> values) {
        long result = 0;
        for (long v : values)
            result += v;
        return result;
    }

    private static long sumInt(Collection<Integer> values) {
        long result = 0;
        for (long v : values)
            result += v;
        return result;
    }

    public static double sumDouble(Collection<Double> values) {
        double result = 0;
        for (double v : values)
            result += v;
        return result;
    }

    public static double percentInRange(long start, long end, long value, double nanValue) {
        if (end - start == 0) return nanValue;
        return ((value - start) * 100.0) / (end - start);
    }

    public static Long min(Collection<Long> values) {
        if (isEmpty(values))
            return null;
        long result = firstExample(values);
        for (long v : values)
            result = Math.min(result, v);
        return result;
    }

    public static Long max(Collection<Long> values) {
        if (isEmpty(values))
            return null;
        long result = firstExample(values);
        for (long v : values)
            result = Math.max(result, v);
        return result;
    }

    public static <T> List<List<T>> partition(List<T> ts, int size) {
        List<List<T>> result = emptyList();
        int i = 0;
        while (i * size < ts.size()) {
            result.add(ts.subList(i * size, Math.min((i + 1) * size, ts.size())));
            i++;
        }
        return result;
    }


    //    this removes duplicates after the first occurrence of the item
//    this is not a good idea for large lists as this will take a long time
    public static <T> List<T> deduplicateMaintainingOrder(List<T> orderedDuplicates) {
        return deduplicateMaintainingOrder(orderedDuplicates, element -> element);
    }

    // Remove duplicates from a list based on provided criterion of uniqueness
    public static <T, R> List<T> deduplicateMaintainingOrder(List<T> origin, Function<T, R> criterion) {
        List<T> result = emptyList();
        Set<R> seen = set();
        for (T t : origin) {
            if (seen.contains(criterion.apply(t))) continue;
            seen.add(criterion.apply(t));
            result.add(t);
        }
        return result;
    }

    public static boolean almostEqual(Double lvalue, Double rvalue) {
        return Math.abs(lvalue - rvalue) < 0.000001;
    }

    public static boolean almostEqual(Double lvalue, Number rvalue) {
        return almostEqual(lvalue, rvalue.doubleValue());
    }

    public static String shortStringGuid(UUID guid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(guid.getMostSignificantBits());
        bb.putLong(guid.getLeastSignificantBits());
        String encoded = Base64.getEncoder().encodeToString(bb.array());
        return encoded.replaceFirst("==$", "").replace("+", "_").replace("/", "-");
    }

    public static UUID guidFromShortString(String encoded) {
        encoded = encoded.replace("_", "+").replace("-", "/") + "==";
        ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.getDecoder().decode(encoded));
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    public static String extractDomain(String email) {
        return email.replaceFirst(".*@(.*)$", "$1");
    }
}