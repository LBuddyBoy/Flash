package dev.lbuddyboy.flash.cache.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.JedisPacket;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class CacheDistributePacket implements JedisPacket {

    private UUID uuid;
    private String name;

    @Override
    public void onReceive() {
        Flash.getInstance().getCacheHandler().getUserCache().update(uuid, name, false);
    }
}
