package dev.lbuddyboy.flash.cache.impl

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.cache.UserCache
import dev.lbuddyboy.flash.cache.packet.CacheDistributePacket
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.Bukkit
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class FlatFileCache : UserCache() {
    var uuidNameMap: MutableMap<UUID?, String?> = ConcurrentHashMap()
    var nameUUIDMap: MutableMap<String?, UUID> = ConcurrentHashMap()
    override fun getName(uuid: UUID?): String? {
        return uuidNameMap[uuid]
    }

    override fun getUUID(name: String?): UUID? {
        return nameUUIDMap[name]
    }

    override fun allUUIDs(): List<UUID> {
        return ArrayList(nameUUIDMap.values)
    }

    override fun load() {
        var i = 0
        try {
            for (key in Flash.instance.userHandler.getUsersYML().gc().getConfigurationSection("profiles")
                .getKeys(false)) {
                val uuid = UUID.fromString(key)
                val name: String = Flash.instance.userHandler.getUsersYML().gc().getString(
                    "profiles.$key.name"
                )
                uuidNameMap[uuid] = name
                nameUUIDMap[name] = uuid
                ++i
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Bukkit.getConsoleSender().sendMessage(CC.translate("&fInitiated the &aFlatFile Cache&f."))
        if (i > 0) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&fCached &b$i&f names & uuids."))
        }
    }

    override fun update(uuid: UUID, name: String?, save: Boolean) {
        // User updates will already handle this.
        if (save) {
            CacheDistributePacket(uuid, name).send()
        }
        uuidNameMap[uuid] = name
        nameUUIDMap[name] = uuid
    }
}