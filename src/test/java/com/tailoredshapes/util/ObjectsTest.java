package com.tailoredshapes.util;

import org.junit.Test;

import java.util.*;

import static com.tailoredshapes.util.Objects.*;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void theReturnsTheSoleValueFromAnIterable() throws Exception {
        Integer expected = 5;
        List<Integer> it = new ArrayList<>();
        it.add(expected);
        Integer result = the(it);
        assertEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void theBombsIfListIsEmpty() throws Exception {
        the(Collections.emptyList());
    }

    @Test(expected = RuntimeException.class)
    public void theBombsIfListIsLargerThan1() throws Exception {
        the(Arrays.asList(1, 2));
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
        assertEquals(Optional.of(5), maybe(list(5)));;
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

        ifAbsent(Optional.empty(), ()-> empty.value = true);
        ifAbsent(Optional.of(1), ()-> full.value = true);

        assertTrue(empty.value);
        assertFalse(full.value);
    }

    @Test
    public void optionallyConsumesThenCallsCallbackIfEmpty() throws Exception {
        One<Boolean> calledBack = one(false);
        One<Boolean> consumed = one(false);

        optionally(Optional.of(true), (t)->consumed.value = t, () -> calledBack.value=true);

        assertFalse(calledBack.value);
        assertTrue(consumed.value);
    }

    @Test
    public void optionallyCallsCallbackIfEmpty() throws Exception {
        One<Boolean> calledBack = one(false);
        One<Boolean> consumed = one(false);

        optionally(Optional.empty(), (t)-> consumed.value = (Boolean) t, () -> calledBack.value=true);

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
        assertEquals(list(1, 2, 3, 4), flatten(list(list(1), list(2,3), list(4))));
    }

    @Test
    public void rejectBasedOnAPredicate() throws Exception {
        assertEquals(list(), reject(list(), (x) -> !x.equals(1)));
        assertEquals(list(), reject(list(2), (x) -> !x.equals(1)));
        assertEquals(list(1), reject(list(1,2,3), (x) -> x != 1));
    }

    @Test
    public void negateInvertsAPredicate() throws Exception {
        assertEquals(list(), reject(list(), negate((x) -> !x.equals(1))));
        assertEquals(list(2), reject(list(2), negate((x) -> !x.equals(1))));
        assertEquals(list(2,3), reject(list(1,2,3), negate((x) -> x != 1)));
    }

    @Test
    public void compactRemovesNulls() throws Exception {
        assertEquals(list(1, 2, 3), compact(list(1, null, 2, null, null, 3)));
    }

    @Test
    public void compactOptionalsRemovesEmpties() throws Exception {
        assertEquals(list(1,2,3),
                compactOptionals(list(optional(1), optional(), optional(2), optional(), optional(), optional(3))));
    }

    @Test
    public void countShouldCount() throws Exception {
        assertEquals(0, count(list(), (x) -> x.equals(1)));
        assertEquals(1, count(list(1), (x) -> x.equals(1)));
        assertEquals(3, count(list(1,1,2,1), (x) -> x.equals(1)));
    }

    @Test
    public void mapFromPairsCreatesAMapFromPairsWithAFunction() throws Exception {
        mapFromPairs(array(list("a", 1), list("b", 2)), (l) -> entry(l.get(0), l.get(1)));
    }
}