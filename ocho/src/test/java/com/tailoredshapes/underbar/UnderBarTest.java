package com.tailoredshapes.underbar;

import com.tailoredshapes.underbar.data.Fork;
import com.tailoredshapes.underbar.data.Heap;
import com.tailoredshapes.underbar.exceptions.UnderBarred;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static com.tailoredshapes.underbar.UnderBar.*;
import static org.junit.Assert.*;

public class UnderBarTest {

    @Test
    public void nonceReturnsTheSoleValueFromAnIterable() throws Exception {
        assertEquals(5, (long) nonce(list(5)));
    }

    @Test(expected = UnderBarred.class)
    public void nonceBombsIfListIsEmpty() throws Exception {
        nonce(list());
    }

    @Test(expected = UnderBarred.class)
    public void nonceBombsIfListIsLargerThan1() throws Exception {
        nonce(array(1, 2));
    }

    @Test
    public void nonceReturnsTheSoleValueOfAnArray() throws Exception {
        Integer expected = 0;
        assertEquals(expected, nonce(new Integer[]{expected}));
    }

    @Test(expected = UnderBarred.class)
    public void nonceBombsIfArrayIsEmpty() throws Exception {
        nonce(new Integer[]{});
    }

    @Test(expected = UnderBarred.class)
    public void nonceBombsIfArrayIsLargerThan1() throws Exception {
        nonce(new Integer[]{0, 1});
    }


    @Test
    public void maybeBehavesLikeNonceWithOptionals() throws Exception {
        assertEquals(Optional.of(5), maybe(list(5)));
    }

    @Test
    public void maybeReturnsEmptyIfListIsEmpty() throws Exception {
        assertFalse(maybe(emptyList()).isPresent());
    }

    @Test(expected = UnderBarred.class)
    public void maybeDiesIfListIsLargerThan1() throws Exception {
        maybe(list(1, 2));
    }

    @Test
    public void ifAbsentCallsARunnableIfCollectionEmpty() throws Exception {
        Heap<Boolean> empty = heap(false);
        Heap<Boolean> full = heap(false);

        ifAbsent(optional(), () -> empty.value = true);
        ifAbsent(optional(1), () -> full.value = true);

        assertTrue(empty.value);
        assertFalse(full.value);
    }

    @Test
    public void createsAnOptional() throws Exception {
        assertEquals(Optional.of(5), optional(5));
        assertEquals(Optional.of(5), optional(optional(5)));
        assertEquals(Optional.empty(), optional());
    }

    @Test
    public void optionallyConsumesThenCallsCallbackIfEmpty() throws Exception {
        Heap<Boolean> calledBack = heap(false);
        Heap<Boolean> consumed = heap(false);

        optionally(optional(true), (t) -> consumed.value = t, () -> calledBack.value = true);

        assertFalse(calledBack.value);
        assertTrue(consumed.value);
    }

    @Test
    public void optionallyCallsCallbackIfEmpty() throws Exception {
        Heap<Boolean> calledBack = heap(false);
        Heap<Boolean> consumed = heap(false);

        optionally(optional(), (t) -> consumed.value = (Boolean) t, () -> calledBack.value = true);

        assertTrue(calledBack.value);
        assertFalse(consumed.value);
    }

    @Test
    public void firstReturnsHeadOfList() throws Exception {
        assertEquals("a", first(list("a", "b")));
    }

    @Test
    public void secondReturnsSecondofList() throws Exception {
        assertEquals("b", second(list("a", "b")));
        assertEquals("b", second(list("a", "b", "c")));
    }

    @Test
    public void lastReturnsEndOfList() throws Exception {
        assertEquals("b", last(list("a", "b")));
        assertEquals("c", last(list("a", "b", "c")));
    }

    @Test
    public void restReturnsEverythingButFirst() throws Exception {
        assertEquals(list("b", "c"), rest(list("a", "b", "c")));
    }

    @Test
    public void takeReturnsTheNthOfAList() throws Exception {
        assertEquals(list(), take(0, list(set("a", "b"))));
        assertEquals(list("a"), take(1, list("a", "b")));
        assertEquals(list("a", "b"), take(2, list("a", "b")));
        assertEquals(list("a", "b"), take(3, list("a", "b")));
    }


    @Test
    public void concatCombinesNLists() throws Exception {
        assertEquals(list("a"), concat(list(), list("a")));
        assertEquals(list("a", "b"), concat(list("a"), list("b")));
        assertEquals(list("a", "b", "c", "d"), concat(list("a", "b"), list("c"), list("d")));
    }

