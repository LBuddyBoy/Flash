package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@Setter
public class AdminMessagePacket implements JedisPacket {

    private final String message;

    @Override
    public void onReceive() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("flash.admin")) continue;

            player.sendMessage(CC.translate(message));
        }
    }

}
