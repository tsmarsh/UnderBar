package com.tailoredshapes.underbar;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tailoredshapes.underbar.UnderBar.list;

/**
 * Created by tmarsh on 11/2/16.
 */
public class UnderReg {
    public static Pattern pattern(String s) {
        return Pattern.compile(s);
    }

    public static Matcher matcher(Pattern re, CharSequence s) {
        return re.matcher(s);
    }

    public static List<String> groups(Matcher m) {
        List<String> l = list();
        int groupCount = m.groupCount();

        for (int c = 0; c < groupCount; c++) {
            l.add(m.group(c));
        }

        return l;
    }
}
