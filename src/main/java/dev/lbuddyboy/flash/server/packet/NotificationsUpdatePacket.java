package dev.lbuddyboy.flash.server.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.server.Server;
import dev.lbuddyboy.flash.server.model.Notification;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class NotificationsUpdatePacket implements JedisPacket {

    private List<Notification> notifications;

    @Override
    public void onReceive() {
        Flash.getInstance().getServerHandler().setNotifications(this.notifications);
    }
}
