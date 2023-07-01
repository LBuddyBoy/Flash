package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.util.bukkit.CC
import lombok.RequiredArgsConstructor
import lombok.Setter
import org.bukkit.Bukkit
import java.util.function.Consumer

@RequiredArgsConstructor
@Setter
class StaffMessagePacket : JedisPacket {
    private val message: String? = null
    private val messageList: List<String>? = null
    override fun onReceive() {
        if (message == null) {
            for (player in Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("flash.staff")) continue
                messageList!!.forEach(Consumer { s: String? -> player.sendMessage(CC.translate(s)) })
            }
            messageList!!.forEach(Consumer { s: String? -> Bukkit.getConsoleSender().sendMessage(CC.translate(s)) })
            return
        }
        for (player in Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("flash.staff")) continue
            player.sendMessage(CC.translate(message))
        }
        messageList!!.forEach(Consumer { s: String? -> Bukkit.getConsoleSender().sendMessage(CC.translate(s)) })
    }
}