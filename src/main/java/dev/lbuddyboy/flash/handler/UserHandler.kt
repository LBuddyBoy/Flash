package dev.lbuddyboy.flash.handler

import com.mongodb.client.model.Filters
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.rank.Rank
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.impl.FlatFileUser
import dev.lbuddyboy.flash.user.impl.MongoUser
import dev.lbuddyboy.flash.user.impl.RedisUser
import dev.lbuddyboy.flash.user.listener.*
import dev.lbuddyboy.flash.user.model.Prefix
import dev.lbuddyboy.flash.util.YamlDoc
import dev.lbuddyboy.flash.util.gson.GSONUtils
import lombok.Getter
import lombok.Setter
import redis.clients.jedis.JedisPool
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Getter
class UserHandler {
    private val users: MutableMap<UUID, User>
    private var usersYML: YamlDoc? = null

    @Setter
    private val prefixes: MutableList<Prefix>

    init {
        users = ConcurrentHashMap()
        prefixes = ArrayList()
        when (FlashLanguage.CACHE_TYPE.string.uppercase(Locale.getDefault())) {
            "FLATFILE", "YAML" -> usersYML = YamlDoc(
                Flash.instance.dataFolder, "users.yml"
            )
        }
        for (document in Flash.instance.mongoHandler.prefixCollection.find()) prefixes.add(
            GSONUtils.getGSON().fromJson(document.toJson(), GSONUtils.PREFIX)
        )
        Flash.instance.server.pluginManager.registerEvents(FreezeListener(), Flash.instance)
        Flash.instance.server.pluginManager.registerEvents(GrantListener(), Flash.instance)
        Flash.instance.server.pluginManager.registerEvents(NoteListener(), Flash.instance)
        Flash.instance.server.pluginManager.registerEvents(PunishmentListener(), Flash.instance)
        Flash.instance.server.pluginManager.registerEvents(UserListener(), Flash.instance)
    }

    fun getUser(uuid: UUID, searchDb: Boolean): User? {
        return if (searchDb) {
            if (users.containsKey(uuid)) getUser(uuid, false) else when (FlashLanguage.CACHE_TYPE.string.uppercase(
                Locale.getDefault()
            )) {
                "REDIS" -> RedisUser(
                    uuid,
                    Flash.instance.cacheHandler.userCache.getName(uuid),
                    true
                )

                "MONGO" -> MongoUser(
                    uuid,
                    Flash.instance.cacheHandler.userCache.getName(uuid),
                    true
                )

                "FLATFILE", "YAML" -> FlatFileUser(
                    uuid,
                    Flash.instance.cacheHandler.userCache.getName(uuid),
                    true
                )

                else -> null
            }
        } else users[uuid]
    }

    fun getUserRank(uuid: UUID, searchDb: Boolean): User? {
        return if (searchDb) {
            if (users.containsKey(uuid)) getUser(uuid, false) else when (FlashLanguage.CACHE_TYPE.string.uppercase(
                Locale.getDefault()
            )) {
                "REDIS" -> {
                    val user =
                        RedisUser(uuid, Flash.instance.cacheHandler.userCache.getName(uuid), false)
                    user.loadRank()
                    user
                }

                "MONGO" -> {
                    val user =
                        MongoUser(uuid, Flash.instance.cacheHandler.userCache.getName(uuid), false)
                    user.loadRank()
                    user
                }

                "FLATFILE", "YAML" -> {
                    val user =
                        FlatFileUser(
                            uuid,
                            Flash.instance.cacheHandler.userCache.getName(uuid),
                            false
                        )
                    user.loadRank()
                    user
                }

                else -> null
            }
        } else users[uuid]
    }

    fun tryUser(uuid: UUID, searchDb: Boolean): User? {
        return try {
            getUser(uuid, searchDb)
        } catch (ignored: Exception) {
            null
        }
    }

    fun tryUserRank(uuid: UUID, searchDb: Boolean): User? {
        return try {
            getUserRank(uuid, searchDb)
        } catch (ignored: Exception) {
            null
        }
    }

    fun createUser(uuid: UUID?, name: String?): User? {
        return when (FlashLanguage.CACHE_TYPE.string.uppercase(Locale.getDefault())) {
            "REDIS" -> RedisUser(uuid, name, true)
            "MONGO" -> MongoUser(uuid, name, true)
            "FLATFILE", "YAML" -> FlatFileUser(uuid, name, true)
            else -> null
        }
    }

    fun deleteUser(uuid: UUID) {
        val user = tryUser(uuid, true) ?: return
        users.remove(uuid)
        when (FlashLanguage.CACHE_TYPE.string.uppercase(Locale.getDefault())) {
            "REDIS" -> {
                RedisHandler.Companion.requestJedis().getResource().hdel("Users", uuid.toString())
                Flash.instance.mongoHandler.userCollection.deleteOne(Filters.eq("uuid", uuid.toString()))
            }

            "MONGO" -> Flash.instance.mongoHandler.userCollection.deleteOne(Filters.eq("uuid", uuid.toString()))
            "FLATFILE", "YAML" -> {}
            else -> {}
        }
    }

    fun relativeAlts(ip: String): List<UUID> {
        val uuids: MutableList<UUID> = ArrayList()
        when (FlashLanguage.CACHE_TYPE.string.uppercase(Locale.getDefault())) {
            "REDIS" -> {
                val pool: JedisPool = RedisHandler.Companion.requestJedis()
                for ((key) in pool.resource.hgetAll("Users")) {
                    val user = RedisUser(UUID.fromString(key), null, true)
                    if (user.getIp() != ip) continue
                    uuids.add(user.getUuid())
                }
            }

            "MONGO" -> for (document in Flash.instance.mongoHandler.userCollection.find()) {
                try {
                    if (!document.containsKey("ip")) continue
                    val altIP = document.getString("ip") ?: continue
                    if (altIP != ip) continue
                    uuids.add(UUID.fromString(document.getString("uuid")))
                } catch (ignored: Exception) {
                }
            }

            "FLATFILE", "YAML" -> for (key in usersYML!!.gc().getConfigurationSection("users").getKeys(false)) {
                val altIP = usersYML.gc().getString("users.$key.ip")
                if (ip != altIP) continue
                uuids.add(UUID.fromString(key))
            }
        }
        return uuids
    }

    fun getPrefix(lookUp: String): Prefix? {
        for (prefix in prefixes) {
            if (prefix.id == lookUp) return prefix
        }
        return null
    }

    val rankConversion: Map<Long, Rank>
        get() {
            val conversion: MutableMap<Long, Rank> = HashMap()
            for (key in FlashLanguage.SYNC_CONVERSION.stringList) {
                val args = key.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                conversion[java.lang.Long.valueOf(args[0])] = Flash.instance.rankHandler.getRank(
                    args[1]
                )
            }
            return conversion
        }
}