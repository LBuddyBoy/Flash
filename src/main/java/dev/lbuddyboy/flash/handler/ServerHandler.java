package dev.lbuddyboy.flash.handler;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.server.Server;
import dev.lbuddyboy.flash.server.listener.NotificationListener;
import dev.lbuddyboy.flash.server.listener.ServerListener;
import dev.lbuddyboy.flash.server.model.Notification;
import dev.lbuddyboy.flash.server.model.impl.MongoNotification;
import dev.lbuddyboy.flash.server.model.impl.RedisNotification;
import dev.lbuddyboy.flash.server.packet.ServerUpdatePacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import lombok.Data;
import org.bson.Document;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class ServerHandler {

    private Map<UUID, Server> servers;
    private Server currentServer;
    private List<Notification> notifications;

    public ServerHandler() {
        this.servers = new HashMap<>();
        this.notifications = new ArrayList<>();

//        if (RedisHandler.isEnabled()) {
//            Jedis jedis = RedisHandler.requestJedis().getResource();
//            for (Map.Entry<String, String> entry : jedis.hgetAll("Servers").entrySet()) {
//                UUID uuid = UUID.fromString(entry.getKey());
//                Server server = GSONUtils.getGSON().fromJson(entry.getValue(), GSONUtils.SERVER);
//
//                this.servers.put(uuid, server);
//            }
//
//            if (!this.servers.values().stream().map(Server::getName).collect(Collectors.toList()).contains(FlashLanguage.SERVER_NAME.getString())) {
//                Server server = new Server(UUID.randomUUID(), FlashLanguage.SERVER_NAME.getString());
//
//                server.setLastResponse(System.currentTimeMillis());
//                server.setMaxPlayers(Bukkit.getMaxPlayers());
//                server.setWhitelist(Bukkit.hasWhitelist());
//                server.setMotd(Bukkit.getMotd());
//
//                this.servers.put(server.getUuid(), server);
//            }
//
//            this.currentServer = this.servers.values().stream().filter(server -> server.getName().equals(FlashLanguage.SERVER_NAME.getString())).collect(Collectors.toList()).get(0);
//
//        }
//
//        Flash.getInstance().getServer().getPluginManager().registerEvents(new ServerListener(), Flash.getInstance());
        Flash.getInstance().getServer().getPluginManager().registerEvents(new NotificationListener(), Flash.getInstance());

        this.loadAllNotifications();
    }

    public void update(Server server) {

        server.setLastResponse(System.currentTimeMillis());
        server.setOnline(Bukkit.getOnlinePlayers().size());
        server.setMaxPlayers(Bukkit.getMaxPlayers());
        server.setWhitelist(Bukkit.hasWhitelist());
        server.setMotd(Bukkit.getMotd());

        Jedis jedis = RedisHandler.requestJedis().getResource();

        jedis.hset("Servers", server.getUuid().toString(), GSONUtils.getGSON().toJson(server, GSONUtils.SERVER));

        new ServerUpdatePacket(server).send();
    }

    public List<Notification> getReadNotifications(User user) {
        return this.notifications.stream().filter(notification -> user.getPlayerInfo().getReadNotifications().contains(notification.getId())).collect(Collectors.toList());
    }

    public List<Notification> getUnReadNotifications(User user) {
        return this.notifications.stream().filter(notification -> !user.getPlayerInfo().getReadNotifications().contains(notification.getId())).collect(Collectors.toList());
    }

    private void loadAllNotifications() {
        if (FlashLanguage.CACHE_TYPE.getString().equalsIgnoreCase("YAML")) return;

        switch (FlashLanguage.CACHE_TYPE.getString().toUpperCase()) {
            case "REDIS": {
                for (Map.Entry<String, String> entry : RedisHandler.requestJedis().getResource().hgetAll("Notifications").entrySet()) {
                    String key = entry.getKey();

                    this.notifications.add(new RedisNotification(UUID.fromString(key)));
                }
                break;
            }
            case "MONGO": {
                for (Document document : Flash.getInstance().getMongoHandler().getNotificationCollection().find()) {
                    UUID uuid = UUID.fromString(document.getString("id"));

                    this.notifications.add(new MongoNotification(uuid));
                }
                break;
            }
        }
    }

    public Notification createNotification(String title, String message) {
        switch (FlashLanguage.CACHE_TYPE.getString().toUpperCase()) {
            case "REDIS": return new RedisNotification(title, message);
            case "MONGO": return new MongoNotification(title, message);
// TODO: ADD FLAT FILE NOTIFICATIONS            case "FLATFILE":
//            case "YAML": return new (name);
            default: return null;
        }
    }

}