    @Test
    public void canMapIntoASet() throws Exception {
        assertEquals(set(1, 2), set(list("1", "1", "2"), Integer::parseInt));

    }

    @Test
    public void unionJoinsNSets() throws Exception {
        assertEquals(set(), union(set(), set()));
        assertEquals(set("a"), union(set(), set("a")));
        assertEquals(set("a"), union(set("a"), set("a")));
        assertEquals(set("a", "b"), union(set("a", "b"), set("a")));
    }

    @Test
    public void intersectionFindsMembersOfNSets() throws Exception {
        assertEquals(set(), intersection(set(), set()));
        assertEquals(set(), intersection(set(), set("a")));
        assertEquals(set("a"), intersection(set("a"), set("a")));
        assertEquals(set("a"), intersection(set("a", "b"), set("a")));
    }

    @Test
    public void differenceFindsTheExclusiveMembersOfNSets() throws Exception {
        assertEquals(set(), difference(set(), set()));
        assertEquals(set(), difference(set(), set("a")));
        assertEquals(set(), difference(set("a"), set("a")));
        assertEquals(set("b"), difference(set("a", "b"), set("a")));
    }

    @Test
    public void zipMapCombinesCollectionsOfKeysAndValues() throws Exception {
        assertEquals(hash(), zipmap(list(), list()));
        assertEquals(hash("key", "value"), zipmap(list("key"), list("value")));
        assertEquals(hash("key", "value", "key2", "value2"), zipmap(list("key", "key2"), list("value", "value2")));
    }

    @Test(expected = UnderBarred.class)
    public void zipMapRequiresAnEvenNumberOfElements() throws Exception {
        zipmap(list(), list("nooope"));
    }

    @Test
    public void zipReturnsAListOfMapEntry() throws Exception {
        assertEquals(list(), zip(list(), list()));
        assertEquals(list(entry("key", "value")), zip(list("key"), list("value")));
        assertEquals(list(entry("key", "value"), entry("key2", "value2")), zip(list("key", "key2"), list("value", "value2")));
    }

    @Test(expected = UnderBarred.class)
    public void zipRequiresAnEvenNumberOfElements() throws Exception {
        zip(list(), list("nooope"));
    }

    @Test
    public void mergeCombinesMaps() throws Exception {
        assertEquals(hash(), merge(hash()));
        assertEquals(hash("a", 1), merge(hash("a", 1)));
        assertEquals(hash("a", 1, "b", 2), merge(hash("a", 1), hash("b", 2)));
        assertEquals(hash("a", 1, "b", 2, "c", 3), merge(hash("a", 1), hash("b", 2, "c", 3)));
        assertEquals(hash("a", 2), merge(hash("a", 1), hash("a", 2)));
    }

    @Test
    public void flattenCombinesCollections() throws Exception {
        assertEquals(list(), flatten(list()));
        assertEquals(list(1), flatten(list(list(), list(1))));
        assertEquals(list(1, 2, 3, 4), flatten(list(list(1), list(2, 3), list(4))));
    }

    @Test
    public void rejectBasedOnAPredicate() throws Exception {
        assertEquals(list(), reject(list(), (x) -> !x.equals(1)));
        assertEquals(list(), reject(list(2), (x) -> !x.equals(1)));
        assertEquals(list(1), reject(list(1, 2, 3), (x) -> x != 1));
    }

    @Test
    public void negateInvertsAPredicate() throws Exception {
        assertEquals(list(), reject(list(), negate((x) -> !x.equals(1))));
        assertEquals(list(2), reject(list(2), negate((x) -> !x.equals(1))));
        assertEquals(list(2, 3), reject(list(1, 2, 3), negate((x) -> x != 1)));
    }

    @Test
    public void compactRemovesNulls() throws Exception {
        assertEquals(list(1, 2, 3), compact(list(1, null, 2, null, null, 3)));
    }

    @Test
    public void compactOptionalsRemovesEmpties() throws Exception {
        assertEquals(list(1, 2, 3),
                compactOptionals(list(optional(1), optional(), optional(2), optional(), optional(), optional(3))));
    }

    @Test
    public void countShouldCount() throws Exception {
        assertEquals(0, count(list(), (x) -> x.equals(1)));
        assertEquals(1, count(list(1), (x) -> x.equals(1)));
        assertEquals(3, count(list(1, 1, 2, 1), (x) -> x.equals(1)));
    }

