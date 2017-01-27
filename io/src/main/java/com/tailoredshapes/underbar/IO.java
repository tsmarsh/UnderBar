package com.tailoredshapes.underbar;

import com.tailoredshapes.stash.Stash;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Optional;

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
        Optional<String> enc = opts.maybe("encoding");
        Charset encoding = optionally(enc, (String s) -> Charset.forName(s), () -> Charset.forName("UTF-8"));

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

    public static URL resource(String path) {
        return Object.class.getResource(path);
    }

    public static File file(URL url) {
        return new File(rethrow(url::toURI));
    }

    public static BufferedReader reader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    public static BufferedReader reader(File f) {
        return new BufferedReader(rethrow(() -> new FileReader(f)));
    }

    public static String slurp(BufferedReader buffy) {
        StringBuilder bob = new StringBuilder();
        String line;
        do {
            line = rethrow(buffy::readLine);
            if (line != null) bob.append(line);
        } while (line != null);

        return bob.toString();
    }

    public static String slurp(File file) {
        return slurp(reader(file));
    }

    public static String slurp(InputStream is) {
        return slurp(reader(is));
    }
}
