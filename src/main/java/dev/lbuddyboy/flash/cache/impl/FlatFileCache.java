package dev.lbuddyboy.flash.cache.impl;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.cache.UserCache;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FlatFileCache extends UserCache {

    public Map<UUID, String> uuidNameMap = new ConcurrentHashMap<>();
    public Map<String, UUID> nameUUIDMap = new ConcurrentHashMap<>();

    @Override
    public String getName(UUID uuid) {
        return uuidNameMap.get(uuid);
    }

    @Override
    public UUID getUUID(String name) {
        return nameUUIDMap.get(name);
    }

    @Override
    public List<UUID> allUUIDs() {
        return new ArrayList<>(nameUUIDMap.values());
    }

    @Override
    public void load() {

        int i = 0;
        try {
            for (String key : Flash.getInstance().getUserHandler().getUsersYML().gc().getConfigurationSection("profiles").getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                String name = Flash.getInstance().getUserHandler().getUsersYML().gc().getString("profiles." + key + ".name");

                uuidNameMap.put(uuid, name);
                nameUUIDMap.put(name, uuid);
                ++i;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bukkit.getConsoleSender().sendMessage(CC.translate("&fInitiated the &aFlatFile Cache&f."));
        if (i > 0) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&fCached &b" + i + "&f names & uuids."));
        }
    }

    @Override
    public void update(UUID uuid, String name) {
        // User updates will already handle this.

        uuidNameMap.put(uuid, name);
        nameUUIDMap.put(name, uuid);
    }

}
