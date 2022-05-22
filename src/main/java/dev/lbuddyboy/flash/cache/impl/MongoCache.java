package dev.lbuddyboy.flash.cache.impl;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.cache.UserCache;
import dev.lbuddyboy.flash.util.CC;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MongoCache extends UserCache {

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
    public void load() {

        int i = 0;
        for (Document document : Flash.getInstance().getMongoHandler().getUserCollection().find()) {
            UUID uuid = UUID.fromString(document.getString("uuid"));
            String name = document.getString("name");

            uuidNameMap.put(uuid, name);
            nameUUIDMap.put(name, uuid);
            ++i;
        }

        Bukkit.getConsoleSender().sendMessage(CC.translate("&fInitiated the &eMongo Cache&f."));
        if (i > 0) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&fCached &b" + i + "&f names & uuids."));
        }
    }

    @Override
    public void update(UUID uuid, String name) {
        // User updates will already handle this.
    }

}
