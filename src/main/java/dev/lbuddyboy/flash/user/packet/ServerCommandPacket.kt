package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ServerCommandPacket implements JedisPacket {

    private List<String> servers;
    private String command;

    @Override
    public void onReceive() {
        this.servers = this.servers.stream().map(String::toLowerCase).collect(Collectors.toList());
        if (this.servers.contains("global")) {
            Tasks.run(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command));
            return;
        }
        if (this.servers.contains(FlashLanguage.SERVER_NAME.getString().toLowerCase())) Tasks.run(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command));
    }

}
