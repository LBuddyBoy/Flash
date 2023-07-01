package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.util.bukkit.CC
import lombok.RequiredArgsConstructor
import lombok.Setter
import org.bukkit.Bukkit

@RequiredArgsConstructor
@Setter
class AdminMessagePacket : JedisPacket {
    private val message: String? = null
    override fun onReceive() {
        for (player in Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("flash.admin")) continue
            player.sendMessage(CC.translate(message))
        }
    }
}