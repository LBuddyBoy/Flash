package dev.lbuddyboy.flash.handler

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.cache.UserCache
import dev.lbuddyboy.flash.cache.impl.DefaultCache
import dev.lbuddyboy.flash.cache.impl.FlatFileCache
import dev.lbuddyboy.flash.cache.impl.MongoCache
import dev.lbuddyboy.flash.cache.impl.RedisCache
import dev.lbuddyboy.flash.cache.listener.CacheListener
import lombok.Getter
import java.util.*

@Getter
class CacheHandler {
    private var userCache: UserCache? = null

    init {
        Flash.instance.server.pluginManager.registerEvents(CacheListener(), Flash.instance)
        userCache = when (FlashLanguage.CACHE_TYPE.string.uppercase(Locale.getDefault())) {
            "REDIS" -> RedisCache()
            "MONGO" -> MongoCache()
            "FLATFILE", "YAML" -> FlatFileCache()
            else -> DefaultCache()
        }
        userCache.load()
    }
}