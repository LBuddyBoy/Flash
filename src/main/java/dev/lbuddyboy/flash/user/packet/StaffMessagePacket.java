package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Setter
public class StaffMessagePacket implements JedisPacket {

    private final String message;
    private List<String> messageList;

    @Override
    public void onReceive() {
        if (message == null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("flash.staff")) continue;

                messageList.forEach(s -> player.sendMessage(CC.translate(s)));
            }
            messageList.forEach(s -> Bukkit.getConsoleSender().sendMessage(CC.translate(s)));
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("flash.staff")) continue;

            player.sendMessage(CC.translate(message));
        }
        messageList.forEach(s -> Bukkit.getConsoleSender().sendMessage(CC.translate(s)));
    }

}
