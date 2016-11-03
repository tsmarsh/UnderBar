package com.tailoredshapes.underbar.function;

@FunctionalInterface
public interface PentaConsumer<T, U, V, X, Y> {
    void accept(T t, U u, V v, X x, Y y);
}
