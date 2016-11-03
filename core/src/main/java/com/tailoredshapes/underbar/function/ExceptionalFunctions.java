package com.tailoredshapes.underbar.function;

/**
 * Created by tmarsh on 11/3/16.
 */
public interface ExceptionalFunctions {

    @FunctionalInterface
    public interface RunnableWithOops<E extends Throwable> {
        void run() throws Throwable;
    }

    public interface RunnableThatMight<E extends Throwable> {
        void run() throws E;
    }

    @FunctionalInterface
    public interface SupplierWithOops<T> {
        T get() throws Throwable;
    }


    @FunctionalInterface
    public interface SupplierWThatMight<T, E extends Throwable> {
        T get() throws E;
    }

}
