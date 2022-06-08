package dev.lbuddyboy.flash.server.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.server.Server;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerUpdatePacket implements JedisPacket {

    private Server newServer;

    @Override
    public void onReceive() {
        Flash.getInstance().getServerHandler().getServers().put(newServer.getUuid(), newServer);
    }
}
