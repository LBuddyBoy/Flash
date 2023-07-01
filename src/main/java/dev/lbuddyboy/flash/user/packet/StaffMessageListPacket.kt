package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.util.bukkit.CC
import lombok.AllArgsConstructor
import org.bukkit.Bukkit
import java.util.function.Consumer

@AllArgsConstructor
class StaffMessageListPacket : JedisPacket {
    private val messageList: List<String>? = null
    private val objects: Array<Any>
    override fun onReceive() {
        for (player in Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("flash.staff")) continue
            messageList!!.forEach(Consumer { s: String? -> player.sendMessage(CC.translate(s, *objects)) })
        }
        messageList!!.forEach(Consumer { s: String? ->
            Bukkit.getConsoleSender().sendMessage(CC.translate(s, *objects))
        })
    }
}