package dev.lbuddyboy.flash.server.packet;

import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

@AllArgsConstructor
public class ServerStopPacket implements JedisPacket {

    private String server;

    @Override
    public void onReceive() {
        if (!FlashLanguage.SERVER_NAME.getString().equals(this.server)) return;

        Bukkit.shutdown();
    }
}
