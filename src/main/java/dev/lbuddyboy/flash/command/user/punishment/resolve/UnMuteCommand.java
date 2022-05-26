package dev.lbuddyboy.flash.command.user.punishment.resolve;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.user.packet.PunishmentRemovePacket;
import dev.lbuddyboy.flash.user.packet.PunishmentSendPacket;
import dev.lbuddyboy.flash.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("unmute|unsilence|pardonmute")
@CommandPermission("flash.command.unmute")
public class UnMuteCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public static void unmute(CommandSender sender, @Name("target") UUID uuid, @Name("reason {-p}") String reason) {
        boolean isPub = reason.contains("-p");
        UUID senderUUID = sender instanceof Player ? ((Player) sender).getUniqueId() : null;

        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        if (sender instanceof Player) {
            User senderUser = Flash.getInstance().getUserHandler().tryUser(senderUUID, true);

            if (senderUser.getActiveRank().getWeight() < user.getActiveRank().getWeight()) {
                sender.sendMessage(CC.translate(FlashLanguage.PUNISHMENTS_HIGHER_PRIORITY.getString()));
                return;
            }
        }

        Punishment punishment = user.getActivePunishment(PunishmentType.MUTE);

        if (punishment == null) {
            sender.sendMessage(CC.translate(FlashLanguage.PUNISHMENTS_NOT_PUNISHED.getString(), "%FORMAT%", PunishmentType.MUTE.getFormat()));
            return;
        }

        punishment.setRemoved(true);
        punishment.setRemovedBy(senderUUID);
        punishment.setRemovedAt(System.currentTimeMillis());
        punishment.setRemovedSilent(!isPub);
        punishment.setRemovedFor(reason);

        if (Bukkit.getPlayer(uuid) != null) {
            user.save(true);
        } else {
            new PunishmentRemovePacket(uuid, punishment).send();
        }

        new PunishmentSendPacket(punishment).send();

    }

}