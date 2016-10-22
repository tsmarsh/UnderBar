package com.tailoredshapes.util.function;

public interface QuadFunction<T, U, V, W, R> {
    R apply(T t, U u, V v, W w);
}
