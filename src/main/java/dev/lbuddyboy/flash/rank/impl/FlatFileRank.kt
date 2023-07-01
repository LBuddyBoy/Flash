package dev.lbuddyboy.flash.rank.impl

import dev.lbuddyboy.flash.Flashimport

dev.lbuddyboy.flash.rank.Rankimport org.bukkit.*import org.bukkit.configuration.file.FileConfigurationimport

java.io.IOExceptionimport java.util.*
class FlatFileRank : Rank {
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
        val config: FileConfiguration = Flash.instance.rankHandler.getRanksYML().gc()
        if (!config.contains("ranks." + getUuid().toString())) {
            save(true, true)
            return
        }
        val path = "ranks." + getUuid().toString() + "."
        name = config.getString(path + "name")
        displayName = config.getString(path + "displayName")
        color = ChatColor.valueOf(config.getString(path + "color"))
        weight = config.getInt(path + "weight")
        defaultRank = config.getBoolean(path + "default")
        staff = config.getBoolean(path + "staff")
        prefix = config.getString(path + "prefix")
        suffix = config.getString(path + "suffix")
        permissions = config.getStringList(path + "permissions")
        inheritance = config.getStringList(path + "inheritance")
    }

    override fun delete() {
        // No clue how to delete shit from a yaml file
    }

    override fun save(async: Boolean) {
        save(async, false)
    }

    private fun save(async: Boolean, reload: Boolean) {
        val config: FileConfiguration = Flash.instance.rankHandler.getRanksYML().gc()
        val path = "ranks." + getUuid().toString() + "."
        config[path + "uuid"] = uuid.toString()
        config[path + "name"] = name
        config[path + "displayName"] = displayName
        config[path + "color"] = color.name
        config[path + "weight"] = weight
        config[path + "default"] = defaultRank
        config[path + "staff"] = staff
        config[path + "prefix"] = prefix
        config[path + "suffix"] = suffix
        config[path + "permissions"] = permissions
        config[path + "inheritance"] = inheritance
        try {
            Flash.instance.rankHandler.getRanksYML().save()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        if (reload) load()
    }
}