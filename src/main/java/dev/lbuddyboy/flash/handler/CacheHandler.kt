package dev.lbuddyboy.flash.handler;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.cache.UserCache;
import dev.lbuddyboy.flash.cache.impl.DefaultCache;
import dev.lbuddyboy.flash.cache.impl.FlatFileCache;
import dev.lbuddyboy.flash.cache.impl.MongoCache;
import dev.lbuddyboy.flash.cache.impl.RedisCache;
import dev.lbuddyboy.flash.cache.listener.CacheListener;
import lombok.Getter;

@Getter
public class CacheHandler {

    private final UserCache userCache;

    public CacheHandler() {
        Flash.getInstance().getServer().getPluginManager().registerEvents(new CacheListener(), Flash.getInstance());

        switch (FlashLanguage.CACHE_TYPE.getString().toUpperCase()) {
            case "REDIS": userCache = new RedisCache(); break;
            case "MONGO": userCache = new MongoCache(); break;
            case "FLATFILE":
            case "YAML": userCache = new FlatFileCache(); break;
            default: userCache = new DefaultCache(); break;
        }

        this.userCache.load();

    }

}
