package dev.lbuddyboy.flash.server.model.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.server.model.Notification
import dev.lbuddyboy.flash.server.packet.NotificationsUpdatePacket
import org.bson.Document
import java.util.*

class MongoNotification : Notification {
    constructor(id: UUID?) {
        this.id = id
        load()
    }

    constructor(title: String?, message: String?) {
        id = UUID.randomUUID()
        this.title = title
        this.message = message
        sentAt = System.currentTimeMillis()
    }

    override fun load() {
        val document: Document =
            Flash.instance.mongoHandler.getNotificationCollection().find(Filters.eq("id", id.toString())).first()
        if (document == null) {
            reload()
            return
        }
        title = document.getString("title")
        message = document.getString("message")
        sentAt = document.getLong("sentAt")
    }

    override fun delete() {
        Flash.instance.serverHandler.getNotifications().remove(this)
        Flash.instance.mongoHandler.getNotificationCollection().deleteOne(Filters.eq("id", id.toString()))
        NotificationsUpdatePacket(Flash.instance.serverHandler.getNotifications()).send()
    }

    override fun save() {
        val document = Document()
        document["id"] = id.toString()
        document["title"] = title
        document["message"] = message
        document["sentAt"] = sentAt
        Flash.instance.mongoHandler.getNotificationCollection()
            .replaceOne(Filters.eq("id", id.toString()), document, ReplaceOptions().upsert(true))
    }

    fun reload() {
        save()
        load()
    }
}