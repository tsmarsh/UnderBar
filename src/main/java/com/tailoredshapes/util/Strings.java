package com.tailoredshapes.util;

import com.google.common.base.Joiner;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class Strings {
    public static <T> String commaSep(List<T> list) {
        return Joiner.on(",").join(list);
    }


    public static <T> String join(String separator, Map<String, T> m, BiFunction<String, T, String> f) {
        return Joiner.on(separator).join(UnderBar.map(m, f));
    }

    public static <V> String toString(V v) {
        return v.toString();
    }

    public static String urlEncode(String s){
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Optional<String> optional(String value) {
        if (value != null){
            return Optional.of(value);
        }
        return Optional.empty();
    }

    public static boolean hasContent(String s) {
        return s.trim().length() > 0;
    }
}