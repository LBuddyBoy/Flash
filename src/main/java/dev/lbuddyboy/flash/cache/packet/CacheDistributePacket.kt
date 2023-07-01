package dev.lbuddyboy.flash.cache.packet

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.JedisPacket
import lombok.AllArgsConstructor
import java.util.*

@AllArgsConstructor
class CacheDistributePacket : JedisPacket {
    private val uuid: UUID? = null
    private val name: String? = null
    override fun onReceive() {
        Flash.instance.cacheHandler.getUserCache().update(uuid, name, false)
    }
}