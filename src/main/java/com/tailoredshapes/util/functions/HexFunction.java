package com.tailoredshapes.util.functions;

public interface HexFunction<T, U, V, W, X, Y, R> {
    R apply(T t, U u, V v, W w, X x, Y y);
}
