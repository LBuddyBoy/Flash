package dev.lbuddyboy.flash.handler;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.server.Server;
import dev.lbuddyboy.flash.server.listener.ServerListener;
import dev.lbuddyboy.flash.server.packet.ServerUpdatePacket;
import dev.lbuddyboy.flash.thread.ServerUpdateThread;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import lombok.Data;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class ServerHandler {

    private Map<UUID, Server> servers;
    private Server currentServer;

    public ServerHandler() {
        this.servers = new HashMap<>();

        if (RedisHandler.isEnabled()) {
            Jedis jedis = RedisHandler.requestJedis().getResource();
            for (Map.Entry<String, String> entry : jedis.hgetAll("Servers").entrySet()) {
                UUID uuid = UUID.fromString(entry.getKey());
                Server server = GSONUtils.getGSON().fromJson(entry.getValue(), GSONUtils.SERVER);

                this.servers.put(uuid, server);
            }
        }

        if (!this.servers.values().stream().map(Server::getName).collect(Collectors.toList()).contains(FlashLanguage.SERVER_NAME.getString())) {
            Server server = new Server(UUID.randomUUID(), FlashLanguage.SERVER_NAME.getString());

            server.setLastResponse(System.currentTimeMillis());
            server.setMaxPlayers(Bukkit.getMaxPlayers());
            server.setWhitelist(Bukkit.hasWhitelist());
            server.setMotd(Bukkit.getMotd());

            this.servers.put(server.getUuid(), server);
        }
        this.currentServer = this.servers.values().stream().filter(server -> server.getName().equals(FlashLanguage.SERVER_NAME.getString())).collect(Collectors.toList()).get(0);

        Flash.getInstance().getServer().getPluginManager().registerEvents(new ServerListener(), Flash.getInstance());

        new ServerUpdateThread().start();
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

}
