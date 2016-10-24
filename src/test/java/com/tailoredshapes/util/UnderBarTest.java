package com.tailoredshapes.util;

import org.junit.Test;

import java.io.Serializable;
import java.util.*;

import static com.tailoredshapes.util.UnderBar.*;
import static org.junit.Assert.*;

public class UnderBarTest {

    @Test
    public void theReturnsTheSoleValueFromAnIterable() throws Exception {
        assertEquals(5, (long) the(list(5)));
    }

    @Test(expected = RuntimeException.class)
    public void theBombsIfListIsEmpty() throws Exception {
        the(list());
    }

    @Test(expected = RuntimeException.class)
    public void theBombsIfListIsLargerThan1() throws Exception {
        the(array(1, 2));
    }

    @Test
    public void theReturnsTheSoleValueOfAnArray() throws Exception {
        Integer expected = 0;
        assertEquals(expected, the(new Integer[]{expected}));
    }

    @Test(expected = RuntimeException.class)
    public void theBombsIfArrayIsEmpty() throws Exception {
        the(new Integer[]{});
    }

    @Test(expected = RuntimeException.class)
    public void theBombsIfArrayIsLargerThan1() throws Exception {
        the(new Integer[]{0, 1});
    }


    @Test
    public void maybeBehavesLikeTheWithOptional() throws Exception {
        assertEquals(Optional.of(5), maybe(list(5)));
    }

    @Test
    public void maybeReturnsEmptyIfListIsEmpty() throws Exception {
        assertFalse(maybe(emptyList()).isPresent());
    }

    @Test(expected = RuntimeException.class)
    public void maybeBombsIfListIsLargerThan1() throws Exception {
        maybe(list(1, 2));
    }

    @Test
    public void ifAbsentCallsARunnableIfCollectionEmpty() throws Exception {
        One<Boolean> empty = one(false);
        One<Boolean> full = one(false);

        ifAbsent(optional(), () -> empty.value = true);
        ifAbsent(optional(1), () -> full.value = true);

        assertTrue(empty.value);
        assertFalse(full.value);
    }

    @Test
    public void optionallyConsumesThenCallsCallbackIfEmpty() throws Exception {
        One<Boolean> calledBack = one(false);
        One<Boolean> consumed = one(false);

        optionally(optional(true), (t) -> consumed.value = t, () -> calledBack.value = true);

        assertFalse(calledBack.value);
        assertTrue(consumed.value);
    }

    @Test
    public void optionallyCallsCallbackIfEmpty() throws Exception {
        One<Boolean> calledBack = one(false);
        One<Boolean> consumed = one(false);

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
        assertEquals(list(), take(0, list("a", "b")));
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
        assertEquals(map(), zipmap(list(), list()));
        assertEquals(map("key", "value"), zipmap(list("key"), list("value")));
        assertEquals(map("key", "value", "key2", "value2"), zipmap(list("key", "key2"), list("value", "value2")));
    }

    @Test(expected = RuntimeException.class)
    public void zipMapRequiresAnEvenNumberOfElements() throws Exception {
        zipmap(list(), list("nooope"));
    }

    @Test
    public void zipReturnsAListOfMapEntry() throws Exception {
        assertEquals(list(), zip(list(), list()));
        assertEquals(list(entry("key", "value")), zip(list("key"), list("value")));
        assertEquals(list(entry("key", "value"), entry("key2", "value2")), zip(list("key", "key2"), list("value", "value2")));
    }

    @Test(expected = RuntimeException.class)
    public void zipRequiresAnEvenNumberOfElements() throws Exception {
        zip(list(), list("nooope"));
    }

    @Test
    public void mergeCombinesMaps() throws Exception {
        assertEquals(map(), merge(map()));
        assertEquals(map("a", 1), merge(map("a", 1)));
        assertEquals(map("a", 1, "b", 2), merge(map("a", 1), map("b", 2)));
        assertEquals(map("a", 1, "b", 2, "c", 3), merge(map("a", 1), map("b", 2, "c", 3)));
        assertEquals(map("a", 2), merge(map("a", 1), map("a", 2)));
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
        assertEquals(map("a", 1, "b", 2),
                mapFromPairs(list(list("a", 1), list("b", 2)), (l) -> entry(l.get(0), l.get(1))));
    }

