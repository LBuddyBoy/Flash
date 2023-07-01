package dev.lbuddyboy.flash.redis

import dev.lbuddyboy.flash.handler.RedisHandler
import dev.lbuddyboy.flash.handler.RedisHandler.Companion.requestJedis
import dev.lbuddyboy.flash.util.gson.GSONUtils

interface JedisPacket {
    fun onReceive()
    fun send() {
        if (!RedisHandler.isEnabled()) {
            onReceive()
            return
        }
        Thread {
            requestJedis().resource.use { jedis ->
                val encodedPacket = this.javaClass.name + "||" + GSONUtils.GSON.toJson(this)
                jedis.publish("Flash:Global", encodedPacket)
            }
        }.start()
    }
}