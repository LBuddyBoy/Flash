package dev.lbuddyboy.flash.handler;

import com.mongodb.client.model.Filters;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.impl.FlatFileUser;
import dev.lbuddyboy.flash.user.impl.MongoUser;
import dev.lbuddyboy.flash.user.impl.RedisUser;
import dev.lbuddyboy.flash.user.listener.*;
import dev.lbuddyboy.flash.user.model.Prefix;
import dev.lbuddyboy.flash.util.YamlDoc;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class UserHandler {

    private final Map<UUID, User> users;
    private YamlDoc usersYML;
    @Setter
    private List<Prefix> prefixes;

    public UserHandler() {
        this.users = new ConcurrentHashMap<>();
        this.prefixes = new ArrayList<>();

        switch (FlashLanguage.CACHE_TYPE.getString().toUpperCase()) {
            case "FLATFILE":
            case "YAML":
                this.usersYML = new YamlDoc(Flash.getInstance().getDataFolder(), "users.yml");
                break;
        }

        for (Document document : Flash.getInstance().getMongoHandler().getPrefixCollection().find()) prefixes.add(GSONUtils.getGSON().fromJson(document.toJson(), GSONUtils.PREFIX));

        Flash.getInstance().getServer().getPluginManager().registerEvents(new FreezeListener(), Flash.getInstance());
        Flash.getInstance().getServer().getPluginManager().registerEvents(new GrantListener(), Flash.getInstance());
        Flash.getInstance().getServer().getPluginManager().registerEvents(new NoteListener(), Flash.getInstance());
        Flash.getInstance().getServer().getPluginManager().registerEvents(new PunishmentListener(), Flash.getInstance());
        Flash.getInstance().getServer().getPluginManager().registerEvents(new UserListener(), Flash.getInstance());

    }

    public User getUser(UUID uuid, boolean searchDb) {
        if (searchDb) {
            if (users.containsKey(uuid))
                return getUser(uuid, false);
            switch (FlashLanguage.CACHE_TYPE.getString().toUpperCase()) {
                case "REDIS":
                    return new RedisUser(uuid, Flash.getInstance().getCacheHandler().getUserCache().getName(uuid), true);
                case "MONGO":
                    return new MongoUser(uuid, Flash.getInstance().getCacheHandler().getUserCache().getName(uuid), true);
                case "FLATFILE":
                case "YAML":
                    return new FlatFileUser(uuid, Flash.getInstance().getCacheHandler().getUserCache().getName(uuid), true);
                default:
                    return null;
            }
        }
        return users.get(uuid);
    }

    public User getUserRank(UUID uuid, boolean searchDb) {
        if (searchDb) {
            if (users.containsKey(uuid))
                return getUser(uuid, false);
            switch (FlashLanguage.CACHE_TYPE.getString().toUpperCase()) {
                case "REDIS": {
                    RedisUser user = new RedisUser(uuid, Flash.getInstance().getCacheHandler().getUserCache().getName(uuid), false);
                    user.loadRank();
                    return user;
                }
                case "MONGO": {
                    MongoUser user = new MongoUser(uuid, Flash.getInstance().getCacheHandler().getUserCache().getName(uuid), false);
                    user.loadRank();
                    return user;
                }
                case "FLATFILE":
                case "YAML": {
                    FlatFileUser user = new FlatFileUser(uuid, Flash.getInstance().getCacheHandler().getUserCache().getName(uuid), false);
                    user.loadRank();
                    return user;
                }
                default:
                    return null;
            }
        }
        return users.get(uuid);
    }

    public User tryUser(UUID uuid, boolean searchDb) {
        try {
            return getUser(uuid, searchDb);
        } catch (Exception ignored) {
            return null;
        }
    }

    public User tryUserRank(UUID uuid, boolean searchDb) {
        try {
            return getUserRank(uuid, searchDb);
        } catch (Exception ignored) {
            return null;
        }
    }

    public User createUser(UUID uuid, String name) {
        switch (FlashLanguage.CACHE_TYPE.getString().toUpperCase()) {
            case "REDIS":
                return new RedisUser(uuid, name, true);
            case "MONGO":
                return new MongoUser(uuid, name, true);
            case "FLATFILE":
            case "YAML":
                return new FlatFileUser(uuid, name, true);
            default:
                return null;
        }
    }

    public void deleteUser(UUID uuid) {
        User user = tryUser(uuid, true);
        if (user == null) return;

        users.remove(uuid);
        switch (FlashLanguage.CACHE_TYPE.getString().toUpperCase()) {
            case "REDIS":
                RedisHandler.requestJedis().getResource().hdel("Users", uuid.toString());
            case "MONGO":
                Flash.getInstance().getMongoHandler().getUserCollection().deleteOne(Filters.eq("uuid", uuid.toString()));
            case "FLATFILE":
            case "YAML":
                // NOT SUPPORTED
            default:
                // NOT SUPPORTED
        }
    }

    public List<UUID> relativeAlts(String ip) {
        List<UUID> uuids = new ArrayList<>();

        switch (FlashLanguage.CACHE_TYPE.getString().toUpperCase()) {
            case "REDIS":
                JedisPool pool = RedisHandler.requestJedis();
                for (Map.Entry<String, String> entry : pool.getResource().hgetAll("Users").entrySet()) {
                    RedisUser user = new RedisUser(UUID.fromString(entry.getKey()), null, true);
                    if (!user.getIp().equals(ip)) continue;

                    uuids.add(user.getUuid());
                }

                break;
            case "MONGO":
                for (Document document : Flash.getInstance().getMongoHandler().getUserCollection().find()) {
                    try {
                        if (!document.containsKey("ip")) continue;
                        String altIP = document.getString("ip");
                        if (altIP == null) continue;
                        if (!altIP.equals(ip)) continue;

                        uuids.add(UUID.fromString(document.getString("uuid")));
                    } catch (Exception ignored) {

                    }
                }
                break;
            case "FLATFILE":
            case "YAML":
                for (String key : this.usersYML.gc().getConfigurationSection("users").getKeys(false)) {
                    String altIP = this.usersYML.gc().getString("users." + key + ".ip");
                    if (!ip.equals(altIP)) continue;

                    uuids.add(UUID.fromString(key));
                }
                break;
        }
        return uuids;
    }

    public Prefix getPrefix(String lookUp) {
        for (Prefix prefix : this.prefixes) {
            if (prefix.getId().equals(lookUp)) return prefix;
        }
        return null;
    }

    public Map<Long, Rank> getRankConversion() {
        Map<Long, Rank> conversion = new HashMap<>();
        for (String key : FlashLanguage.SYNC_CONVERSION.getStringList()) {
            String[] args = key.split(":");
            conversion.put(Long.valueOf(args[0]), Flash.getInstance().getRankHandler().getRank(args[1]));
        }
        return conversion;
    }

}
