package dev.lbuddyboy.flash.server.packet

import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.util.bukkit.Tasks
import lombok.AllArgsConstructor
import org.bukkit.Bukkit

@AllArgsConstructor
class ServerCommandPacket : JedisPacket {
    private val server: String? = null
    private val command: String? = null
    override fun onReceive() {
        if (FlashLanguage.SERVER_NAME.string != server) return
        Tasks.run { Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command) }
    }
}