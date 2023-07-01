package dev.lbuddyboy.flash.server.packet

import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.redis.JedisPacket
import lombok.AllArgsConstructor
import org.bukkit.Bukkit

@AllArgsConstructor
class ServerStopPacket : JedisPacket {
    private val server: String? = null
    override fun onReceive() {
        if (FlashLanguage.SERVER_NAME.string != server) return
        Bukkit.shutdown()
    }
}