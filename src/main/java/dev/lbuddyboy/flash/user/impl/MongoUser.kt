package dev.lbuddyboy.flash.user.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.model.Grant
import dev.lbuddyboy.flash.util.bukkit.*
import dev.lbuddyboy.flash.util.gson.GSONUtils
import org.bson.Document
import java.util.*

class MongoUser(uuid: UUID?, name: String?, load: Boolean) : User() {
    init {
        this.uuid = uuid
        this.name = name
        if (load) load()
    }

    override fun load() {
        try {
            val document: Document =
                Flash.instance.mongoHandler.getUserCollection().find(Filters.eq("uuid", uuid.toString())).first()
            if (document.containsKey("name")) name = document.getString("name")
            if (document.containsKey("ip")) ip = document.getString("ip")
            if (document.containsKey("blocked")) blocked =
                GSONUtils.getGSON().fromJson(document.getString("blocked"), GSONUtils.UUID)
            if (document.containsKey("activePrefix")) activePrefix =
                GSONUtils.getGSON().fromJson(document.getString("activePrefix"), GSONUtils.PREFIX)
            if (document.containsKey("punishments")) punishments =
                GSONUtils.getGSON().fromJson(document.getString("punishments"), GSONUtils.PUNISHMENTS)
            if (document.containsKey("permissions")) permissions =
                GSONUtils.getGSON().fromJson(document.getString("permissions"), GSONUtils.USER_PERMISSION)
            if (document.containsKey("knownIps")) knownIps =
                GSONUtils.getGSON().fromJson(document.getString("knownIps"), GSONUtils.STRING)
            if (document.containsKey("notes")) notes =
                GSONUtils.getGSON().fromJson(document.getString("notes"), GSONUtils.NOTE)
            if (document.containsKey("grants")) grants =
                GSONUtils.getGSON().fromJson(document.getString("grants"), GSONUtils.GRANT)
            if (document.containsKey("promotions")) promotions =
                GSONUtils.getGSON().fromJson(document.getString("promotions"), GSONUtils.PROMOTIONS)
            if (document.containsKey("demotions")) demotions =
                GSONUtils.getGSON().fromJson(document.getString("demotions"), GSONUtils.DEMOTIONS)
            if (document.containsKey("playerInfo")) playerInfo =
                GSONUtils.getGSON().fromJson(document.getString("playerInfo"), GSONUtils.PLAYER_INFO)
            if (document.containsKey("serverInfo")) serverInfo =
                GSONUtils.getGSON().fromJson(document.getString("serverInfo"), GSONUtils.SERVER_INFO)
            if (document.containsKey("staffInfo")) staffInfo =
                GSONUtils.getGSON().fromJson(document.getString("staffInfo"), GSONUtils.STAFF_INFO)
            if (activeGrants.isEmpty()) grants.add(Grant.Companion.defaultGrant())
            updateGrants()
        } catch (ignored: Exception) {
            save(true, true)
        }
    }

    override fun loadRank() {
        try {
            val document: Document =
                Flash.instance.mongoHandler.getUserCollection().find(Filters.eq("uuid", uuid.toString())).first()
            if (document.containsKey("name")) name = document.getString("name")
            if (document.containsKey("ip")) ip = document.getString("ip")
            if (document.containsKey("grants")) grants =
                GSONUtils.getGSON().fromJson(document.getString("grants"), GSONUtils.GRANT)
            if (activeGrants.isEmpty()) grants.add(Grant.Companion.defaultGrant())
            updateGrants()
        } catch (ignored: Exception) {
            save(true, true)
        }
    }

    override fun save(async: Boolean) {
        save(async, false)
    }

    private fun save(async: Boolean, reload: Boolean) {
        if (!async) {
            val document = Document()
            document["uuid"] = uuid.toString()
            document["name"] = name
            document["ip"] = ip
            document["blocked"] = GSONUtils.getGSON().toJson(blocked, GSONUtils.UUID)
            document["punishments"] = GSONUtils.getGSON().toJson(punishments, GSONUtils.PUNISHMENTS)
            document["permissions"] = GSONUtils.getGSON().toJson(permissions, GSONUtils.USER_PERMISSION)
            document["knownIps"] = GSONUtils.getGSON().toJson(knownIps, GSONUtils.STRING)
            document["notes"] = GSONUtils.getGSON().toJson(notes, GSONUtils.NOTE)
            document["grants"] = GSONUtils.getGSON().toJson(grants, GSONUtils.GRANT)
            document["promotions"] = GSONUtils.getGSON().toJson(promotions, GSONUtils.PROMOTIONS)
            document["demotions"] = GSONUtils.getGSON().toJson(demotions, GSONUtils.DEMOTIONS)
            document["activePrefix"] = GSONUtils.getGSON().toJson(activePrefix, GSONUtils.PREFIX)
            document["playerInfo"] = GSONUtils.getGSON().toJson(playerInfo, GSONUtils.PLAYER_INFO)
            document["serverInfo"] = GSONUtils.getGSON().toJson(serverInfo, GSONUtils.SERVER_INFO)
            document["staffInfo"] = GSONUtils.getGSON().toJson(staffInfo, GSONUtils.STAFF_INFO)
            Flash.instance.mongoHandler.getUserCollection()
                .replaceOne(Filters.eq("uuid", uuid.toString()), document, ReplaceOptions().upsert(true))
            if (reload) load()
            return
        }
        Tasks.runAsync { save(false, reload) }
    }
}