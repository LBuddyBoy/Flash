package dev.lbuddyboy.flash.util.bukkit

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.util.Callable
import org.bukkit.Bukkit

object Tasks {
    fun run(callable: Callable) {
        Bukkit.getServer().scheduler.runTask(Flash.instance) { callable.call() }
    }

    fun runAsync(callable: Callable) {
        Bukkit.getServer().scheduler.runTaskAsynchronously(Flash.instance) { callable.call() }
    }

    fun runLater(callable: Callable, delay: Long) {
        Bukkit.getServer().scheduler.runTaskLater(Flash.instance, { callable.call() }, delay)
    }

    fun runAsyncLater(callable: Callable, delay: Long) {
        Bukkit.getServer().scheduler.runTaskLaterAsynchronously(Flash.instance, { callable.call() }, delay)
    }

    fun runTimer(callable: Callable, delay: Long, interval: Long) {
        Bukkit.getServer().scheduler.runTaskTimer(Flash.instance, { callable.call() }, delay, interval)
    }

    fun runAsyncTimer(callable: Callable, delay: Long, interval: Long) {
        Bukkit.getServer().scheduler.runTaskTimerAsynchronously(
            Flash.instance,
            { callable.call() },
            delay,
            interval
        )
    }
}