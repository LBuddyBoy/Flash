package dev.lbuddyboy.flash.handler

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.redis.sub.JedisSubscriber
import dev.lbuddyboy.flash.util.bukkit.CC
import lombok.Getter
import org.bukkit.Bukkit
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

class RedisHandler {
    init {
        if (!Flash.instance.config.getBoolean("redis.use")) {
            Bukkit.getConsoleSender()
                .sendMessage(CC.translate("&fDid not connect to the &bRedis Database&f &aenable&f it in the &econfig.yml&f!"))
            return
        }
        connect()
    }

    companion object {
        @Getter
        private var enabled = false
        fun connect() {
            val connectTo = requestJedis()
            val thread = Thread({
                while (true) {
                    try {
                        val jedis = connectTo.resource
                        var throwable: Throwable? = null
                        try {
                            val pubSub = JedisSubscriber()
                            jedis!!.subscribe(pubSub, "Flash:Global")
                        } catch (pubSub: Throwable) {
                            throwable = pubSub
                            throw pubSub
                        } finally {
                            if (jedis == null) continue
                            if (throwable != null) {
                                try {
                                    jedis.close()
                                } catch (pubSub: Throwable) {
                                    throwable.addSuppressed(pubSub)
                                }
                                continue
                            }
                            jedis.close()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }, "Redis Packet Subscriber")
            thread.isDaemon = true
            thread.start()
            enabled = true
        }

        @JvmStatic
		fun requestJedis(): JedisPool {
            return JedisPool(
                JedisPoolConfig(),
                Flash.instance.config.getString("redis.host"),
                Flash.instance.config.getInt("redis.port"),
                20000,
                if (Flash.instance.config.getString("redis.auth.password")
                        .isEmpty()
                ) null else Flash.instance.config.getString("redis.auth.password"),
                Flash.instance.config.getInt("redis.channel-id", 0)
            )
        }
    }
}