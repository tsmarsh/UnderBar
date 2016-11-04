package com.tailoredshapes.underbar;

import java.util.List;

/**
 * Created by tmarsh on 11/3/16.
 */
public class Fork<T> {
    public final List<T> in;
    public final List<T> out;

    public Fork(List<T> in, List<T> out) {
        this.in = in;
        this.out = out;
    }
}
