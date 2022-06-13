package dev.lbuddyboy.flash.command.user.punishment;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.user.packet.PunishmentAddPacket;
import dev.lbuddyboy.flash.user.packet.PunishmentSendPacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.JavaUtils;
import dev.lbuddyboy.flash.util.TimeUtils;
import dev.lbuddyboy.flash.util.bukkit.UserUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("blacklist")
@CommandPermission("flash.command.blacklist")
public class BlacklistCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public static void blacklist(CommandSender sender, @Name("target") UUID uuid, @Name("duration") String time, @Name("reason {-p}") String reason) {
        boolean isPub = reason.contains("-p");
        UUID senderUUID = sender instanceof Player ? ((Player) sender).getUniqueId() : null;
        long duration = JavaUtils.parse(time);
        if (time.equalsIgnoreCase("perm")) duration = Long.MAX_VALUE;

        if (duration <= 0) {
            sender.sendMessage(CC.translate("&cInvalid duration."));
            return;
        }

        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        if (sender instanceof Player) {
            User senderUser = Flash.getInstance().getUserHandler().tryUser(senderUUID, true);
            Player senderPlayer = (Player) sender;

            if (!senderPlayer.hasPermission("flash.punish.permanent")) {
                long max = JavaUtils.parse(FlashLanguage.PUNISHMENTS_DEFAULT_PUNISH_TIME.getString());
                if (duration > max) {
                    sender.sendMessage(CC.translate("&cYou do not have permission to punish longer than " + TimeUtils.formatLongIntoDetailedString(max / 1000) + "."));
                    return;
                }
            }

            if (senderUser.getActiveRank().getWeight() < user.getActiveRank().getWeight()) {
                sender.sendMessage(CC.translate(FlashLanguage.PUNISHMENTS_HIGHER_PRIORITY.getString()));
                return;
            }
        }

        if (user.hasActivePunishment(PunishmentType.BLACKLIST)) {
            sender.sendMessage(CC.translate(FlashLanguage.PUNISHMENTS_ALREADY_PUNISHED.getString(), "%FORMAT%", PunishmentType.BLACKLIST.getFormat()));
            return;
        }

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.BLACKLIST, uuid, senderUUID, System.currentTimeMillis(), duration, reason, FlashLanguage.SERVER_NAME.getString(), !isPub);

        user.getPunishments().add(punishment);
        user.save(true);
        new PunishmentAddPacket(uuid, punishment).send();

        new PunishmentSendPacket(punishment).send();
        if (senderUUID != null) UserUtils.addPunishment(senderUUID, punishment);

    }

}
