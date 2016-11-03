package com.tailoredshapes.underbar;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tailoredshapes.underbar.Die.rethrow;


public class UnderString {
    public static String reverse(CharSequence s) {
        return new StringBuilder(s).reverse().toString();
    }

    public static String reQuoteReplacement(CharSequence replacement){
        return Matcher.quoteReplacement(replacement.toString());
    }

    public static <T> String commaSep(List<T> list) {
        return Joiner.on(",").join(list);
    }


    public static <T> String join(String separator, Map<String, T> m, BiFunction<String, T, String> f) {
        return Joiner.on(separator).join(UnderBar.map(m, f));
    }

    public static <V> String toString(V v) {
        return v.toString();
    }

    public static String urlEncode(String s) {
        return rethrow(() -> URLEncoder.encode(s, "UTF-8"));
    }

    public static boolean hasContent(String s) {
        return s.trim().length() > 0;
    }

}