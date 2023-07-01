package dev.lbuddyboy.flash.handler

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.server.Server
import dev.lbuddyboy.flash.server.listener.NotificationListener
import dev.lbuddyboy.flash.server.model.Notification
import dev.lbuddyboy.flash.server.model.impl.MongoNotification
import dev.lbuddyboy.flash.server.model.impl.RedisNotification
import dev.lbuddyboy.flash.server.packet.ServerUpdatePacket
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.util.gson.GSONUtils
import lombok.Data
import org.bukkit.Bukkit
import redis.clients.jedis.Jedis
import java.util.*
import java.util.stream.Collectors

@Data
class ServerHandler {
    private val servers: Map<UUID, Server>
    private val currentServer: Server? = null
    private val notifications: MutableList<Notification>

    init {
        servers = HashMap()
        notifications = ArrayList()

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
//        Flash.instance.getServer().getPluginManager().registerEvents(new ServerListener(), Flash.instance);
        Flash.instance.server.pluginManager.registerEvents(NotificationListener(), Flash.instance)
        loadAllNotifications()
    }

    fun update(server: Server) {
        server.lastResponse = System.currentTimeMillis()
        server.online = Bukkit.getOnlinePlayers().size
        server.maxPlayers = Bukkit.getMaxPlayers()
        server.isWhitelist = Bukkit.hasWhitelist()
        server.motd = Bukkit.getMotd()
        val jedis: Jedis = RedisHandler.Companion.requestJedis().getResource()
        jedis.hset("Servers", server.uuid.toString(), GSONUtils.getGSON().toJson(server, GSONUtils.SERVER))
        ServerUpdatePacket(server).send()
    }

    fun getReadNotifications(user: User): List<Notification> {
        return notifications.stream()
            .filter { notification: Notification -> user.getPlayerInfo().readNotifications.contains(notification.getId()) }
            .collect(Collectors.toList())
    }

    fun getUnReadNotifications(user: User): List<Notification> {
        return notifications.stream()
            .filter { notification: Notification -> !user.getPlayerInfo().readNotifications.contains(notification.getId()) }
            .collect(Collectors.toList())
    }

    private fun loadAllNotifications() {
        if (FlashLanguage.CACHE_TYPE.string.equals("YAML", ignoreCase = true)) return
        when (FlashLanguage.CACHE_TYPE.string.uppercase(Locale.getDefault())) {
            "REDIS" -> {
                for ((key) in RedisHandler.Companion.requestJedis().getResource().hgetAll("Notifications").entries) {
                    notifications.add(RedisNotification(UUID.fromString(key)))
                }
            }

            "MONGO" -> {
                for (document in Flash.instance.mongoHandler.notificationCollection.find()) {
                    val uuid = UUID.fromString(document.getString("id"))
                    notifications.add(MongoNotification(uuid))
                }
            }
        }
    }

    fun createNotification(title: String?, message: String?): Notification? {
        return when (FlashLanguage.CACHE_TYPE.string.uppercase(Locale.getDefault())) {
            "REDIS" -> RedisNotification(title, message)
            "MONGO" -> MongoNotification(title, message)
            else -> null
        }
    }
}