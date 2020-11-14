package com.tailoredshapes.underbar.ocho;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tmarsh on 11/2/16.
 */
public interface UnderReg {
    static Pattern pattern(String s) {
        return Pattern.compile(s);
    }

    static Matcher matcher(Pattern re, CharSequence s) {
        return re.matcher(s);
    }

    static List<String> groups(Matcher m) {
        List<String> l = new ArrayList<>();
        if (!m.matches()) {
            return l;
        }

        int groupCount = m.groupCount();

        for (int c = 1; c <= groupCount; c++) {
            String group = m.group(c);
            l.add(group);
        }

        return l;
    }
}