    @Test
    public void mapFromPairsCreatesAMapFromPairsWithAFunction() throws Exception {
        assertEquals(hash("a", 1, "b", 2),
                mapFromEntry(list(list("a", 1), list("b", 2)), (l) -> entry(l.get(0), l.get(1))));
    }

    @Test
    public void modifyKeysShouldChangeTheKeysInAMap() throws Exception {
        Map<String, Integer> in = hash("wrong", 1, "foo", 1);
        Map<String, Integer> out = hash("right", 1, "foo", 1);

        assertEquals(out, modifyKeys(in, (k) -> k.equals("wrong") ? "right" : k));
    }

    @Test
    public void modifyValueShouldChangeTheValuesInAMap() throws Exception {
        assertEquals(hash("a", 1, "b", 2), modifyValues(hash("a", "1", "b", "2"), Integer::parseInt));
    }

    @Test(expected = UnderBarred.class)
    public void modifyValuesShouldDegradeGracefully() throws Exception {
        modifyValues(hash("a", "twifty", "b", "2"), Integer::parseInt);
    }

    @Test
    public void filterKeysShouldReturnAMapWithKeysThatPassPredicate() throws Exception {
        Map<String, Integer> in = hash("wrong", 1, "foo", 1);
        Map<String, Integer> out = hash("foo", 1);

        assertEquals(out, filterKeys(in, (k) -> k.equals("foo")));
    }

    @Test
    public void groupByCollatesBasedOnAFunction() throws Exception {
        assertEquals(hash(String.class, list("a", "b"), Integer.class, list(1, 3, 2)),
                groupBy(list("a", 1, "b", 3, 2), Serializable::getClass));

        assertEquals(hash(0, list(2, 4), 1, list(1, 3, 5)),
                groupBy(list(1, 2, 3, 4, 5), ((v) -> v % 2)));
    }

    @Test
    public void bifurcateSplitsACollectionBasedOnAPredicate() throws Exception {
        Fork<Integer> out = tee(list(1, 2, 3, 4), (v) -> v % 2 == 0);
        assertEquals(list(2, 4), out.in);
        assertEquals(list(1, 3), out.out);
    }

    @Test
    public void mapWithIndexGivesAccessToAnIndexDuringAMap() throws Exception {
        assertEquals(modifiableList(1L, 3L, 5L, 7L, 9L), mapWithIndex(list(1, 2, 3, 4, 5), (i, v) -> i + v));
    }

    @Test
    public void mapPerformsAFunctionOverACollection() throws Exception {
        assertEquals(list(2, 3, 4, 5), map(list(1, 2, 3, 4), (x) -> x + 1));
    }

    @Test
    public void mapWorksOnArrays() throws Exception {
        assertEquals(list(2, 3, 4, 5), map(array(1, 2, 3, 4), (x) -> x + 1));
    }

    @Test
    public void mapWorksOnMaps() throws Exception {
        assertEquals(
                list(2, 3, 4, 5),
                map(
                        hash("a", 1, "b", 2, "c", 3, "d", 4),
                        (k, v) -> v + 1));
    }

    @Test
    public void doTimesRepeatsAFunction() throws Exception {
        Heap<Integer> one = heap(0);
        doTimes(5, () -> one.value += 1);
        assertEquals(5, (int) one.value);
    }

    @Test
    public void doTimesRepeatsAFunctionWithAnIndex() throws Exception {
        Heap<Integer> one = heap(1);
        doTimes(5, (i) -> one.value += i);
        assertEquals(11, (int) one.value);
    }

    @Test
    public void anyPassesIfAnyValuesInTheCollectionPass() throws Exception {
        assertFalse(any(list(), (x) -> x.equals(2)));
        assertTrue(any(list(1, 1, 2, 1), (x) -> x == 2));
    }

    @Test
    public void allPassesIfAllValuesInTheCollectionPass() throws Exception {
        assertTrue(all(list(), (x) -> x.equals(2)));
        assertFalse(all(list(1, 1, 2, 1), (x) -> x == 2));
        assertTrue(all(list(1, 1, 1), (x) -> x == 1));
    }

    @Test
    public void repeatRepeatsAFunction() throws Exception {
        assertEquals(list(1, 1, 1, 1, 1), repeat(1, 5));
    }

