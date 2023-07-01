package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.redis.JedisPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class GlobalMessagePacket implements JedisPacket {

    private final UUID target;
    private List<String> messages;
    private String message;

    public GlobalMessagePacket(UUID target, List<String> messages) {
        this.target = target;
        this.messages = messages;
    }

    public GlobalMessagePacket(UUID target, String message) {
        this.target = target;
        this.message = message;
    }

    @Override
    public void onReceive() {
        Player player = Bukkit.getPlayer(this.target);
        if (player == null) return;

        if (this.messages != null) {
            this.messages.forEach(player::sendMessage);
            return;
        }

        player.sendMessage(this.message);
    }

}
