package dev.lbuddyboy.flash.server.model.impl;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.handler.RedisHandler;
import dev.lbuddyboy.flash.server.model.Notification;
import dev.lbuddyboy.flash.server.packet.NotificationsUpdatePacket;
import dev.lbuddyboy.flash.util.gson.GSONUtils;

import java.util.UUID;

public class RedisNotification extends Notification {

    public RedisNotification(UUID id) {
        this.id = id;

        load();
    }

    public RedisNotification(String title, String message) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.message = message;
        this.sentAt = System.currentTimeMillis();

        save();
    }

    @Override
    public void load() {
        Notification notification = GSONUtils.getGSON().fromJson(RedisHandler.requestJedis().getResource().hget("Notifications", this.id.toString()), GSONUtils.NOTIFICATION);

        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.sentAt = notification.getSentAt();
    }

    @Override
    public void delete() {
        Flash.getInstance().getServerHandler().getNotifications().remove(this);
        RedisHandler.requestJedis().getResource().hdel("Notifications", this.id.toString());
        new NotificationsUpdatePacket(Flash.getInstance().getServerHandler().getNotifications()).send();
    }

    @Override
    public void save() {
        RedisHandler.requestJedis().getResource().hset("Notifications", this.id.toString(), GSONUtils.getGSON().toJson(this, GSONUtils.NOTIFICATION));
    }
}
