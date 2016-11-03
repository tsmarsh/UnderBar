package com.tailoredshapes.underbar.function;

public interface ExceptionalFunctions {

    @FunctionalInterface
    interface RunnableThatMight<E extends Throwable> {
        void run() throws E;
    }

    @FunctionalInterface
    interface RunnableWithOops extends RunnableThatMight<Throwable> {
        void run() throws Throwable;
    }

    @FunctionalInterface
    interface SupplierThatMight<T, E extends Throwable> {
        T get() throws E;
    }

    @FunctionalInterface
    interface SupplierWithOops<T> extends SupplierThatMight<T, Throwable>{
        T get() throws Throwable;
    }
}
