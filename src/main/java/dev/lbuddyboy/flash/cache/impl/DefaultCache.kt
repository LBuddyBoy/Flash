package dev.lbuddyboy.flash.cache.impl

import dev.lbuddyboy.flash.cache.UserCache
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.Bukkit
import java.util.*

class DefaultCache : UserCache() {
    override fun load() {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&fInitiated the &aDefault Cache&f."))
    }

    override fun getName(uuid: UUID?): String? {
        return Bukkit.getOfflinePlayer(uuid).name
    }

    override fun getUUID(name: String?): UUID? {
        return Bukkit.getOfflinePlayer(name).uniqueId
    }

    override fun update(uuid: UUID, name: String?, save: Boolean) {}
}