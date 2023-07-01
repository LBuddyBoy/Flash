package dev.lbuddyboy.flash.command.essentials.message;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("message|msg|m|dm|pm|privatemessage|privatemsg")
@CommandPermission("flash.command.message")
public class MessageCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public static void message(CommandSender sender, @Name("target") UUID targetUUID, @Name("message") String message) {
        if (!(sender instanceof Player)) {

            User targetUser = Flash.getInstance().getUserHandler().tryUser(targetUUID, false);
            Player targetPlayer = Bukkit.getPlayer(targetUUID);

            targetPlayer.sendMessage(CC.translate("&7(From &4&lCONSOLE&7): " + message));
            sender.sendMessage(CC.translate("&7(To " + targetUser.getColoredName() + "&7): " + message));

            targetUser.getPlayerInfo().setReply(null);

            return;
        }

        Player senderPlayer = (Player) sender;
        UUID senderUUID = senderPlayer.getUniqueId();

        User senderUser = Flash.getInstance().getUserHandler().tryUser(senderUUID, false);
        User targetUser = Flash.getInstance().getUserHandler().tryUser(targetUUID, false);

        if (targetUser == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        if (targetUser.getBlocked().contains(senderUUID) && !senderPlayer.hasPermission("flash.message.bypass")) {
            sender.sendMessage(CC.translate("&cThat player currently has you blocked."));
            return;
        }

        if (senderUser.getBlocked().contains(senderUUID) && !senderPlayer.hasPermission("flash.message.bypass")) {
            sender.sendMessage(CC.translate("&cYou currently have that player blocked."));
            return;
        }

        if (!senderUser.getPlayerInfo().isPmsOn()) {
            sender.sendMessage(CC.translate("&cYour messages are currently toggled off."));
            return;
        }

        if (!targetUser.getPlayerInfo().isPmsOn() && !senderPlayer.hasPermission("flash.message.bypass")) {
            sender.sendMessage(CC.translate("&cThat players messages are currently toggled off."));
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(targetUUID);

        targetPlayer.sendMessage(CC.translate("&7(From " + senderUser.getColoredName() + "&7): " + message));
        senderPlayer.sendMessage(CC.translate("&7(To " + targetUser.getColoredName() + "&7): " + message));

        senderUser.getPlayerInfo().setReply(targetUUID);
        targetUser.getPlayerInfo().setReply(senderUUID);

    }

}
