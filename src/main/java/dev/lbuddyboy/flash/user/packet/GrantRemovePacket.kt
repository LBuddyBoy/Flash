package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.user.model.Demotion
import dev.lbuddyboy.flash.user.model.Grant
import lombok.AllArgsConstructor
import java.util.*

@AllArgsConstructor
class GrantRemovePacket : JedisPacket {
    private val uuid: UUID? = null
    private val grant: Grant? = null
    override fun onReceive() {
        val user = Flash.instance.userHandler.tryUser(uuid, false) ?: return
        if (grant!!.rank.isStaff) {
            val demotion = Demotion(user.activeRank.coloredName, System.currentTimeMillis())
            user.getDemotions().add(demotion)
        }
        for (userGrant in user.getGrants()) {
            if (userGrant.uuid.toString() != grant.uuid.toString()) continue
            userGrant.removedBy = grant.removedBy
            userGrant.removedFor = grant.removedFor
            userGrant.removedAt = grant.removedAt
        }
        user.getGrants().removeIf { g: Grant -> g.uuid.toString() == grant.uuid.toString() }
        user.getGrants().add(grant)
        user.save(true)
        user.updateGrants()
    }
}