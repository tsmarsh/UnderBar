package com.tailoredshapes.underbar;

import org.junit.Test;

import static com.tailoredshapes.underbar.IO.*;
import static org.junit.Assert.assertEquals;

public class IOTest {


    @Test
    public void slurpCanReadAFile() throws Exception {
        assertEquals("Hello, World!", slurp(file(resource("/test.txt"))));
    }


    @Test
    public void slurpCanReadAnInputStream() throws Exception {
        assertEquals("Hello, World!", slurp(stringInputStream("Hello, World!")));
    }

}