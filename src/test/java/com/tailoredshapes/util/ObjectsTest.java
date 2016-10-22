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
        Integer expected = 5;
        List<Integer> it = new ArrayList<>();
        it.add(expected);
        assertEquals(Optional.of(5), maybe(it));;
    }

    @Test
    public void maybeReturnsEmptyIfListIsEmpty() throws Exception {
        assertFalse(maybe(Collections.emptyList()).isPresent());
    }

    @Test(expected = RuntimeException.class)
    public void maybeBombsIfListIsLargerThan1() throws Exception {
        maybe(Arrays.asList(1, 2));
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
    
}