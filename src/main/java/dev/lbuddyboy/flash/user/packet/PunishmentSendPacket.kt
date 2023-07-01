package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.user.model.Punishment
import dev.lbuddyboy.flash.user.model.PunishmentType
import dev.lbuddyboy.flash.util.bukkit.Tasks
import lombok.AllArgsConstructor
import org.bukkit.Bukkit

@AllArgsConstructor
class PunishmentSendPacket : JedisPacket {
    private val punishment: Punishment? = null
    override fun onReceive() {
        punishment!!.announce()
        if (punishment.isRemoved) return
        val target = Bukkit.getPlayer(punishment.target) ?: return
        val message = punishment.format()
        if (punishment.type == PunishmentType.MUTE || punishment.type == PunishmentType.WARN) return
        target.sendMessage(message)
        Tasks.run { target.kickPlayer(message) }
    }
}