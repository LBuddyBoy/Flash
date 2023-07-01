package dev.lbuddyboy.flash.rank.impl

import com.mongodb.client.model.Filtersimport

com.mongodb.client.model.ReplaceOptionsimport dev.lbuddyboy.flash.Flashimport dev.lbuddyboy.flash.rank.Rankimport dev.lbuddyboy.flash.rank.packet.RanksUpdatePacketimport dev.lbuddyboy.flash.util.gson.GSONUtilsimport org.bson.Documentimport org.bukkit.*import java.util.*

class MongoRank : Rank {
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
        val document: Document =
            Flash.instance.mongoHandler.getRankCollection().find(Filters.eq("uuid", uuid.toString())).first()
        if (document == null) {
            save(true, true)
            return
        }
        if (document.containsKey("name")) name = document.getString("name")
        if (document.containsKey("displayName")) displayName = document.getString("displayName")
        if (document.containsKey("color")) color = ChatColor.valueOf(document.getString("color"))
        if (document.containsKey("weight")) weight = document.getInteger("weight")
        if (document.containsKey("prefix")) prefix = document.getString("prefix")
        if (document.containsKey("suffix")) suffix = document.getString("suffix")
        if (document.containsKey("default")) defaultRank = document.getBoolean("default")
        if (document.containsKey("staff")) staff = document.getBoolean("staff")
        if (document.containsKey("permissions")) permissions =
            GSONUtils.getGSON().fromJson(document.getString("permissions"), GSONUtils.STRING)
        if (document.containsKey("inheritance")) inheritance =
            GSONUtils.getGSON().fromJson(document.getString("inheritance"), GSONUtils.STRING)
    }

    override fun delete() {
        Flash.instance.rankHandler.getRanks().remove(uuid)
        Flash.instance.mongoHandler.getRankCollection().deleteOne(Filters.eq("uuid", uuid.toString()))
        RanksUpdatePacket(Flash.instance.rankHandler.getRanks()).send()
    }

    override fun save(async: Boolean) {
        save(async, false)
    }

    private fun save(async: Boolean, reload: Boolean) {
        val document = Document()
        document["uuid"] = uuid.toString()
        document["name"] = name
        document["displayName"] = displayName
        document["color"] = color.name
        document["weight"] = weight
        document["prefix"] = prefix
        document["suffix"] = suffix
        document["default"] = defaultRank
        document["staff"] = staff
        document["permissions"] = GSONUtils.getGSON().toJson(permissions, GSONUtils.STRING)
        document["inheritance"] = GSONUtils.getGSON().toJson(inheritance, GSONUtils.STRING)
        Flash.instance.mongoHandler.getRankCollection()
            .replaceOne(Filters.eq("uuid", uuid.toString()), document, ReplaceOptions().upsert(true))
        if (reload) load()
    }
}