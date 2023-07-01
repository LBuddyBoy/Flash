package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Punishment;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class PunishmentRemovePacket implements JedisPacket {

    private UUID uuid;
    private Punishment punishment;

    @Override
    public void onReceive() {
        User user = Flash.getInstance().getUserHandler().tryUser(this.uuid, false);
        if (user == null) return;

        for (Punishment userPunishment : user.getPunishments()) {
            if (!userPunishment.getId().toString().equals(punishment.getId().toString())) continue;

            userPunishment.setRemovedBy(punishment.getRemovedBy());
            userPunishment.setRemovedFor(punishment.getRemovedFor());
            userPunishment.setRemovedAt(punishment.getRemovedAt());
            userPunishment.setRemovedSilent(punishment.isRemovedSilent());
            userPunishment.setRemoved(punishment.isRemoved());
        }

        user.save(true);
    }

}
