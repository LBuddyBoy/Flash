package dev.lbuddyboy.flash.user.packet;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PunishmentSendPacket implements JedisPacket {

    private Punishment punishment;

    @Override
    public void onReceive() {

        punishment.announce();

        if (punishment.isRemoved()) return;

        Player target = Bukkit.getPlayer(punishment.getTarget());

        if (target == null) return;

        String message = CC.translate(punishment.getType().getKickMessage(),
                "%REASON%", punishment.getSentFor(),
                "%REASON%", punishment.getSentFor(),
                "%TEMP-FORMAT%", FlashLanguage.PUNISHMENT_TEMPORARY_FORMAT.getString().replaceAll("%TIME%", punishment.getExpireString())
        );

        target.sendMessage(message);
        if (punishment.getType() == PunishmentType.MUTE || punishment.getType() == PunishmentType.WARN) return;
        Tasks.run(() -> target.kickPlayer(message));

    }

}
