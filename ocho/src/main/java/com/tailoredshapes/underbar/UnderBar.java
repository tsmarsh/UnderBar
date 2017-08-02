package com.tailoredshapes.underbar;

import com.tailoredshapes.underbar.data.Fork;
import com.tailoredshapes.underbar.data.Heap;
import com.tailoredshapes.underbar.exceptions.UnderBarred;
import com.tailoredshapes.underbar.function.RegularFunctions;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.tailoredshapes.underbar.Die.*;
import static com.tailoredshapes.underbar.UnderString.commaSep;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public interface UnderBar {
    Map<Supplier<?>, Object> lazyCache = Collections.synchronizedMap(hash());

    /**
     * Asserts that a collection contains exactly one value and returns it
     */
    static <T> T nonce(Iterable<T> ts) {
        Iterator<T> i = ts.iterator();
        dieUnless(i.hasNext(), () -> "input to 'nonce' cannot be empty");
        T result = i.next();
        dieIf(i.hasNext(), () -> "input to 'nonce' has length > 1: " + ts);
        return result;
    }

    /**
     * Asserts that an array contains exactly one value and returns it
     */
    static <T> T nonce(T[] ts) {
        dieIfNull(ts, () -> "input to 'nonce' cannot be null");
        dieUnless(ts.length == 1, () -> "length of input to 'nonce' must be 1. " + commaSep(list(ts)));
        return ts[0];
    }

    /**
     * Returns true if the collection isEmpty
     */
    static boolean isEmpty(Collection<?> coll) {
        return coll.isEmpty();
    }

    static <T> Optional<T> maybe(Iterable<T> ts) {
        Iterator<T> i = ts.iterator();
        if (!i.hasNext()) {
            return Optional.empty();
        }
        T result = i.next();
        dieIf(i.hasNext(), () -> "input to 'nonce' has length > 1: " + ts);
        return Optional.of(result);
    }

    /**
     * Executes a runnable if an optional is empty
     */
    static void ifAbsent(Optional<?> maybe, Runnable noT) {
        if (!maybe.isPresent())
            noT.run();
    }

    /**
     * Checks if a value is null. If it isn't execute a function, otherwise execute a supplier
     */
    static <T, R> R maybeNull(T maybeNull, Function<T, R> onT, Supplier<R> noT) {
        return optionally(ofNullable(maybeNull), onT, noT);
    }

    /**
     * Checks if a optional is present. If it isn't execute a function, otherwise execute a supplier
     */
    static <T, R> R optionally(Optional<T> maybe, Function<T, R> onT, Supplier<R> noT) {
        return maybe.isPresent() ? onT.apply(maybe.get()) : noT.get();
    }

    /**
     * Provides a mechanism for getting the value from a hash, or a default value
     */
    static <K, V, R> R maybeGet(Map<K, V> maybe, K k, Function<V, R> onT, Supplier<R> noT) {
        return maybeNull(maybe.get(k), onT, noT);
    }

    /**
     * @return An empty ArrayList
     */
    static <T> List<T> emptyList() {
        return new ArrayList<>();
    }

    /**
     * Returns an fixed size list apply ts in it
     */
    @SafeVarargs
    static <T> List<T> list(T... ts) {
        return Arrays.asList(ts);
    }


    /**
     * Returns a modifiable list from an iterable
     */
    static <T> List<T> list(Iterable<T> ts) {
        ArrayList<T> result = new ArrayList<>();
        ts.forEach(result::add);
        return result;
    }

    /**
     * Returns a modifiable list apply ts in it
     */
    @SafeVarargs
    static <T> List<T> modifiableList(T... ts) {
        List<T> result = new ArrayList<>();
        result.addAll(list(ts));
        return result;
    }

    /**
     * Creates a new HashSet of ts
     */
    @SafeVarargs
    static <T> Set<T> set(T... ts) {
        return new HashSet<>(Arrays.asList(ts));
    }

    /**
     * Creates a new HashSet of ts from an iterable
     */
    static <T> Set<T> set(Iterable<T> ts) {
        HashSet<T> result = new HashSet<>();
        ts.forEach(result::add);
        return result;
    }

    /**
     * Creates a new HashSet of T from an iterable of F using a convertion function
     */
    static <T, F> Set<T> set(Iterable<F> is, Function<F, T> toT) {
        return set(map(is, toT));
    }


    /**
     * Lambda require the objects they mutate to be either effectively final or heap allocated.
     * This function heap allocates an object.
     */
    static <T> Heap<T> heap(T t) {
        return new Heap<>(t);
    }

    /**
     * A convenience function for creating an array
     */
    @SafeVarargs
    static <T> T[] array(T... ts) {
        return ts;
    }

    /**
     * Returns the first member of an iterable collection
     */
    static <T> T first(Iterable<T> ts) {
        Iterator<T> iterator = ts.iterator();
        dieUnless(iterator.hasNext(), () -> "can't take first of empty iterable");
        return iterator.next();
    }

    /**
     * Returns the last member of an iterable collection
     */
    static <T> T last(List<T> ts) {
        dieIf(isEmpty(ts), () -> "can't take last of empty list!");
        return ts.get(ts.size() - 1);
    }

    /**
     * Returns the second member of an iterable.
     */
    static <T> T second(Iterable<T> ts) {
        Iterator<T> iterator = ts.iterator();
        iterator.next();
        return iterator.next();
    }

    /**
     * Returns everything but the first member of a collection
     */
    static <T> List<T> rest(List<T> ts) {
        return ts.subList(1, ts.size());
    }

    /**
     * Returns the first n of a collection
     */
    static <T> List<T> take(int n, List<T> ts) {
        return ts.subList(0, Math.min(n, ts.size()));
    }

    /**
     * Pretty sure this is broken
     */
    static <T> Optional<T> takeWhile(Iterable<T> ts, Predicate<T> p) {
        for (T next : ts) {
            if (p.test(next)) return optional(next);
        }
        return optional();
    }

    /**
     * Checks for null and size
     */
    static boolean hasContent(Collection<?> coll) {
        return coll != null && coll.size() > 0;
    }


    /**
     * Joins one dimensional collections
     */
    @SafeVarargs
    static <T> List<T> concat(Collection<T>... collections) {
        ArrayList<T> result = new ArrayList<>(collections[0]);
        for (int i = 1; i < collections.length; i++)
            result.addAll(collections[i]);
        return result;
    }

    /**
     * A union over N sets
     */
    @SafeVarargs
    static <T> Set<T> union(Set<T>... s) {
        List<Set<T>> sets = list(s);
        return stream(sets).reduce((a, b) -> {
            a.addAll(b);
            return a;
        }).get();

    }

    /**
     * The intersection of N sets
     */
    @SafeVarargs
    static <T> Set<T> intersection(Set<T>... sets) {
        Set<T> result = new HashSet<>(sets[0]);

        for (int i = 1; i < sets.length; i++) {
            result.retainAll(sets[i]);
        }

        return result;
    }

    /**
     * The difference of N sets
     */
    @SafeVarargs
    static <T> Set<T> difference(Set<T>... sets) {
        Set<T> result = new HashSet<>(sets[0]);

        for (int i = 1; i < sets.length; i++) {
            result.removeAll(sets[i]);
        }

        return result;
    }

    /**
     * Join a collection of keys and a collection of values into a hash
     */
    static <K, V> Map<K, V> zipmap(Collection<? extends K> keys, Collection<? extends V> values) {
        dieUnless(keys.size() == values.size(), () -> "keys and values must be nonce same size. " + keys.size() + " != " + values.size());
        HashMap<K, V> result = new HashMap<>();
        Iterator<? extends V> vi = values.iterator();
        keys.forEach(k -> result.put(k, vi.next()));
        return result;
    }

    /**
     * Pairs and lists keys and values from two collections
     */
    static <K, V> List<Map.Entry<K, V>> zip(Collection<? extends K> keys, Collection<? extends V> values) {
        dieUnless(keys.size() == values.size(), () -> "keys and values must be nonce same size. " + keys.size() + " != " + values.size());
        List<Map.Entry<K, V>> result = emptyList();
        Iterator<? extends V> vi = values.iterator();
        keys.forEach(k -> result.add(entry(k, vi.next())));
        return result;
    }

    /**
     * Like concat, for maps
     */
    @SafeVarargs
    static <K, V> Map<K, V> merge(Map<K, V>... ms) {
        List<Map<K, V>> maps = list(ms);
        Map<K, V> result = new HashMap<>(first(maps));
        rest(maps).forEach(result::putAll);
        return result;
    }

    /**
     * returns an empty HashMap
     */
    static <K, V> Map<K, V> hash() {
        return new HashMap<>();
    }

    static <K, V> Map<K, V> mapWith(Map<K, V> m, K k, V v) {
        m.put(k, v);
        return m;
    }

    /**
     * FlatMaps a function over an iterable
     */
    static <T, U> List<T> collectAll(Iterable<U> us, Function<U, Iterable<T>> toTs) {
        return stream(us).flatMap(u -> stream(toTs.apply(u))).collect(toList());
    }

    /**
     * fells trees into lists
     */
    static <T, L extends Iterable<T>> List<T> flatten(Iterable<L> listsOfT) {
        return collectAll(listsOfT, list -> list);
    }


    /**
     * The inverse of filter
     */
    static <T> List<T> reject(Iterable<T> ts, Predicate<T> predicate) {
        return filter(ts, negate(predicate));
    }

    /**
     * Inverts a predicate
     */
    static <T> Predicate<T> negate(Predicate<T> pred) {
        return t -> !pred.test(t);
    }

    /**
     * Removes nulls from a collection
     */
    static <T> List<T> compact(Iterable<T> ts) {
        return filter(ts, t -> t != null);
    }

    /**
     * Removes empty optionals from a collection
     */
    static <T> List<T> compactOptionals(Iterable<Optional<T>> ts) {
        return compact(map(ts, t -> t.orElse(null)));
    }

    /**
     * Convenience function for creating Map.Entry
     */
    static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    /**
     * Returns the number of members of a collection that satisfy a predicate
     */
    static <T> int count(Iterable<T> ts, Predicate<T> matches) {
        return filter(ts, matches).size();
    }


    /**
     * Takes an array, and a function that can convert members of that array to Map.Entry and builds a hash
     *
     * @throws UnderBarred if there are duplicate keys
     */
    static <T, K, V> Map<K, V> mapFromEntry(T[] ts, Function<T, Map.Entry<K, V>> toEntry) {
        return mapFromEntry(list(ts), toEntry);
    }

    /**
     * Takes a list, and a function that can convert members of that array to Map.Entry and builds a hash
     *
     * @throws UnderBarred if there are duplicate keys
     */
    static <T, K, V> Map<K, V> mapFromEntry(Iterable<T> ts, Function<T, Map.Entry<K, V>> toEntry) {
        Map<K, V> result = new LinkedHashMap<>();
        Map<K, List<T>> results = new HashMap<>();
        ts.forEach(t -> {
            Map.Entry<K, V> entry = toEntry.apply(t);
            results.computeIfAbsent(entry.getKey(), k -> emptyList()).add(t);
            result.put(entry.getKey(), entry.getValue());
        });
        List<K> duplicateKeys = map(filter(results.entrySet(), entry -> entry.getValue().size() > 1), Map.Entry::getKey);
        dieUnless(duplicateKeys.isEmpty(), () -> "Duplicates: " + filterKeys(results, duplicateKeys::contains));
        return result;
    }

    /**
     * Creates a new hash by Iterating over a hash, using toU to modify the values
     */
    static <K, V, U> Map<K, U> modifyValues(Map<K, V> m, Function<V, U> toU) {
        return m.entrySet().stream().map(entry -> entry(
                entry.getKey(),
                rethrow(
                        () -> toU.apply(entry.getValue()),
                        () -> {
                            V value = entry.getValue();
                            return "failed to produce new value for key '" + entry.getKey() + "' and value '" + value + "'";
                        }
                )
        )).collect(HashMap::new, (m1, entry) -> m1.put(entry.getKey(), entry.getValue()), HashMap::putAll);
    }

    /**
     * Creates a new hash by Iterating over a hash, using toU to modify the keys
     */
    static <K, V, U> Map<U, V> modifyKeys(Map<K, V> m, Function<K, U> toU) {
        return m.entrySet().stream().map(entry -> entry(toU.apply(entry.getKey()), entry.getValue()))
                .collect(HashMap::new, (m1, entry) -> m1.put(entry.getKey(), entry.getValue()), HashMap::putAll);
    }

    /**
     * Create a new hash apply only the members of m that satisfy the predicate
     */
    static <K, V> Map<K, V> filterKeys(Map<K, V> m, Predicate<K> predicate) {
        return m.entrySet().stream()
                .filter(entry -> predicate.test(entry.getKey()))
                .collect(HashMap::new, (m1, entry) -> m1.put(entry.getKey(), entry.getValue()), HashMap::putAll);
    }


    static <K, V> Map<K, List<V>> groupBy(Iterable<V> vs, Function<V, K> toK) {
        return tap(hash(), result -> vs.forEach(v ->
                result.computeIfAbsent(toK.apply(v), k -> emptyList()).add(v)));
    }

    /**
     * Find position of first element that statisfies the predicate
     */
    static <T> Optional<Long> indexOf(Iterable<T> ts, Predicate<T> isItem) {
        long i = 0L;

        for (T t : ts) {
            if (isItem.test(t))
                return optional(i);
            i++;
        }
        return optional();
    }

    /**
     * Split a collection into a fork based on a predicate
     * <p>
     * A function is used to maintain compatibility apply groupby
     */
    static <T> Fork<T> tee(Iterable<T> ts, Function<T, Boolean> isIn) {
        Map<Boolean, List<T>> result = groupBy(ts, isIn);
        return new Fork<>(result.getOrDefault(true, list()), result.getOrDefault(false, list()));
    }

    /**
     * Iterable to stream
     */
    static <T> Stream<T> stream(Iterable<T> in) {
        return StreamSupport.stream(in.spliterator(), false);
    }

    /**
     * Map a function over an iterable
     */
    static <T, F> List<T> map(Iterable<F> fs, Function<F, T> toT) {
        return stream(fs).map(toT).collect(toList());
    }

    /**
     * Map a function over an iterble apply an zero based index
     */
    static <T, F> List<T> mapWithIndex(Iterable<F> fs, BiFunction<F, Long, T> toT) {
        Heap<Long> index = heap(0L);
        return stream(fs).map(f -> toT.apply(f, index.value++)).collect(toList());
    }


    /**
     * Map a function over an array
     */
    static <T, F> List<T> map(F[] fs, Function<F, T> toT) {
        return map(Arrays.asList(fs), toT);
    }

    /**
     * Map a function over a hash
     */
    static <T, K, V> List<T> map(Map<K, V> m, BiFunction<K, V, T> tOfKV) {
        return map(m.entrySet(), entry -> tOfKV.apply(entry.getKey(), entry.getValue()));
    }

    /**
     * Return a new list containing only the members that satisfy the predicate
     */
    static <T> List<T> filter(Iterable<T> ts, Predicate<T> predicate) {
        return stream(ts).filter(predicate).collect(toList());
    }

    /**
     * Loops over a hash applying onKV, but returns void
     */
    static <K, V> void each(Map<K, V> m, BiConsumer<K, V> onKV) {
        m.entrySet().forEach(entry -> onKV.accept(entry.getKey(), entry.getValue()));
    }

    /**
     * Repeats the consumer N times apply an zero based index
     */
    static void doTimes(int n, Consumer<Integer> onN) {
        for (int i = 0; i < n; i++) onN.accept(i);
    }


    /**
     * Repeats the runnable n times
     */
    static void doTimes(int n, Runnable r) {
        for (int i = 0; i < n; i++) r.run();
    }


    /**
     * returns the result of applying the function apply an index
     */
    static <R> List<R> makeTimes(int n, Function<Integer, R> r) {
        return tap(emptyList(), result -> {
            for (int i = 0; i < n; i++) {
                result.add(r.apply(i));
            }
        });
    }

    /**
     * Returns the result of applyin the supplier n times without an index
     */
    static <R> List<R> makeTimes(int n, Supplier<R> r) {
        return tap(emptyList(), result -> {
            for (int i = 0; i < n; i++) result.add(r.get());
        });
    }

    /**
     * Boilerplate methods for generating a hash hash.
     */
    static <K, V> Map<K, V> hash(K k1, V v1) {
        return mapWith(hash(), k1, v1);
    }

    static <K, V> Map<K, V> hash(K k1, V v1, K k2, V v2) {
        return mapWith(hash(k1, v1), k2, v2);
    }

    static <K, V> Map<K, V> hash(K k1, V v1, K k2, V v2, K k3, V v3) {
        return mapWith(hash(k1, v1, k2, v2), k3, v3);
    }

    static <K, V> Map<K, V> hash(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return mapWith(hash(k1, v1, k2, v2, k3, v3), k4, v4);
    }

    static <K, V> Map<K, V> hash(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return mapWith(hash(k1, v1, k2, v2, k3, v3, k4, v4), k5, v5);
    }

    static <K, V> Map<K, V> hash(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return mapWith(hash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5), k6, v6);
    }

    static <K, V> Map<K, V> hash(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return mapWith(hash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6), k7, v7);
    }

    static <K, V> Map<K, V> hash(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return mapWith(hash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7), k8, v8);
    }

    static <K, V> Map<K, V> hash(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return mapWith(hash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8), k9, v9);
    }

    static <K, V> Map<K, V> hash(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return mapWith(hash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9), k10, v10);
    }

    /**
     * Returns true if any of the members of the iterable satisfy the predicate
     */
    static <T> boolean any(Iterable<T> ts, Predicate<T> pred) {
        return hasContent(filter(ts, pred));
    }

    /**
     * Returns true if all of the memebers of an interable satisfy the predicate
     */
    static <T> boolean all(Iterable<T> ts, Predicate<T> pred) {
        return !hasContent(reject(ts, pred));
    }

    /**
     * Return a list of n t
     */
    static <T> List<T> repeat(T t, int n) {
        return IntStream.range(0, n).mapToObj(i -> t).collect(toList());
    }

    static <T extends Comparable> List<T> sort(Iterable<T> ts) {
        return stream(ts).sorted().collect(toList());
    }

    /**
     * Returns a list sorted by toU
     */
    static <T, U extends Comparable<U>> List<T> sortBy(Iterable<T> ts, Function<T, U> toU) {
        return stream(ts).sorted((t1, t2) -> toU.apply(t1).compareTo(toU.apply(t2))).collect(toList());
    }

    /**
     * Returns a SortedMaps sorted by the natural order of keys
     */
    static <K extends Comparable, V> SortedMap<K, V> sort(Map<K, V> m) {
        return new TreeMap<>(m);
    }

    /**
     * Returns a SortedMap sored by toU
     */
    static <K, V, U extends Comparable<U>> SortedMap<K, V> sortBy(Map<K, V> m, Function<K, U> toU) {
        TreeMap<K, V> result = new TreeMap<>((t1, t2) -> toU.apply(t1).compareTo(toU.apply(t2)));
        result.putAll(m);
        return result;
    }


    /**
     * Applys a T to a function apply side effects
     */
    static <T> void withVoid(T t, Consumer<T> onT) {
        onT.accept(t);
    }

    static <T, U> void withVoid(T t, U u, BiConsumer<T, U> onTandU) {
        onTandU.accept(t, u);
    }

    static <T, U, V> void withVoid(T t, U u, V v, RegularFunctions.TriConsumer<T, U, V> f) {
        f.accept(t, u, v);
    }

    static <T, U, V, W> void withVoid(T t, U u, V v, W w, RegularFunctions.QuadConsumer<T, U, V, W> f) {
        f.accept(t, u, v, w);
    }

    static <T, U, V, W, X> void withVoid(T t, U u, V v, W w, X x, RegularFunctions.PentaConsumer<T, U, V, W, X> f) {
        f.accept(t, u, v, w, x);
    }

    static <T, U, V, W, X, Y> void withVoid(T t, U u, V v, W w, X x, Y y, RegularFunctions.HexConsumer<T, U, V, W, X, Y> f) {
        f.accept(t, u, v, w, x, y);
    }

    static <T, U, V, W, X, Y, Z> void withVoid(T t, U u, V v, W w, X x, Y y, Z z, RegularFunctions.SeptaConsumer<T, U, V, W, X, Y, Z> f) {
        f.accept(t, u, v, w, x, y, z);
    }

    static <T, U, V, W, X, Y, Z, A> void withVoid(T t, U u, V v, W w, X x, Y y, Z z, A a, RegularFunctions.OctaConsumer<T, U, V, W, X, Y, Z, A> f) {
        f.accept(t, u, v, w, x, y, z, a);
    }

    /**
     * Returns the value of applying a function
     */
    static <T, R> R apply(T t, Function<T, R> onT) {
        return onT.apply(t);
    }

    static <T, U, R> R apply(T t, U u, BiFunction<T, U, R> onT) {
        return onT.apply(t, u);
    }

    static <T, U, V, R> R apply(T t, U u, V v, RegularFunctions.TriFunction<T, U, V, R> onT) {
        return onT.apply(t, u, v);
    }

    static <T, U, V, W, R> R apply(T t, U u, V v, W w, RegularFunctions.QuadFunction<T, U, V, W, R> onT) {
        return onT.apply(t, u, v, w);
    }

    static <T, U, V, W, X, R> R apply(T t, U u, V v, W w, X x, RegularFunctions.PentaFunction<T, U, V, W, X, R> onT) {
        return onT.apply(t, u, v, w, x);
    }

    static <T, U, V, W, X, Y, R> R apply(T t, U u, V v, W w, X x, Y y, RegularFunctions.HexFunction<T, U, V, W, X, Y, R> onT) {
        return onT.apply(t, u, v, w, x, y);
    }

    static <T, U, V, W, X, Y, Z, R> R apply(T t, U u, V v, W w, X x, Y y, Z z, RegularFunctions.SeptaFunction<T, U, V, W, X, Y, Z, R> onT) {
        return onT.apply(t, u, v, w, x, y, z);
    }

    static <T, U, V, W, X, Y, Z, A, R> R apply(T t, U u, V v, W w, X x, Y y, Z z, A a, RegularFunctions.OctaFunction<T, U, V, W, X, Y, Z, A, R> onT) {
        return onT.apply(t, u, v, w, x, y, z, a);
    }

    /**
     * Apply t to onT and return t
     */
    static <T> T tap(T t, Consumer<T> onT) {
        onT.accept(t);
        return t;
    }

    /**
     * Only evaluates makeT when necessary
     */
    static <T> Supplier<T> lazy(Supplier<T> makeT) {
        return () -> (T) lazyCache.computeIfAbsent(makeT, Supplier::get);
    }

    static void clearLazyCache() {
        lazyCache.clear();
    }

    /**
     * Create an optional with value t
     */
    static <T> Optional<T> optional(T t) {
        if(t instanceof Optional) {
            return (Optional<T>) t;
        }
        return Optional.of(t);
    }

    /**
     * Create an empty optional
     */
    static <T> Optional<T> optional() {
        return Optional.empty();
    }


    /**
     * Creates a new list with the contents shuffled
     */
    static <T> List<T> shuffle(List<T> ts) {
        List<T> nl = new ArrayList<>(ts);
        Collections.shuffle(nl);
        return nl;
    }

    /**
     * Split ts into smaller lists of size
     */
    static <T> List<List<T>> partition(List<T> ts, int size) {
        List<List<T>> result = emptyList();
        int i = 0;
        while (i * size < ts.size()) {
            result.add(ts.subList(i * size, Math.min((i + 1) * size, ts.size())));
            i++;
        }
        return result;
    }

    static <T> T[][] partition(T[] ts, Class<T> c, int size) {

        int i = 0;

        int girth = ts.length / size;
        T[][] destination = (T[][]) Array.newInstance(c, ts.length % size == 0 ? girth : girth + 1, size);

        while(i * size < ts.length){
            System.arraycopy(ts, i*size, destination[i], 0, Math.min(ts.length - (i * size), size));
            i++;
        }

        return destination;
    }

    /**
     * Allows comparison of doubles
     */
    static boolean almostEqual(Double lvalue, Double rvalue) {
        return Math.abs(lvalue - rvalue) < 0.000001;
    }

    /**
     * Allows comparision of doubles against numbers
     */
    static boolean almostEqual(Double lvalue, Number rvalue) {
        return almostEqual(lvalue, rvalue.doubleValue());
    }

    /**
     * Creates a list of 0 -> max
     */
    static List<Integer> range(int max) {
        return makeTimes(max, i -> i);
    }

    /**
     * Creates a list of start -> end
     */
    static List<Integer> range(int start, int end) {
        return makeTimes(end - start, i -> i + start);
    }


    static <T, R> R reduce(Collection<T> col, R identity, BiFunction<R, T, R> fn) {
        Heap<R> h = heap(identity);
        for (T t : col) {
            h.value = fn.apply(h.value, t);
        }
        return h.value;
    }
}