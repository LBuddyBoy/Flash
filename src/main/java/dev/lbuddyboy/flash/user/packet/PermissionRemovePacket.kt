package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.user.model.UserPermission
import lombok.AllArgsConstructor
import java.util.*

@AllArgsConstructor
class PermissionRemovePacket : JedisPacket {
    private val uuid: UUID? = null
    private val permission: UserPermission? = null
    override fun onReceive() {
        val user = Flash.instance.userHandler.tryUser(uuid, false) ?: return
        for (userPermission in user.getPermissions()) {
            if (userPermission.node != permission.getNode()) continue
            userPermission.removedBy = permission.getRemovedBy()
            userPermission.removedFor = permission.getRemovedFor()
            userPermission.removedAt = permission.getRemovedAt()
            userPermission.isRemoved = permission.isRemoved()
        }
        user.save(true)
        user.updateGrants()
    }
}