package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.util.bukkit.CC
import lombok.AllArgsConstructor

@AllArgsConstructor
class ServerChangePacket : JedisPacket {
    private val join = false
    private val player: String? = null
    private val server: String? = null
    override fun onReceive() {
        if (!join) {
            CC.broadCastStaff(FlashLanguage.SERVER_CHANGE_LEAVE.string, "%PLAYER%", player, "%SERVER%", server)
            return
        }
        CC.broadCastStaff(FlashLanguage.SERVER_CHANGE_JOIN.string, "%PLAYER%", player, "%SERVER%", server)
    }
}