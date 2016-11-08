package com.tailoredshapes.underbar;

import com.tailoredshapes.underbar.data.Fork;
import com.tailoredshapes.underbar.data.Heap;
import com.tailoredshapes.underbar.function.RegularFunctions;

import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.tailoredshapes.underbar.Die.*;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;

public class UnderBar {
    public static final Random random = new Random();
    private static final Map<Supplier<?>, Object> lazyCache = Collections.synchronizedMap(map());

    /**
     * Asserts that a collection contains exactly one value and returns it
     *
     * Examples:
     * {@link com.tailoredshapes.underbar.UnderBarTest#nonceReturnsTheSoleValueFromAnIterable() nonceReturnsTheSoleValueFromAnIterable}
     */
    public static <T> T nonce(Iterable<T> ts) {
        Iterator<T> i = ts.iterator();
        dieUnless(i.hasNext(), () -> "input to 'nonce' cannot be empty");
        T result = i.next();
        dieIf(i.hasNext(), () -> "input to 'nonce' has length > 1: " + ts);
        return result;
    }

    /**
     * Asserts that an array contains exactly one value and returns it
     *
     * @param ts An array with a single value
     * @param <T> The type of the value
     * @return the value in the array
     */
    public static <T> T nonce(T[] ts) {
        dieIfNull(ts, () -> "input to 'nonce' cannot be null");
        dieUnless(ts.length == 1, () -> "length of input to 'nonce' must be 1. " + UnderString.commaSep(list(ts)));
        return ts[0];
    }

    /**
     * Returns true if the collection isEmpty
     *
     * @param coll a collection
     * @return true if the collection isEmpty
     */
    public static boolean isEmpty(Collection<?> coll) {
        return coll.isEmpty();
    }

    public static <T> Optional<T> maybe(Iterable<T> ts) {
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
     * @param maybe the optional under test
     * @param noT the runnable you'd like to execute
     */
    public static void ifAbsent(Optional<?> maybe, Runnable noT) {
        if (!maybe.isPresent())
            noT.run();
    }

    /**
     * Checks if a value is null. If it isn't execute a function, otherwise execute a supplier
     * @param maybeNull the value under test
     * @param onT what to do if the value isn't null
     * @param noT what to do if the value is null
     * @param <T> The type of the incoming value
     * @param <R> The type of out going value
     * @return The result of either onT or noT
     */
    public static <T, R> R maybeNull(T maybeNull, Function<T, R> onT, Supplier<R> noT) {
        return optionally(ofNullable(maybeNull), onT, noT);
    }

    /**
     * Checks if a optional is present. If it isn't execute a function, otherwise execute a supplier
     * @param maybe the value under test
     * @param onT what to do if the value isn't null
     * @param noT what to do if the value is null
     * @param <T> The type of the incoming value
     * @param <R> The type of out going value
     * @return The result of either onT or noT
     */
    public static <T, R> R optionally(Optional<T> maybe, Function<T, R> onT, Supplier<R> noT) {
        Heap<R> one = heap(null);
        maybe.ifPresent(t -> one.value = onT.apply(t));
        if (!maybe.isPresent())
            one.value = noT.get();
        return one.value;
    }

    /**
     * Provides a mechanism for getting the value from a map, or a default value
     *
     * @param maybe the map
     * @param k The key
     * @param onT The function to execute if the key is present
     * @param noT The function to execute if the key is not present
     * @param <K> The type of the key
     * @param <V> The type of the value in the map
     * @param <R> The return type of the function and supplier
     * @return The calculated value of either onT or noT
     */
    public static <K, V, R> R maybeGet(Map<K, V> maybe, K k, Function<V, R> onT, Supplier<R> noT) {
        return maybeNull(maybe.get(k), onT, noT);
    }

    /**
     * @return An empty ArrayList
     */
    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }

    /**
     * Returns an fixed size list with ts in it
     * @param ts the values
     * @param <T> The type of the list
     * @return An ArrayList containing ts.
     */
    @SafeVarargs
    public static <T> List<T> list(T... ts) {
        return Arrays.asList(ts);
    }


    /**
     * Returns a modifiable list from an iterable
     * @param ts the values
     * @param <T> The type of the list
     * @return An ArrayList containing ts.
     */
    public static <T> List<T> list(Iterable<T> ts) {
        ArrayList<T> result = new ArrayList<>();
        ts.forEach(result::add);
        return result;
    }

