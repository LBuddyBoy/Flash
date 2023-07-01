package dev.lbuddyboy.flash.server.model.impl

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.handler.RedisHandler.Companion.requestJedis
import dev.lbuddyboy.flash.server.model.Notification
import dev.lbuddyboy.flash.server.packet.NotificationsUpdatePacket
import dev.lbuddyboy.flash.util.gson.GSONUtils
import java.util.*

class RedisNotification : Notification {
    constructor(id: UUID?) {
        this.id = id
        load()
    }

    constructor(title: String?, message: String?) {
        id = UUID.randomUUID()
        this.title = title
        this.message = message
        sentAt = System.currentTimeMillis()
        save()
    }

    override fun load() {
        val notification = GSONUtils.getGSON().fromJson<Notification>(
            requestJedis().resource.hget("Notifications", id.toString()),
            GSONUtils.NOTIFICATION
        )
        title = notification.getTitle()
        message = notification.getMessage()
        sentAt = notification.getSentAt()
    }

    override fun delete() {
        Flash.instance.serverHandler.getNotifications().remove(this)
        requestJedis().resource.hdel("Notifications", id.toString())
        NotificationsUpdatePacket(Flash.instance.serverHandler.getNotifications()).send()
    }

    override fun save() {
        requestJedis().resource.hset(
            "Notifications",
            id.toString(),
            GSONUtils.getGSON().toJson(this, GSONUtils.NOTIFICATION)
        )
    }
}