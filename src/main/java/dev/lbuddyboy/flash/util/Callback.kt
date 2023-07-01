package dev.lbuddyboy.flash.util

interface Callback<T, O> {
    fun call(t: T, t2: O)
    interface SingleCallback<T> {
        fun call(t: T)
    }

    interface TripleCallback<T, V, O> {
        fun call(t: T, v: V, o: O)
    }
}