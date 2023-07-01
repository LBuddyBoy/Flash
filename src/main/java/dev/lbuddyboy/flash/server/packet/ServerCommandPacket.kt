package dev.lbuddyboy.flash.server.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.server.Server;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class ServerCommandPacket implements JedisPacket {

    private String server, command;

    @Override
    public void onReceive() {
        if (!FlashLanguage.SERVER_NAME.getString().equals(this.server)) return;

        Tasks.run(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command));
    }
}
