package com.tailoredshapes.util;

import org.joda.time.DateTime;

public class Dates {
    public static DateTime date(String string) {
        return new DateTime(string);
    }

    public static String isoString(DateTime time){
        return time.toDateTimeISO().toString();
    }
}
