package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.user.model.Grant
import dev.lbuddyboy.flash.user.model.Promotion
import lombok.AllArgsConstructor
import java.util.*

@AllArgsConstructor
class GrantAddPacket : JedisPacket {
    private val uuid: UUID? = null
    private val grant: Grant? = null
    override fun onReceive() {
        val user = Flash.instance.userHandler.tryUser(uuid, false) ?: return
        if (user.hasGrant(grant.getUuid())) return
        if (user.activeRank.isStaff && user.activeRank.getWeight() < grant!!.rank.getWeight()) {
            val promotion = Promotion(user.activeRank.coloredName, grant!!.rank.coloredName, System.currentTimeMillis())
            user.getPromotions().add(promotion)
        }
        if (grant!!.rank.isStaff && !user.activeRank.isStaff) {
            user.getStaffInfo().joinedStaffTeam = System.currentTimeMillis()
        }
        user.getGrants().add(grant)
        user.save(true)
        user.updateGrants()
    }
}