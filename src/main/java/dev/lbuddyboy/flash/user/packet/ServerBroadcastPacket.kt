package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.util.Callable;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ServerBroadcastPacket implements JedisPacket {

    private List<String> servers;
    private String message;

    @Override
    public void onReceive() {
        this.servers = this.servers.stream().map(String::toLowerCase).collect(Collectors.toList());
        if (this.servers.contains("global")) {
            Tasks.run(() -> Bukkit.broadcastMessage(this.message));
            return;
        }
        if (this.servers.contains(FlashLanguage.SERVER_NAME.getString().toLowerCase())) Tasks.run(() -> Bukkit.broadcastMessage(this.message));
    }

}
