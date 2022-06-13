package dev.lbuddyboy.flash.command.essentials.message;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("reply|r|respond")
@CommandPermission("flash.command.reply")
public class ReplyCommand extends BaseCommand {

    @Default
    public static void message(Player senderPlayer, @Name("message") String message) {

        UUID senderUUID = senderPlayer.getUniqueId();

        User senderUser = Flash.getInstance().getUserHandler().tryUser(senderUUID, false);
        UUID targetUUID = senderUser.getPlayerInfo().getReply();

        if (targetUUID == null) {
            senderPlayer.sendMessage(CC.translate("&cYou don't have anyone to reply to."));
            return;
        }

        User targetUser = Flash.getInstance().getUserHandler().tryUser(targetUUID, false);

        if (targetUser == null) {
            senderPlayer.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        if (targetUser.getBlocked().contains(senderUUID) && !senderPlayer.hasPermission("flash.message.bypass")) {
            senderPlayer.sendMessage(CC.translate("&cThat player currently has you blocked."));
            return;
        }

        if (senderUser.getBlocked().contains(senderUUID) && !senderPlayer.hasPermission("flash.message.bypass")) {
            senderPlayer.sendMessage(CC.translate("&cYou currently have that player blocked."));
            return;
        }

        if (!senderUser.getPlayerInfo().isPmsOn()) {
            senderPlayer.sendMessage(CC.translate("&cYour messages are currently toggled off."));
            return;
        }

        if (!targetUser.getPlayerInfo().isPmsOn() && !senderPlayer.hasPermission("flash.message.bypass")) {
            senderPlayer.sendMessage(CC.translate("&cThat players messages are currently toggled off."));
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(targetUUID);

        targetPlayer.sendMessage(CC.translate("&7(From " + senderUser.getColoredName() + "&7): " + message));
        senderPlayer.sendMessage(CC.translate("&7(To " + targetUser.getColoredName() + "&7): " + message));

    }


}
