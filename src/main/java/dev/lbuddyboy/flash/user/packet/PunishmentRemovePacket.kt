package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.user.model.Punishment
import lombok.AllArgsConstructor
import java.util.*

@AllArgsConstructor
class PunishmentRemovePacket : JedisPacket {
    private val uuid: UUID? = null
    private val punishment: Punishment? = null
    override fun onReceive() {
        val user = Flash.instance.userHandler.tryUser(uuid, false) ?: return
        for (userPunishment in user.getPunishments()) {
            if (userPunishment.id.toString() != punishment.getId().toString()) continue
            userPunishment.removedBy = punishment.getRemovedBy()
            userPunishment.removedFor = punishment.getRemovedFor()
            userPunishment.removedAt = punishment.getRemovedAt()
            userPunishment.isRemovedSilent = punishment.isRemovedSilent()
            userPunishment.isRemoved = punishment.isRemoved()
        }
        user.save(true)
    }
}