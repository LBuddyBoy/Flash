package dev.lbuddyboy.flash.redis.sub

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.JedisPacket
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.gson.GSONUtils
import lombok.NoArgsConstructor
import lombok.SneakyThrows
import redis.clients.jedis.JedisPubSub

@NoArgsConstructor
class JedisSubscriber : JedisPubSub() {
    @SneakyThrows
    override fun onMessage(channel: String, message: String) {
        val packetClass: Class<*>
        val packetMessageSplit = message.indexOf("||")
        val packetClassStr = message.substring(0, packetMessageSplit)
        val messageJson = message.substring(packetMessageSplit + "||".length)
        packetClass = try {
            Class.forName(packetClassStr)
        } catch (ignored: ClassNotFoundException) {
            Flash.instance.server.consoleSender.sendMessage(CC.translate("&cA jedis packet tried to establish, but the version seems to be different."))
            return
        }
        val packet = GSONUtils.GSON.fromJson(messageJson, packetClass) as JedisPacket
        packet?.onReceive()
    }
}