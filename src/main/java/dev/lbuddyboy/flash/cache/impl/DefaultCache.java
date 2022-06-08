package dev.lbuddyboy.flash.cache.impl;

import dev.lbuddyboy.flash.cache.UserCache;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.Bukkit;

import java.util.UUID;

public class DefaultCache extends UserCache {

    @Override
    public void load() {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&fInitiated the &aDefault Cache&f."));
    }

    @Override
    public String getName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    @Override
    public UUID getUUID(String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId();
    }

    @Override
    public void update(UUID uuid, String name) {
    }

}