    @Test
    public void smapFromPairsCreatesAStringMapFromPairsWithAFunction() throws Exception {
        assertEquals(smap("a", 1, "b", 2),
                smapFromPairs(list(list("a", 1), list("b", 2)),
                        (l) -> entry((String) l.get(0), l.get(1))));
    }

    @Test
    public void smapFromKeysCreatesAStingMapFromKeysAndAFunction() throws Exception {
        assertEquals(smap("a", 1, "b", 1),
                smapFromKeys(list("a", "b"),
                        (l) -> 1));
    }

    @Test
    public void modifyKeysShouldChangeTheKeysInAMap() throws Exception {
        Map<String, Integer> in = map("wrong", 1, "foo", 1);
        Map<String, Integer> out = map("right", 1, "foo", 1);

        assertEquals(out, modifyKeys(in, (k) -> k.equals("wrong") ? "right" : k));
    }

    @Test
    public void modifyValueShouldChangeTheValuesInAMap() throws Exception {
        Map<String, Integer> in = map("bar", 1, "foo", 1);
        Map<String, Integer> out = map("bar", 2, "foo", 2);

        assertEquals(out, modifyValues(in, (v) -> v + 1));
    }

    @Test
    public void filterKeysShouldReturnAMapWithKeysThatPassPredicate() throws Exception {
        Map<String, Integer> in = map("wrong", 1, "foo", 1);
        Map<String, Integer> out = map("foo", 1);

        assertEquals(out, filterKeys(in, (k) -> k.equals("foo")));
    }

    @Test
    public void groupByCollatesBasedOnAFunction() throws Exception {
        assertEquals(map(String.class, list("a", "b"), Integer.class, list(1, 3, 2)),
                groupBy(list("a", 1, "b", 3, 2), Serializable::getClass));

        assertEquals(map(0, list(2, 4), 1, list(1, 3, 5)),
                groupBy(list(1, 2, 3, 4, 5), ((v) -> v % 2)));
    }

    @Test
    public void bifurcateSplitsACollectionBasedOnAPredicate() throws Exception {
        InOut<Integer> out = bifurcate(list(1, 2, 3, 4), (v) -> v % 2 == 0);
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
                        map("a", 1, "b", 2, "c", 3, "d", 4),
                        (k, v) -> v + 1));
    }

    @Test
    public void mapWorksOnOptionals() throws Exception {
        assertEquals(
                optional(3),
                map(
                        optional(2),
                        (v) -> 1 + v));
    }

    @Test
    public void doTimesRepeatsAFunction() throws Exception {
        One<Integer> one = one(0);
        doTimes(5, () -> one.value += 1);
        assertEquals(5, (int) one.value);
    }

    @Test
    public void doTimesRepeatsAFunctionWithAnIndex() throws Exception {
        One<Integer> one = one(1);
        doTimes(5, (i) -> one.value += i);
        assertEquals(11, (int) one.value);
    }

    @Test
    public void allMatchFirstChecksThatResultsAreTheSame() throws Exception {
        assertTrue(allMatchFirst(list(1), (x) -> x));
        assertTrue(allMatchFirst(list(1, 1), (x) -> x));
        assertFalse(allMatchFirst(list(1, 1, 2), (x) -> x));
    }

    @Test
    public void anyPassesIfAnyValuesInTheCollectionPass() throws Exception {
        assertFalse(any(list(), (x) -> x.equals(2)));
        assertTrue(any(list(1, 1, 2, 1), (x) -> x == 2));
    }

    @Test
    public void allPassesIfAllValuesInTheCollectionPass() throws Exception {
        assertFalse(all(list(), (x) -> x.equals(2)));
        assertFalse(all(list(1, 1, 2, 1), (x) -> x == 2));
        assertTrue(all(list(1, 1, 1), (x) -> x == 1));
    }
}