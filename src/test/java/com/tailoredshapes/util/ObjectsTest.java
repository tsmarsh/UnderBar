package com.tailoredshapes.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    
}