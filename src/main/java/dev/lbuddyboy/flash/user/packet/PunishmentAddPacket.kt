package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.user.model.Punishment
import lombok.AllArgsConstructor
import java.util.*

@AllArgsConstructor
class PunishmentAddPacket : JedisPacket {
    private val uuid: UUID? = null
    private val punishment: Punishment? = null
    override fun onReceive() {
        val user = Flash.instance.userHandler.tryUser(uuid, false) ?: return
        if (user.hasPunishment(punishment.getId())) return
        user.getPunishments().add(punishment)
        user.save(true)
    }
}