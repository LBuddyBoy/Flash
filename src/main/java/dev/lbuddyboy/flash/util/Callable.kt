package dev.lbuddyboy.flash.util

interface Callable {
    fun call()

    companion object {
        const val sent = false
    }
}