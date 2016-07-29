package com.tailoredshapes.underbar;

import com.tailoredshapes.stash.Stash;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;

import static com.tailoredshapes.underbar.Die.rethrow;
import static com.tailoredshapes.underbar.UnderBar.optionally;

public class IO {

    public static ByteArrayInputStream stringInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }

    public static ByteArrayInputStream stringInputStream(String s, String encoding) {
        return rethrow(() -> new ByteArrayInputStream(s.getBytes(encoding)));
    }

    public static void close(Closeable stream) {
        rethrow(stream::close);
    }

    public static Date lastModifiedDate(File file) {
        long l = file.lastModified() / 1000;
        return new Date(l * 1000);
    }

    public static BufferedWriter writer(OutputStream outputStream, Stash opts) {
        Charset encoding = optionally(opts.optional("encoding"), Charset::forName, () -> Charset.forName("UTF-8"));

        return new BufferedWriter(rethrow(() -> new OutputStreamWriter(outputStream, encoding)));
    }

    public static BufferedWriter writer(OutputStream outputStream) {
        return new BufferedWriter(new PrintWriter(outputStream));
    }

    public static BufferedWriter responseWriter(OutputStream outputStream, Stash opts) {
        return optionally(
                opts.maybe("encoding"),
                (charset) -> writer(outputStream, opts),
                () -> writer(outputStream));
    }
}
