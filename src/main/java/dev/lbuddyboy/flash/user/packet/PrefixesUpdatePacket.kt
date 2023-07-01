package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.user.model.*
import dev.lbuddyboy.flash.util.bukkit.CC
import lombok.AllArgsConstructor

@AllArgsConstructor
class PrefixesUpdatePacket : JedisPacket {
    private val newPrefixes: List<Prefix>? = null
    override fun onReceive() {
        Flash.instance.userHandler.setPrefixes(newPrefixes)
        CC.broadCastStaff("&4[Prefixes] &aAll ranks have been updated & refreshed!")
    }
}