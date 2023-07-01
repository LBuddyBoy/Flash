package dev.lbuddyboy.flash.user.packet

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.user.model.UserPermission
import lombok.AllArgsConstructor
import java.util.*

@AllArgsConstructor
class PermissionAddPacket : JedisPacket {
    private val uuid: UUID? = null
    private val permission: List<UserPermission>? = null
    override fun onReceive() {
        val user = Flash.instance.userHandler.tryUser(uuid, false) ?: return
        user.setPermissions(permission)
        user.save(true)
        user.updatePerms()
    }
}