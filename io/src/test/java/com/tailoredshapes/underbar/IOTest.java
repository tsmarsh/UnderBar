package com.tailoredshapes.underbar;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Writer;
import java.util.Date;

import static com.tailoredshapes.stash.Stash.stash;
import static com.tailoredshapes.underbar.IO.*;
import static org.junit.Assert.assertEquals;

public class IOTest {


    @Test
    public void easesBuildingAWriter() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try(Writer writer = responseWriter(outputStream, stash())){
            writer.write("Hello, World!");
        }

        close(outputStream);
        assertEquals("Hello, World!", outputStream.toString("UTF-8"));
    }

    @Test
    public void canTakeACharSetFromAStash() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try(BufferedWriter writer = responseWriter(outputStream, stash("encoding", "UTF-8"))){
            writer.write("Hello, World!");
        }

        close(outputStream);
        assertEquals("Hello, World!", outputStream.toString("UTF-8"));
    }

    @Test
    public void canGetLastModified() throws Exception {
        File file = new File(String.join(File.separator, "src", "test", "resources", "test.txt"));
        assertEquals(new Date(file.lastModified()), lastModifiedDate(file));
    }

    @Test
    public void slurpCanReadAFile() throws Exception {
        assertEquals("Hello, World!", slurp(new File(String.join(File.separator, ".", "src", "test", "resources", "test.txt"))));
    }


    @Test
    public void slurpCanReadAnInputStream() throws Exception {
        assertEquals("Hello, World!", slurp(stringInputStream("Hello, World!")));
        assertEquals("Hello, World!", slurp(stringInputStream("Hello, World!", "UTF-8")));
    }


}