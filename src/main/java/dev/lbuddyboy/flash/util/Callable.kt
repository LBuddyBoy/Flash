package dev.lbuddyboy.flash.util;

public interface Callable {

    boolean sent = false;

    void call();
}