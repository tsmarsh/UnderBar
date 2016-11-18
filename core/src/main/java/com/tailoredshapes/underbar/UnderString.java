package com.tailoredshapes.underbar;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import static com.tailoredshapes.underbar.Die.rethrow;
import static com.tailoredshapes.underbar.UnderBar.hash;
import static com.tailoredshapes.underbar.UnderBar.map;


public class UnderString {
    public static String reverse(CharSequence s) {
        return new StringBuilder(s).reverse().toString();
    }

    public static String reQuoteReplacement(CharSequence replacement) {
        return Matcher.quoteReplacement(replacement.toString());
    }

    public static <T> String commaSep(List<T> list) {
        return join(",", list);
    }

    public static String join(Iterable<String> coll) {
        StringBuilder sb = new StringBuilder();
        hash(coll, sb::append);
        return sb.toString();
    }

    public static String join(Object... os){
        return join(map(os, o->o.toString()));
    }
    public static <T> String join(String separator, Iterable<T> coll) {
        Iterator<T> iterator = coll.iterator();

        StringBuilder sb = new StringBuilder(iterator.next().toString());

        while (iterator.hasNext()) {
            sb.append(separator).append(iterator.next());
        }

        return sb.toString();
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