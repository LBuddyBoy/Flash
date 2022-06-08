package dev.lbuddyboy.flash.thread;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.impl.RedisUser;
import dev.lbuddyboy.flash.util.Batch;
import dev.lbuddyboy.flash.util.Callable;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class UserUpdateThread extends Thread {

    @Override
    public void run() {
        while (true) {

            for (User user : Flash.getInstance().getUserHandler().getUsers().values()) {
                user.updatePerms();
                user.updateGrants();
            }

            try {
                Thread.sleep(15_000L);
            } catch (InterruptedException ignored) {}

        }
    }
}
