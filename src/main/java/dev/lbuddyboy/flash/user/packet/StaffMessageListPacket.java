package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@AllArgsConstructor
public class StaffMessageListPacket implements JedisPacket {

    private List<String> messageList;

    @Override
    public void onReceive() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("flash.staff")) continue;

            messageList.forEach(s -> player.sendMessage(CC.translate(s)));
        }
        messageList.forEach(s -> Bukkit.getConsoleSender().sendMessage(CC.translate(s)));
    }

}
