package com.tailoredshapes.underbar.dates;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public interface Dates {

    static Date date(Instant instant) {
        return Date.from(instant);
    }

    static Date date(String string) {
        TemporalAccessor parse = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC).parse(string);
        return date(Instant.from(parse));
    }

    static Instant instant(String string) {
        return Instant.from(ISO_INSTANT.parse(string));
    }

    static String isoString(Date time) {
        return ISO_INSTANT.format(time.toInstant());
    }

    static String isoString(Instant instant) {
        return ISO_INSTANT.format(instant);
    }
}