    /**
     * Returns a modifiable list with ts in it
     *
     * @param ts the values
     * @param <T> the type of the list
     * @return the list
     */
    @SafeVarargs
    public static <T> List<T> modifiableList(T... ts) {
        List<T> result = new ArrayList<>();
        result.addAll(list(ts));
        return result;
    }

    /**
     * Creates a new HashSet of ts
     *
     * @param ts the values
     * @param <T> the type of the Set contents
     * @return the set
     */
    @SafeVarargs
    public static <T> Set<T> set(T... ts) {
        return new HashSet<>(Arrays.asList(ts));
    }

    /**
     * Creates a new HashSet of ts from an iterable
     *
     * @param ts the values
     * @param <T> the type of the Set contents
     * @return the set
     */
    public static <T> Set<T> set(Iterable<T> ts) {
        HashSet<T> result = new HashSet<>();
        ts.forEach(result::add);
        return result;
    }

    /**
     * Creates a new HashSet of T from an iterable of F using a convertion function
     *
     * @param is the values
     * @param <T> the type of the Set contents
     * @return the set
     */
    public static <T, F> Set<T> set(Iterable<F> is, Function<F, T> toT) {
        return set(map(is, toT));
    }


    /**
     * Creates a sorted list of Map.Entry from a map and a comparator
     *
     * @param kv the map
     * @param comparator the sorting function
     * @return
     */
    public static <K, V, S extends Comparable<S>> List<Map.Entry<K, V>> sortBy(Map<K, V> kv, BiFunction<K, V, S> comparator) {
        return sortBy(kv.entrySet(), entry -> comparator.apply(entry.getKey(), entry.getValue()));
    }


    /**
     * Lambda require the objects they mutate to be either effectively final or heap allocated.
     * This function heap allocates an object.
     *
     * @param t the object heap allocate
     * @param <T> the type
     * @return Returns a {@link com.tailoredshapes.underbar.data.Heap}
     */
    public static <T> Heap<T> heap(T t) {
        return new Heap<>(t);
    }

    /**
     * A convenience function for creating an array
     */
    @SafeVarargs
    public static <T> T[] array(T... ts) {
        return ts;
    }

    /**
     * Returns the first member of an iterable collection
     */
    public static <T> T first(Iterable<T> ts) {
        Iterator<T> iterator = ts.iterator();
        dieUnless(iterator.hasNext(), () -> "can't take first of empty iterable");
        return iterator.next();
    }

    /**
     * Returns the last member of an iterable collection
     */
    public static <T> T last(List<T> ts) {
        dieIf(isEmpty(ts), () -> "can't take last of empty list!");
        return ts.get(ts.size() - 1);
    }

    /**
     * Returns the second member of an iterable.
     *
     */
    public static <T> T second(Iterable<T> ts) {
        Iterator<T> iterator = ts.iterator();
        iterator.next();
        return iterator.next();
    }

    /**
     * Returns everything but the first member of a collection
     */
    public static <T> List<T> rest(List<T> ts) {
        return ts.subList(1, ts.size());
    }

    /**
     * Returns the first n of a collection
     */
    public static <T> List<T> take(int n, List<T> ts) {
        return ts.subList(0, Math.min(n, ts.size()));
    }

    /**
     * Pretty sure this is broken
     */
    public static <T> Optional<T> takeWhile(Iterable<T> ts, Predicate<T> p) {
        for (T next : ts) {
            if (p.test(next)) return optional(next);
        }
        return optional();
    }

    /**
     * Checks for null and size
     */
    public static boolean hasContent(Collection<?> coll) {
        return coll != null && coll.size() > 0;
    }


