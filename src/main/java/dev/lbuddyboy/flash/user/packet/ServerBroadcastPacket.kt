package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.util.bukkit.Tasks
import lombok.AllArgsConstructor
import org.bukkit.Bukkit
import java.util.*
import java.util.stream.Collectors

@AllArgsConstructor
class ServerBroadcastPacket : JedisPacket {
    private var servers: List<String>? = null
    private val message: String? = null
    override fun onReceive() {
        servers = servers!!.stream().map { obj: String -> obj.lowercase(Locale.getDefault()) }
            .collect(Collectors.toList())
        if (servers.contains("global")) {
            Tasks.run { Bukkit.broadcastMessage(message) }
            return
        }
        if (servers.contains(FlashLanguage.SERVER_NAME.string.lowercase(Locale.getDefault()))) Tasks.run {
            Bukkit.broadcastMessage(
                message
            )
        }
    }
}