package com.tailoredshapes.underbar.function;

/**
 * Created by tmarsh on 11/3/16.
 */
public class ExceptionalFunctions {
    @FunctionalInterface
    public interface RunnableWithOops<E extends Throwable> {
        void run() throws E;
    }

    @FunctionalInterface
    public interface SupplierWithOops<T> {
        T get() throws Throwable;
    }

}
