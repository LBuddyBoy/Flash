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
import dev.lbuddyboy.flash.util.bukkit.UserUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("kick")
@CommandPermission("flash.command.kick")
public class KickCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public static void ban(CommandSender sender, @Name("target") UUID uuid, @Name("reason {-p}") String reason) {
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

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.KICK, uuid, senderUUID, System.currentTimeMillis(), Long.MAX_VALUE, reason, FlashLanguage.SERVER_NAME.getString(), !isPub);

        user.getPunishments().add(punishment);
        user.save(true);
        new PunishmentAddPacket(uuid, punishment).send();

        Player target = Bukkit.getPlayer(uuid);
        if (target != null) Bukkit.getScheduler().runTask(Flash.getInstance(), () -> target.kickPlayer(punishment.format()));

        Bukkit.getScheduler().runTask(Flash.getInstance(), () -> new PunishmentSendPacket(punishment).send());
        if (senderUUID != null) UserUtils.addPunishment(senderUUID, punishment);


    }

}
