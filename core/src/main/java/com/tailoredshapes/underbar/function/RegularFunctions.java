package com.tailoredshapes.underbar.function;

/**
 * Created by tmarsh on 11/3/16.
 */
public interface RegularFunctions {
    @FunctionalInterface
    interface SeptaConsumer<S, T, U, V, X, Y, Z> {
        void accept(S s, T t, U u, V v, X x, Y y, Z z);
    }

    @FunctionalInterface
    interface SeptaFunction<T, U, V, W, X, Y, Z, R> {
        R apply(T t, U u, V v, W w, X x, Y y, Z z);
    }

    @FunctionalInterface
    interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }

    @FunctionalInterface
    interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    @FunctionalInterface
    interface QuadFunction<T, U, V, W, R> {
        R apply(T t, U u, V v, W w);
    }

    @FunctionalInterface
    interface HexFunction<T, U, V, W, X, Y, R> {
        R apply(T t, U u, V v, W w, X x, Y y);
    }

    @FunctionalInterface
    interface QuadConsumer<T, U, V, X> {
        void accept(T t, U u, V v, X x);
    }

    @FunctionalInterface
    interface HexConsumer<T, U, V, X, Y, Z> {
        void accept(T t, U u, V v, X x, Y y, Z z);
    }

    @FunctionalInterface
    interface OctaConsumer<R, S, T, U, V, X, Y, Z> {
        void accept(R r, S s, T t, U u, V v, X x, Y y, Z z);
    }

    @FunctionalInterface
    interface OctaFunction<T, U, V, W, X, Y, Z, A, R> {
        R apply(T t, U u, V v, W w, X x, Y y, Z z, A a);
    }

    @FunctionalInterface
    interface PentaFunction<T, U, V, W, X, R> {
        R apply(T t, U u, V v, W w, X x);
    }

    @FunctionalInterface
    interface PentaConsumer<T, U, V, X, Y> {
        void accept(T t, U u, V v, X x, Y y);
    }
}
