package com.tailoredshapes.underbar.ocho;

import org.junit.Test;

import java.util.regex.*;

import static com.tailoredshapes.underbar.ocho.UnderBar.list;
import static com.tailoredshapes.underbar.ocho.UnderReg.*;
import static org.junit.Assert.*;

/**
 * Created by tmarsh on 11/3/16.
 */
public class UnderRegTest {
    @Test
    public void fundamentals() throws Exception {
        Pattern pattern = pattern("(\\d)(\\d)(\\d)");
        Matcher abc = matcher(pattern, "123");

        assertEquals(list("1", "2", "3"), groups(abc));
    }

}