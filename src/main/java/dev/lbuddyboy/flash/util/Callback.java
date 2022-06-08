package dev.lbuddyboy.flash.util;

public interface Callback<T, O> {
    void call(T t, O t2);

    public static interface SingleCallback<T> {
        void call(T t);
    }
    public static interface TripleCallback<T, V, O> {
        void call(T t, V v, O o);
    }
}
