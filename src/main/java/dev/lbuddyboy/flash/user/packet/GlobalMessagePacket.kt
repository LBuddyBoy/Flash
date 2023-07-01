package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.redis.JedisPacket
import org.bukkit.Bukkit
import java.util.*
import java.util.function.Consumer

class GlobalMessagePacket : JedisPacket {
    private val target: UUID
    private var messages: List<String>? = null
    private var message: String? = null

    constructor(target: UUID, messages: List<String>?) {
        this.target = target
        this.messages = messages
    }

    constructor(target: UUID, message: String?) {
        this.target = target
        this.message = message
    }

    override fun onReceive() {
        val player = Bukkit.getPlayer(target) ?: return
        if (messages != null) {
            messages!!.forEach(Consumer { s: String? -> player.sendMessage(s) })
            return
        }
        player.sendMessage(message)
    }
}