package dev.lbuddyboy.flash.util;

import dev.lbuddyboy.flash.Flash;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Tasks {

    public static void run(Callable callable) {
        Bukkit.getServer().getScheduler().runTask(Flash.getInstance(), callable::call);
    }

    public static void runAsync(Callable callable) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Flash.getInstance(), callable::call);
    }

    public static void runLater(Callable callable, long delay) {

        Bukkit.getServer().getScheduler().runTaskLater(Flash.getInstance(), callable::call, delay);
    }

    public static BukkitTask runAsyncLater(Callable callable, long delay) {
        return Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(Flash.getInstance(), callable::call, delay);
    }

    public static void runTimer(Callable callable, long delay, long interval) {

        Bukkit.getServer().getScheduler().runTaskTimer(Flash.getInstance(), callable::call, delay, interval);
    }

    public static void runAsyncTimer(Callable callable, long delay, long interval) {

        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Flash.getInstance(), callable::call, delay, interval);
    }

    public interface Callable {
        void call();
    }
}
