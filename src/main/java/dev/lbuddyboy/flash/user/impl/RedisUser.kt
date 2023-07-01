package dev.lbuddyboy.flash.user.impl

import dev.lbuddyboy.flash.handler.RedisHandler.Companion.requestJedis
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.model.Grant
import dev.lbuddyboy.flash.util.bukkit.*
import dev.lbuddyboy.flash.util.gson.GSONUtils
import java.util.*

class RedisUser(uuid: UUID?, name: String?, load: Boolean) : User() {
    init {
        this.uuid = uuid
        this.name = name
        if (load) load()
    }

    override fun load() {
        try {
            val user = GSONUtils.getGSON()
                .fromJson<RedisUser>(requestJedis().resource.hget("Users", getUuid().toString()), GSONUtils.REDIS_USER)
            name = user.getName()
            ip = user.getIp()
            permissions = user.getPermissions()
            knownIps = user.getKnownIps()
            punishments.addAll(user.getPunishments())
            notes = user.getNotes()
            grants = user.getGrants()
            promotions = user.getPromotions()
            demotions = user.getDemotions()
            activePrefix = user.getActivePrefix()
            playerInfo = user.getPlayerInfo()
            staffInfo = user.getStaffInfo()
            if (grants.isEmpty()) {
                grants.add(Grant.Companion.defaultGrant())
            }
            updateGrants()
        } catch (ignored: Exception) {
            save(true, true)
        }
    }

    override fun loadRank() {
        load()
    }

    override fun save(async: Boolean) {
        save(async, false)
    }

    private fun save(async: Boolean, reload: Boolean) {
        if (!async) {
            requestJedis().resource.hset("Users", getUuid().toString(), toJson())
            if (reload) load()
            return
        }
        Tasks.runAsync { save(false, reload) }
    }

    fun toJson(): String {
        return GSONUtils.getGSON().toJson(this, GSONUtils.REDIS_USER)
    }
}