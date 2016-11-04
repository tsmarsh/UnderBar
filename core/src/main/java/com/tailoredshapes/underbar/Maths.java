package com.tailoredshapes.underbar;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.tailoredshapes.underbar.UnderBar.*;

/**
 * Created by tmarsh on 11/3/16.
 */
public class Maths {
    public static boolean xor(boolean a, boolean b) {
        return (a || b) && !(a && b);
    }

    public static double average(Collection<Long> values) {
        if (isEmpty(values))
            return Double.NaN;
        return ((double) sum(values)) / values.size();
    }

    public static double averageInt(Collection<Integer> values) {
        if (isEmpty(values))
            return Double.NaN;
        return ((double) sumInt(values)) / values.size();
    }

    public static double averageDouble(Collection<Double> values) {
        if (isEmpty(values))
            return Double.NaN;
        return sumDouble(values) / values.size();
    }

    public static long sum(Collection<Long> values) {
        long result = 0;
        for (long v : values)
            result += v;
        return result;
    }

    public static long sumInt(Collection<Integer> values) {
        long result = 0;
        for (long v : values)
            result += v;
        return result;
    }

    public static double sumDouble(Collection<Double> values) {
        double result = 0;
        for (double v : values)
            result += v;
        return result;
    }

    public static Long min(Collection<Long> values) {
        if (isEmpty(values))
            return null;
        long result = first(values);
        for (long v : values)
            result = Math.min(result, v);
        return result;
    }

    public static Long max(Collection<Long> values) {
        if (isEmpty(values))
            return null;
        long result = first(values);
        for (long v : values)
            result = Math.max(result, v);
        return result;
    }

    public static double medianInt(List<Integer> values) {
        if (isEmpty(values))
            return Double.NaN;
        int count = values.size();
        int half = count / 2;
        return count % 2 == 1
                ? values.get(half).doubleValue()
                : (values.get(half - 1) + values.get(half)) / 2.0;
    }

    public static double median(List<Long> values) {
        if (isEmpty(values))
            return Double.NaN;
        int count = values.size();
        int half = count / 2;
        return count % 2 == 1
                ? values.get(half).doubleValue()
                : (values.get(half - 1) + values.get(half)) / 2.0;
    }
}
