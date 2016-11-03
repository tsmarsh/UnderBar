package com.tailoredshapes.underbar.function;

@FunctionalInterface
public interface SeptaConsumer<S , T, U, V, X, Y, Z> {
    void accept(S s, T t, U u, V v, X x, Y y, Z z);
}
