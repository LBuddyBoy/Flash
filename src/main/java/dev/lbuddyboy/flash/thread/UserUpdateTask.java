package dev.lbuddyboy.flash.thread;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class UserUpdateTask extends Thread {

    @Override
    public void run() {
        for (User user : Flash.getInstance().getUserHandler().getUsers().values()) {
            user.updatePerms();
            user.updateGrants();
        }

        try {
            sleep(10_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
