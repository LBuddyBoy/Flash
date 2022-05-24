package dev.lbuddyboy.flash.redis;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.handler.RedisHandler;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import redis.clients.jedis.Jedis;

public interface JedisPacket {

    void onReceive();

    default void send() {
        if (!RedisHandler.isEnabled()) {
            onReceive();
            return;
        }

        new Thread(() -> {
            try (Jedis jedis = RedisHandler.requestJedis().getResource()) {
                String encodedPacket = this.getClass().getName() + "||" + GSONUtils.GSON.toJson(this);
                jedis.publish("Flash:Global", encodedPacket);
            }
        }).start();
    }
}

