package dev.lbuddyboy.flash.rank.impl

import dev.lbuddyboy.flash.Flashimport

dev.lbuddyboy.flash.handler.RedisHandler.Companion.requestJedisimport dev.lbuddyboy.flash.rank.Rankimport dev.lbuddyboy.flash.rank.packet.RanksUpdatePacketimport dev.lbuddyboy.flash.util.gson.GSONUtilsimport java.util.*
class RedisRank : Rank {
    constructor(name: String?) {
        uuid = UUID.randomUUID()
        this.name = name
        load()
    }

    constructor(uuid: UUID?, name: String?) {
        this.uuid = uuid
        this.name = name
        load()
    }

    override fun load() {
        try {
            val rank = GSONUtils.getGSON()
                .fromJson<RedisRank>(requestJedis().resource.hget("Ranks", getUuid().toString()), GSONUtils.REDIS_RANK)
            name = rank.getName()
            uuid = rank.getUuid()
            displayName = rank.getDisplayName()
            color = rank.getColor()
            weight = rank.getWeight()
            prefix = rank.getPrefix()
            defaultRank = rank.isDefaultRank
            suffix = rank.getSuffix()
            permissions = rank.getPermissions()
            inheritance = rank.getInheritance()
            staff = rank.isStaff
        } catch (ignored: Exception) {
            save(true, true)
        }
    }

    override fun delete() {
        Flash.instance.rankHandler.getRanks().remove(uuid)
        requestJedis().resource.hdel("Ranks", getUuid().toString())
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    override fun save(async: Boolean) {
        save(async, false)
    }

    private fun save(async: Boolean, reload: Boolean) {
        requestJedis().resource.hset("Ranks", getUuid().toString(), toJson())
        if (reload) load()
    }

    fun toJson(): String {
        return GSONUtils.getGSON().toJson(this, GSONUtils.REDIS_RANK)
    }
}