    /**
     * Joins one dimensional collections
     *
     * @param collections
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> List<T> concat(Collection<T>... collections) {
        ArrayList<T> result = new ArrayList<>(collections[0]);
        for (int i = 1; i < collections.length; i++)
            result.addAll(collections[i]);
        return result;
    }

    /**
     * A union over N sets
     */
    @SafeVarargs
    public static <T> Set<T> union(Set<T>... s) {
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
    public static <T> Set<T> intersection(Set<T>... sets) {
        Set<T> result = new HashSet<>(sets[0]);
        for (int i = 1; i < sets.length; i++)
            result.retainAll(sets[i]);
        return result;
    }

    /**
     * The difference of N sets
     *
     */
    @SafeVarargs
    public static <T> Set<T> difference(Set<T>... sets) {
        Set<T> result = new HashSet<>(sets[0]);
        for (int i = 1; i < sets.length; i++)
            result.removeAll(sets[i]);
        return result;
    }

    /**
     * Join a collection of keys and a collection of values into a map
     */
    public static <K, V> Map<K, V> zipmap(Collection<? extends K> keys, Collection<? extends V> values) {
        dieUnless(keys.size() == values.size(), () -> "keys and values must be nonce same size. " + keys.size() + " != " + values.size());
        HashMap<K, V> result = new HashMap<>();
        Iterator<? extends V> vi = values.iterator();
        keys.forEach(k -> result.put(k, vi.next()));
        return result;
    }

    /**
     * Pairs and lists keys and values from two collections
     */
    public static <K, V> List<Map.Entry<K, V>> zip(Collection<? extends K> keys, Collection<? extends V> values) {
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
    public static <K, V> Map<K, V> merge(Map<K, V>... ms) {
        List<Map<K, V>> maps = list(ms);
        Map<K, V> result = new HashMap<>(first(maps));
        rest(maps).forEach(result::putAll);
        return result;
    }

    /**
     * returns an empty HashMap
     */
    public static <K, V> Map<K, V> map() {
        return new HashMap<>();
    }

    private static <K, V> Map<K, V> mapWith(Map<K, V> m, K k, V v) {
        m.put(k, v);
        return m;
    }

    /**
     * FlatMaps a function over an iterable
     */
    public static <T, U> List<T> collectAll(Iterable<U> us, Function<U, Iterable<T>> toTs) {
        return stream(us).flatMap(u -> stream(toTs.apply(u))).collect(toList());
    }

    /**
     * fells trees into lists
     */
    public static <T, L extends Iterable<T>> List<T> flatten(Iterable<L> listsOfT) {
        return collectAll(listsOfT, list -> list);
    }


    /**
     * The inverse of filter
     */
    public static <T> List<T> reject(Iterable<T> ts, Predicate<T> predicate) {
        return filter(ts, negate(predicate));
    }

    /**
     * Inverts a predicate
     */
    public static <T> Predicate<T> negate(Predicate<T> pred) {
        return t -> !pred.test(t);
    }

    /**
     * Removes nulls from a collection
     */
    public static <T> List<T> compact(Iterable<T> ts) {
        return filter(ts, t -> t != null);
    }

    /**
     * Removes empty optionals from a collection
     */
    public static <T> List<T> compactOptionals(Iterable<Optional<T>> ts) {
        return compact(map(ts, t -> t.orElse(null)));
    }

    /**
     * Convenience function for creating Map.Entry
     */
    public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    /**
     * Returns the number of members of a collection that satisfy a predicate
     */
    public static <T> int count(Iterable<T> ts, Predicate<T> matches) {
        return filter(ts, matches).size();
    }


    public static <T, K, V> Map<K, V> mapFromEntry(T[] ts, Function<T, Map.Entry<K, V>> toEntry) {
        return mapFromEntry(list(ts), toEntry);
    }

    public static <T, K, V> Map<K, V> mapFromEntry(Iterable<T> ts, Function<T, Map.Entry<K, V>> toEntry) {
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
        // This collect implementation is safe because we don't modify nonce keys, so don't need to protect against duplicate keys
        return m.entrySet().stream()
                .filter(entry -> predicate.test(entry.getKey()))
                .collect(HashMap::new, (m1, entry) -> m1.put(entry.getKey(), entry.getValue()), HashMap::putAll);
    }

    public static <K, V> Map<K, List<V>> groupBy(Iterable<V> vs, Function<V, K> toK) {
        return tap(map(), result -> vs.forEach(v ->
                result.computeIfAbsent(toK.apply(v), k -> emptyList()).add(v)));
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

    public static <T> Fork<T> tee(Iterable<T> ts, Function<T, Boolean> isIn) {
        Map<Boolean, List<T>> result = groupBy(ts, isIn);
        return new Fork<>(result.getOrDefault(true, list()), result.getOrDefault(false, list()));
    }

    public static <T> Stream<T> stream(Iterable<T> in) {
        return StreamSupport.stream(in.spliterator(), false);
    }

    public static <T, F> List<T> map(Iterable<F> fs, Function<F, T> toT) {
        return stream(fs).map(toT).collect(toList());
    }

    public static <T, F> List<T> mapWithIndex(Iterable<F> fs, BiFunction<F, Long, T> toT) {
        Heap<Long> index = heap(0L);
        return stream(fs).map(f -> toT.apply(f, index.value++)).collect(toList());
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

    public static <K, V> void each(Map<K, V> m, BiConsumer<K, V> onKV) {
        m.entrySet().forEach(entry -> onKV.accept(entry.getKey(), entry.getValue()));
    }

    public static <K, V> void each(List<Map.Entry<K, V>> items, BiConsumer<K, V> onKV) {
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
            for (int i = 0; i < n; i++) {
                result.add(r.apply(i));
            }
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

    public static <T> void withVoid(T t, Consumer<T> onT) {
        onT.accept(t);
    }

    public static <T, U> void withVoid(T t, U u, BiConsumer<T, U> onTandU) {
        onTandU.accept(t, u);
    }

    public static <T, U, V> void withVoid(T t, U u, V v, RegularFunctions.TriConsumer<T, U, V> onTandUandV) {
        onTandUandV.accept(t, u, v);
    }

    public static <T, R> R with(T t, Function<T, R> onT) {
        return onT.apply(t);
    }

    public static <T, U, R> R with(T t, U u, BiFunction<T, U, R> onT) {
        return onT.apply(t, u);
    }

    public static <T, U, V, R> R with(T t, U u, V v, RegularFunctions.TriFunction<T, U, V, R> onT) {
        return onT.apply(t, u, v);
    }

    public static <T, U, V, W, R> R with(T t, U u, V v, W w, RegularFunctions.QuadFunction<T, U, V, W, R> onT) {
        return onT.apply(t, u, v, w);
    }

    public static <T, U, V, W, X, R> R with(T t, U u, V v, W w, X x, RegularFunctions.PentaFunction<T, U, V, W, X, R> onT) {
        return onT.apply(t, u, v, w, x);
    }

    public static <T, U, V, W, X, Y, R> R with(T t, U u, V v, W w, X x, Y y, RegularFunctions.HexFunction<T, U, V, W, X, Y, R> onT) {
        return onT.apply(t, u, v, w, x, y);
    }

    public static <T, U, V, W, X, Y, Z, R> R with(T t, U u, V v, W w, X x, Y y, Z z, RegularFunctions.SeptaFunction<T, U, V, W, X, Y, Z, R> onT) {
        return onT.apply(t, u, v, w, x, y, z);
    }

    public static <T, U, V, W, X, Y, Z, A, R> R with(T t, U u, V v, W w, X x, Y y, Z z, A a, RegularFunctions.OctaFunction<T, U, V, W, X, Y, Z, A, R> onT) {
        return onT.apply(t, u, v, w, x, y, z, a);
    }

    public static <T> T tap(T t, Consumer<T> onT) {
        onT.accept(t);
        return t;
    }

    public static <T> T tapVoid(T t, Runnable doSomething) {
        doSomething.run();
        return t;
    }

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

    public static <T> List<List<T>> partition(List<T> ts, int size) {
        List<List<T>> result = emptyList();
        int i = 0;
        while (i * size < ts.size()) {
            result.add(ts.subList(i * size, Math.min((i + 1) * size, ts.size())));
            i++;
        }
        return result;
    }

    public static String extractDomain(String email) {
        return email.replaceFirst(".*@(.*)$", "$1");
    }

    public static boolean almostEqual(Double lvalue, Double rvalue) {
        return Math.abs(lvalue - rvalue) < 0.000001;
    }

    public static boolean almostEqual(Double lvalue, Number rvalue) {
        return almostEqual(lvalue, rvalue.doubleValue());
    }

    public static List<Integer> range(int max){
        return makeTimes(max, i->i);
    }

    public static List<Integer> range(int start, int end){
        return makeTimes(end - start, i->i+start);
    }

    public static <T, R> R reduce(Collection<T> col, R identity, BiFunction<R, T, R> fn){
        Heap<R> h = heap(identity);
        for(T t: col){
            h.value = fn.apply(h.value, t);
        }
        return h.value;
    }
}