package dev.lbuddyboy.flash.cache;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class UserCache {

    public abstract UUID getUUID(String name);
    public abstract String getName(UUID uuid);
    public abstract void load();

    public abstract void update(UUID uuid, String name);

}
