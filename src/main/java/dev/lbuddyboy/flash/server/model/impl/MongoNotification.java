package dev.lbuddyboy.flash.server.model.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.handler.RedisHandler;
import dev.lbuddyboy.flash.rank.packet.RanksUpdatePacket;
import dev.lbuddyboy.flash.server.model.Notification;
import dev.lbuddyboy.flash.server.packet.NotificationsUpdatePacket;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.UUID;

public class MongoNotification extends Notification {

    public MongoNotification(UUID id) {
        this.id = id;

        load();
    }

    public MongoNotification(String title, String message) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.message = message;
        this.sentAt = System.currentTimeMillis();
    }

    @Override
    public void load() {
        Document document = Flash.getInstance().getMongoHandler().getNotificationCollection().find(Filters.eq("id", this.id.toString())).first();

        if (document == null) {
            reload();
            return;
        }

        this.title = document.getString("title");
        this.message = document.getString("message");
        this.sentAt = document.getLong("sentAt");
    }

    @Override
    public void delete() {
        Flash.getInstance().getServerHandler().getNotifications().remove(this);
        Flash.getInstance().getMongoHandler().getNotificationCollection().deleteOne(Filters.eq("id", this.id.toString()));
        new NotificationsUpdatePacket(Flash.getInstance().getServerHandler().getNotifications()).send();
    }

    @Override
    public void save() {
        Document document = new Document();

        document.put("id", this.id.toString());
        document.put("title", this.title);
        document.put("message", this.message);
        document.put("sentAt", this.sentAt);

        Flash.getInstance().getMongoHandler().getNotificationCollection().replaceOne(Filters.eq("id", this.id.toString()), document, new ReplaceOptions().upsert(true));
    }

    public void reload() {
        save();
        load();
    }

}
