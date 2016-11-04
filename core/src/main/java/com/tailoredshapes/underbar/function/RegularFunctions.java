package com.tailoredshapes.underbar.function;

/**
 * Created by tmarsh on 11/3/16.
 */
public interface RegularFunctions {
    @FunctionalInterface
    public static interface SeptaConsumer<S , T, U, V, X, Y, Z> {
        void accept(S s, T t, U u, V v, X x, Y y, Z z);
    }

    @FunctionalInterface
    public static interface SeptaFunction<T, U, V, W, X, Y, Z, R> {
        R apply(T t, U u, V v, W w, X x, Y y, Z z);
    }

    @FunctionalInterface
    public static interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }

    @FunctionalInterface
    public static interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    @FunctionalInterface
    public static interface QuadFunction<T, U, V, W, R> {
        R apply(T t, U u, V v, W w);
    }

    @FunctionalInterface
    public static interface HexFunction<T, U, V, W, X, Y, R> {
        R apply(T t, U u, V v, W w, X x, Y y);
    }

    @FunctionalInterface
    public static interface QuadConsumer<T, U, V, X> {
        void accept(T t, U u, V v, X x);
    }

    @FunctionalInterface
    public static interface HexConsumer<T, U, V, X, Y, Z> {
        void accept(T t, U u, V v, X x, Y y, Z z);
    }

    @FunctionalInterface
    public static interface OctaConsumer<R, S , T, U, V, X, Y, Z> {
        void accept(R r, S s, T t, U u, V v, X x, Y y, Z z);
    }

    @FunctionalInterface
    public static interface OctaFunction<T, U, V, W, X, Y, Z, A, R> {
        R apply(T t, U u, V v, W w, X x, Y y, Z z, A a);
    }

    @FunctionalInterface
    public static interface PentaFunction<T, U, V, W, X, R> {
        R apply(T t, U u, V v, W w, X x);
    }

    @FunctionalInterface
    public static interface PentaConsumer<T, U, V, X, Y> {
        void accept(T t, U u, V v, X x, Y y);
    }
}
