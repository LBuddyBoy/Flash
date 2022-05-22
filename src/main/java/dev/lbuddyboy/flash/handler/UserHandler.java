package dev.lbuddyboy.flash.handler;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.cache.impl.DefaultCache;
import dev.lbuddyboy.flash.cache.impl.FlatFileCache;
import dev.lbuddyboy.flash.cache.impl.MongoCache;
import dev.lbuddyboy.flash.cache.impl.RedisCache;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.impl.FlatFileUser;
import dev.lbuddyboy.flash.user.impl.MongoUser;
import dev.lbuddyboy.flash.user.impl.RedisUser;
import dev.lbuddyboy.flash.user.listener.GrantListener;
import dev.lbuddyboy.flash.user.listener.UserListener;
import dev.lbuddyboy.flash.util.Tasks;
import dev.lbuddyboy.flash.util.YamlDoc;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class UserHandler {

    private final Map<UUID, User> users;
    private YamlDoc usersYML;

    public UserHandler() {
        this.users = new ConcurrentHashMap<>();

        switch (FlashLanguage.CACHE_TYPE.getString().toUpperCase()) {
            case "FLATFILE":
            case "YAML":
                this.usersYML = new YamlDoc(Flash.getInstance().getDataFolder(), "users.yml");
                break;
        }

        Tasks.runAsyncTimer(() -> {
            for (User user : users.values()) {
                user.updatePerms();
                user.updateGrants();
            }
        }, 20 * 5, 20 * 5);

        Flash.getInstance().getServer().getPluginManager().registerEvents(new GrantListener(), Flash.getInstance());
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

}
