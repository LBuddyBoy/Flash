package dev.lbuddyboy.flash.user.impl

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.model.Grant
import dev.lbuddyboy.flash.util.gson.GSONUtils
import org.bukkit.configuration.file.FileConfiguration
import java.io.IOException
import java.util.*

class FlatFileUser(uuid: UUID?, name: String?, load: Boolean) : User() {
    init {
        this.uuid = uuid
        this.name = name
        if (load) load()
    }

    override fun load() {
        val config: FileConfiguration = Flash.instance.userHandler.getUsersYML().gc()
        if (!config.contains("profiles." + getUuid().toString())) {
            save(true, true)
            return
        }
        val path = "profiles." + getUuid().toString() + "."
        name = config.getString(path + "name")
        ip = config.getString(path + "ip")
        knownIps = config.getStringList(path + "knownIps")
        activePrefix = GSONUtils.getGSON().fromJson(config.getString(path + "activePrefix"), GSONUtils.PREFIX)
        blocked = GSONUtils.getGSON().fromJson(config.getString(path + "blocked"), GSONUtils.UUID)
        permissions = GSONUtils.getGSON().fromJson(config.getString(path + "permissions"), GSONUtils.USER_PERMISSION)
        punishments = GSONUtils.getGSON().fromJson(config.getString(path + "punishments"), GSONUtils.PUNISHMENTS)
        notes = GSONUtils.getGSON().fromJson(config.getString(path + "notes"), GSONUtils.NOTE)
        grants = GSONUtils.getGSON().fromJson(config.getString(path + "grants"), GSONUtils.GRANT)
        promotions = GSONUtils.getGSON().fromJson(config.getString(path + "promotions"), GSONUtils.PROMOTIONS)
        demotions = GSONUtils.getGSON().fromJson(config.getString(path + "demotions"), GSONUtils.DEMOTIONS)
        playerInfo = GSONUtils.getGSON().fromJson(config.getString(path + "playerInfo"), GSONUtils.PLAYER_INFO)
        serverInfo = GSONUtils.getGSON().fromJson(config.getString(path + "serverInfo"), GSONUtils.SERVER_INFO)
        staffInfo = GSONUtils.getGSON().fromJson(config.getString(path + "staffInfo"), GSONUtils.STAFF_INFO)
        if (grants.isEmpty()) {
            grants.add(Grant.Companion.defaultGrant())
        }
        updateGrants()
    }

    override fun loadRank() {
        val config: FileConfiguration = Flash.instance.userHandler.getUsersYML().gc()
        if (!config.contains("profiles." + getUuid().toString())) {
            save(true, true)
            return
        }
        val path = "profiles." + getUuid().toString() + "."
        name = config.getString(path + "name")
        grants = GSONUtils.getGSON().fromJson(config.getString(path + "grants"), GSONUtils.GRANT)
        if (grants.isEmpty()) {
            grants.add(Grant.Companion.defaultGrant())
        }
        updateGrants()
    }

    override fun save(async: Boolean) {
        save(false, false)
    }

    fun save(saveConfig: Boolean, reload: Boolean) {

        /*
        Can't run config updates async. Throws concurrent modification exceptions
         */
        val config: FileConfiguration = Flash.instance.userHandler.getUsersYML().gc()
        val path = "profiles." + getUuid().toString() + "."
        config[path + "uuid"] = uuid.toString()
        config[path + "name"] = name
        config[path + "ip"] = ip
        config[path + "knownIps"] = knownIps
        config[path + "activePrefix"] = GSONUtils.getGSON().toJson(activePrefix, GSONUtils.PREFIX)
        config[path + "punishments"] = GSONUtils.getGSON().toJson(punishments, GSONUtils.PUNISHMENTS)
        config[path + "blocked"] = GSONUtils.getGSON().toJson(blocked, GSONUtils.UUID)
        config[path + "permissions"] = GSONUtils.getGSON().toJson(permissions, GSONUtils.USER_PERMISSION)
        config[path + "notes"] = GSONUtils.getGSON().toJson(notes, GSONUtils.NOTE)
        config[path + "grants"] = GSONUtils.getGSON().toJson(grants, GSONUtils.GRANT)
        config[path + "promotions"] = GSONUtils.getGSON().toJson(promotions, GSONUtils.PROMOTIONS)
        config[path + "demotions"] = GSONUtils.getGSON().toJson(demotions, GSONUtils.DEMOTIONS)
        config[path + "playerInfo"] = GSONUtils.getGSON().toJson(playerInfo, GSONUtils.PLAYER_INFO)
        config[path + "serverInfo"] = GSONUtils.getGSON().toJson(serverInfo, GSONUtils.SERVER_INFO)
        config[path + "staffInfo"] = GSONUtils.getGSON().toJson(staffInfo, GSONUtils.STAFF_INFO)
        if (saveConfig) {
            try {
                Flash.instance.userHandler.getUsersYML().save()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
        if (reload) load()
    }
}