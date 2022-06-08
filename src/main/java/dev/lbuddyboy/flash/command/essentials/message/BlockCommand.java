package dev.lbuddyboy.flash.command.essentials.message;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.UserUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("block|ignore")
@CommandPermission("flash.command.ignore")
public class BlockCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public static void message(CommandSender sender, @Name("target") UUID targetUUID) {

        Player senderPlayer = (Player) sender;
        UUID senderUUID = senderPlayer.getUniqueId();

        User senderUser = Flash.getInstance().getUserHandler().tryUser(senderUUID, false);

        if (senderUser.getBlocked().contains(senderUUID)) {
            sender.sendMessage(CC.translate("&cThat player is already blocked."));
            return;
        }

        senderUser.getBlocked().add(targetUUID);
        sender.sendMessage(CC.translate("&aSuccessfully blocked " + UserUtils.formattedName(targetUUID) + "&a."));

    }

}
