package dev.lbuddyboy.flash.cache.impl

import dev.lbuddyboy.flash.cache.UserCache
import dev.lbuddyboy.flash.cache.packet.CacheDistributePacket
import dev.lbuddyboy.flash.handler.RedisHandler.Companion.requestJedis
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.Bukkit
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class RedisCache : UserCache() {
    override fun getName(uuid: UUID?): String? {
        return uuidNameMap[uuid]
    }

    override fun getUUID(name: String?): UUID? {
        return nameUUIDMap[name]
    }

    override fun load() {
        val cache = requestJedis().resource.hgetAll(KEY)
        var i = 0
        for ((key, name) in cache) {
            val uuid = UUID.fromString(key)
            uuidNameMap[uuid] = name
            nameUUIDMap[name] = uuid
            i++
        }
        Bukkit.getConsoleSender().sendMessage(CC.translate("&fInitiated the &bRedis Cache&f."))
        if (i > 0) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&fCached &b$i&f names & uuids."))
        }
    }

    override fun allUUIDs(): List<UUID> {
        return ArrayList(nameUUIDMap.values)
    }

    override fun update(uuid: UUID, name: String?, save: Boolean) {
        if (save) {
            requestJedis().resource.hset(KEY, uuid.toString(), name)
            CacheDistributePacket(uuid, name).send()
        }
        uuidNameMap[uuid] = name
        nameUUIDMap[name] = uuid
    }

    companion object {
        private const val KEY = "CACHE"
        private val uuidNameMap: MutableMap<UUID?, String?> = ConcurrentHashMap()
        private val nameUUIDMap: MutableMap<String?, UUID> = ConcurrentHashMap()
    }
}