    @Test
    public void sortBySortsAListByAComparable() throws Exception {
        assertEquals(list(1, 2, 3, 4, 5), sortBy(list(1, 3, 5, 2, 4), (x) -> x));
    }

    @Test
    public void sortSortsAListOfComparables() throws Exception {
        assertEquals(list(1, 2, 3, 4, 5), sort(list(1, 3, 5, 2, 4)));
    }

    @Test
    public void withVoidDoesNotReturnAValue() throws Exception {
        Heap<Integer> sideEffect = heap(0);
        withVoid(5, (x) -> sideEffect.value = x);
        assertEquals(5, (int) sideEffect.value);

        withVoid(1, 2, (x, y) -> sideEffect.value = x + y);
        assertEquals(3, (int) sideEffect.value);

        withVoid(1, 2, 3, (x, y, z) -> sideEffect.value = x + y + z);
        assertEquals(6, (int) sideEffect.value);

        withVoid(1, 2, 3, 4, (w, x, y, z) -> sideEffect.value = w + x + y + z);
        assertEquals(10, (int) sideEffect.value);

        withVoid(1, 2, 3, 4, 5, (v, w, x, y, z) -> sideEffect.value = v + w + x + y + z);
        assertEquals(15, (int) sideEffect.value);

        withVoid(1, 2, 3, 4, 5, 6, (u, v, w, x, y, z) -> sideEffect.value = u + v + w + x + y + z);
        assertEquals(21, (int) sideEffect.value);

        withVoid(1, 2, 3, 4, 5, 6, 7, (t, u, v, w, x, y, z) -> sideEffect.value = t + u + v + w + x + y + z);
        assertEquals(28, (int) sideEffect.value);

        withVoid(1, 2, 3, 4, 5, 6, 7, 8, (s, t, u, v, w, x, y, z) -> sideEffect.value = s + t + u + v + w + x + y + z);
        assertEquals(36, (int) sideEffect.value);

    }

    @Test
    public void withAppliesAndReturns() throws Exception {
        assertEquals(1, (int) apply(1, (x) -> x));
        assertEquals(
                list(1, 2),
                apply(1, 2, (x, y) -> list(x, y)));
        assertEquals(
                list(1, 2, 3),
                apply(1, 2, 3, (x, y, z) -> list(x, y, z)));
        assertEquals(
                list(1, 2, 3, 4),
                apply(1, 2, 3, 4, (x, y, z, a) -> list(x, y, z, a)));
        assertEquals(
                list(1, 2, 3, 4, 5),
                apply(1, 2, 3, 4, 5, (a, b, c, d, e) -> list(a, b, c, d, e)));
        assertEquals(
                list(1, 2, 3, 4, 5, 6),
                apply(1, 2, 3, 4, 5, 6, (a, b, c, d, e, f) -> list(a, b, c, d, e, f)));
        assertEquals(
                list(1, 2, 3, 4, 5, 6, 7),
                apply(1, 2, 3, 4, 5, 6, 7, (a, b, c, d, e, f, g) -> list(a, b, c, d, e, f, g)));
        assertEquals(
                list(1, 2, 3, 4, 5, 6, 7, 8),
                apply(1, 2, 3, 4, 5, 6, 7, 8, (a, b, c, d, e, f, g, h) -> list(a, b, c, d, e, f, g, h)));
    }

    @Test
    public void tapCallsAFunction() throws Exception {
        Heap<Integer> sideEffect = heap(0);

        assertEquals(1, (int) tap(1, (x) -> sideEffect.value = x));
        assertEquals(1, (int) sideEffect.value);
    }

    @Test
    public void partitionTest() throws Exception {
        assertEquals(
                list(list(1, 2), list(3, 4), list(5)),
                partition(list(1, 2, 3, 4, 5), 2));
    }

    @Test
    public void superMapTest() throws Exception {
        Map<String, Integer> map = hash(
                "a", 1,
                "b", 2,
                "c", 3,
                "d", 4,
                "e", 5,
                "f", 6,
                "g", 7,
                "h", 8,
                "i", 9,
                "10", 10);
        assertEquals(10, map.size());
    }

    @Test
    public void takeWhileTest() throws Exception {
        Heap<Boolean> one = heap(false);
        Heap<Boolean> two = heap(false);

        clearLazyCache();

        Optional<Supplier<Supplier<Boolean>>> supplierSupplier = takeWhile(
                list(lazy(() -> (Supplier<Boolean>) () -> {
                    one.value = true;
                    return true;
                }), lazy(() -> (Supplier<Boolean>) () -> {
                    two.value = true;
                    return true;
                })), (x) -> x.get().get());

        assertTrue(supplierSupplier.get().get().get());
        assertTrue(one.value);
        assertFalse(two.value);
    }


