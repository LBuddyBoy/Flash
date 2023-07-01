package dev.lbuddyboy.flash.cache.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.cache.UserCache
import dev.lbuddyboy.flash.cache.packet.CacheDistributePacket
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bson.Document
import org.bukkit.Bukkit
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class MongoCache : UserCache() {
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
        for (document in Flash.instance.mongoHandler.getCacheCollection().find()) {
            val uuid = UUID.fromString(document.getString("uuid"))
            val name = document.getString("name")
            try {
                uuidNameMap[uuid] = name
                nameUUIDMap[name] = uuid
            } catch (ignored: Exception) {
            }
            ++i
        }
        Bukkit.getConsoleSender().sendMessage(CC.translate("&fInitiated the &eMongo Cache&f."))
        if (i > 0) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&fCached &b$i&f names & uuids."))
        }
    }

    override fun update(uuid: UUID, name: String?, save: Boolean) {
        if (save) {
            if (Flash.instance.mongoHandler.getCacheCollection().find(Filters.eq("uuid", uuid.toString()))
                    .first() == null || Flash.instance.mongoHandler.getCacheCollection()
                    .find(Filters.eq("name", name)).first() == null
            ) {
                val document = Document()
                document["uuid"] = uuid.toString()
                document["name"] = name
                Flash.instance.mongoHandler.getCacheCollection()
                    .replaceOne(Filters.eq("uuid", uuid.toString()), document, ReplaceOptions().upsert(true))
            }
            CacheDistributePacket(uuid, name).send()
        }
        uuidNameMap[uuid] = name
        nameUUIDMap[name] = uuid
    }

    companion object {
        private val uuidNameMap: MutableMap<UUID?, String?> = ConcurrentHashMap()
        private val nameUUIDMap: MutableMap<String?, UUID> = ConcurrentHashMap()
    }
}