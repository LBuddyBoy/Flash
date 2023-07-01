package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Punishment;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class PunishmentAddPacket implements JedisPacket {

    private UUID uuid;
    private Punishment punishment;

    @Override
    public void onReceive() {
        User user = Flash.getInstance().getUserHandler().tryUser(this.uuid, false);
        if (user == null) return;
        if (user.hasPunishment(punishment.getId())) return;

        user.getPunishments().add(punishment);
        user.save(true);
    }

}