    @Test
    public void takeWhileDeepTest() throws Exception {
        Heap<Integer> one = heap(0);
        Heap<Integer> two = heap(0);
        Heap<Integer> three = heap(0);

        clearLazyCache();

        Optional<Supplier<Supplier<Boolean>>> supplierSupplier = takeWhile(
                list(
                        lazy(() -> (Supplier<Boolean>) () -> {
                            one.value = 1;
                            return false;
                        }),
                        lazy(() -> (Supplier<Boolean>) () -> {
                            two.value = 2;
                            return true;
                        }),
                        lazy(() -> (Supplier<Boolean>) () -> {
                            three.value = 3;
                            return false;
                        })), (x) -> x.get().get());

        assertTrue(supplierSupplier.get().get().get());
        assertEquals(2, (int) two.value);
        assertEquals(1, (int) one.value);
        assertEquals(0, (int) three.value);
    }

    @Test
    public void makeTimesTest() throws Exception {
        assertEquals(list("foo", "foo", "foo"), makeTimes(3, () -> "foo"));
        assertEquals(list("foo0", "foo1", "foo2"), makeTimes(3, (i) -> "foo" + i));
    }

    @Test
    public void almostEqualTest() throws Exception {
        assertTrue(almostEqual(0.000001, 0.0000001));
        assertFalse(almostEqual(0.000001, 0.000002));

        assertTrue(almostEqual(0.1, 1 / 10.0f));
    }

    @Test
    public void rangeTest() throws Exception {
        assertEquals(list(0, 1, 2, 3), range(4));

        assertEquals(list(2, 3), range(2, 4));
    }

    @Test
    public void shuffleTest() throws Exception {
        List<Integer> deck = range(52);
        assertNotEquals(deck, shuffle(deck));
    }

    @Test
    public void mapFromEntryTest() throws Exception {
        Map<String, Integer> result = mapFromEntry(
                array("h", "e", "l", "o"),
                (c) -> entry(c, (int) c.charAt(0)));

        assertEquals(hash("h", 104, "e", 101, "l", 108, "o", 111), result);
    }

    @Test(expected = UnderBarred.class)
    public void mapFromEntryDuplicateTest() throws Exception {
        mapFromEntry(
                array("h", "e", "l", "l", "o"),
                (c) -> entry(c, (int) c.charAt(0)));
    }

    @Test
    public void forEachTest() throws Exception {
        Heap<Integer> s = heap(0);
        each(zipmap(range(5), range(5, 10)), (k, v) -> s.value += k + v);
        assertEquals(45, s.value.intValue());
    }


    @Test
    public void shouldReduceACollectionWithIdentity() throws Exception {
        assertEquals("prefix123", reduce(list(1, 2, 3), "prefix", (a, v) -> a + v));
        assertEquals("prefix", reduce(new ArrayList<String>(), "prefix", (a, v) -> a + v));
    }


    @Test
    public void shouldReduceACollectionToAnInteger() throws Exception {
        assertEquals(Integer.valueOf(6), reduce(list("1", "2", "3"), 0, (Integer a, String v) -> a + Integer.parseInt(v)));
    }


    @Test
    public void shouldMakeAccessingAMapSafer() throws Exception {
        Map<String, String> map = hash("foo", "bar");
        assertEquals("bar", maybeGet(map, "foo", x -> x, () -> "nope"));
        assertEquals("nope", maybeGet(map, "derp", x -> x, () -> "nope"));
    }

    @Test
    public void shouldReturnTheFirstMemberThatSatifiesPredicate() throws Exception {
        assertEquals(optional(1L), indexOf(list("foo", "eggs", "spam"), (v) -> v.equals("eggs")));
        assertEquals(optional(), indexOf(list("foo", "eggs", "spam"), (v) -> v.equals("ham")));
        assertEquals(optional(), indexOf(list(), (v) -> v.equals("ham")));

    }

    @Test
    public void shouldSortAMap() throws Exception {
        assertEquals(list(1, 2, 3), list(sort(hash(1, "1", 3, "3", 2, "2")).keySet()));
        assertEquals(list(1, 2, 3), list(sortBy(hash(1, "1", 3, "3", 2, "2"), x -> x).keySet()));
    }
}