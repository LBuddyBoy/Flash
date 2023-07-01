package dev.lbuddyboy.flash.server.packet

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.server.model.Notification
import lombok.AllArgsConstructor

@AllArgsConstructor
class NotificationsUpdatePacket : JedisPacket {
    private val notifications: List<Notification>? = null
    override fun onReceive() {
        Flash.instance.serverHandler.setNotifications(notifications)
    }
}