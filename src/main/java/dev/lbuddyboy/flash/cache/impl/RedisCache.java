package dev.lbuddyboy.flash.cache.impl;

import dev.lbuddyboy.flash.cache.UserCache;
import dev.lbuddyboy.flash.handler.RedisHandler;
import dev.lbuddyboy.flash.util.CC;
import dev.lbuddyboy.flash.util.Tasks;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RedisCache extends UserCache {

    private static final String KEY = "CACHE";

    private static final Map<UUID, String> uuidNameMap = new ConcurrentHashMap<>();
    private static final Map<String, UUID> nameUUIDMap = new ConcurrentHashMap<>();

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
        Map<String, String> cache = RedisHandler.requestJedis().getResource().hgetAll(KEY);
        int i = 0;
        for (Map.Entry<String, String> cacheEntry : cache.entrySet()) {
            UUID uuid = UUID.fromString(cacheEntry.getKey());
            String name = cacheEntry.getValue();

            uuidNameMap.put(uuid, name);
            nameUUIDMap.put(name, uuid);
            i++;
        }

        Bukkit.getConsoleSender().sendMessage(CC.translate("&fInitiated the &bRedis Cache&f."));
        if (i > 0) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&fCached &b" + i + "&f names & uuids."));
        }
    }

    @Override
    public void update(UUID uuid, String name) {
        Tasks.runAsync(() -> RedisHandler.requestJedis().getResource().hset(KEY, uuid.toString(), name));
    }

}