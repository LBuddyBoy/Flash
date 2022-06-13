package dev.lbuddyboy.flash.cache.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.cache.UserCache;
import dev.lbuddyboy.flash.cache.packet.CacheDistributePacket;
import dev.lbuddyboy.flash.handler.RedisHandler;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import org.bson.Document;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MongoCache extends UserCache {

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
    public List<UUID> allUUIDs() {
        return new ArrayList<>(nameUUIDMap.values());
    }

    @Override
    public void load() {

        int i = 0;
        for (Document document : Flash.getInstance().getMongoHandler().getCacheCollection().find()) {
            UUID uuid = UUID.fromString(document.getString("uuid"));
            String name = document.getString("name");

            try {
                uuidNameMap.put(uuid, name);
                nameUUIDMap.put(name, uuid);
            } catch (Exception ignored) {}
            ++i;
        }

        Bukkit.getConsoleSender().sendMessage(CC.translate("&fInitiated the &eMongo Cache&f."));
        if (i > 0) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&fCached &b" + i + "&f names & uuids."));
        }
    }

    @Override
    public void update(UUID uuid, String name, boolean save) {
        if (save) {
            if (Flash.getInstance().getMongoHandler().getCacheCollection().find(Filters.eq("uuid", uuid.toString())).first() == null || Flash.getInstance().getMongoHandler().getCacheCollection().find(Filters.eq("name", name)).first() == null) {
                Document document = new Document();

                document.put("uuid", uuid.toString());
                document.put("name", name);

                Flash.getInstance().getMongoHandler().getCacheCollection().replaceOne(Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
            }
            new CacheDistributePacket(uuid, name).send();
        }

        uuidNameMap.put(uuid, name);
        nameUUIDMap.put(name, uuid);
    }

}
