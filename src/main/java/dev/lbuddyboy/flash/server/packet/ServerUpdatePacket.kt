package dev.lbuddyboy.flash.server.packet

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.server.Server
import lombok.AllArgsConstructor

@AllArgsConstructor
class ServerUpdatePacket : JedisPacket {
    private val newServer: Server? = null
    override fun onReceive() {
        Flash.instance.serverHandler.getServers().put(newServer.getUuid(), newServer)
    }
}