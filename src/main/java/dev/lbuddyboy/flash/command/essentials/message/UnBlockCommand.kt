package dev.lbuddyboy.flash.command.essentials.message;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.UserUtils;
import org.bson.codecs.OverridableUuidRepresentationUuidCodec;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("unblock|unignore")
@CommandPermission("flash.command.unignore")
public class UnBlockCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public static void message(Player sender, @Name("target") UUID targetUUID) {

        UUID senderUUID = sender.getUniqueId();

        User senderUser = Flash.getInstance().getUserHandler().tryUser(senderUUID, false);

        if (!senderUser.getBlocked().contains(senderUUID)) {
            sender.sendMessage(CC.translate("&cThat player is not blocked."));
            return;
        }

        senderUser.getBlocked().remove(targetUUID);
        sender.sendMessage(CC.translate("&aSuccessfully unblocked " + UserUtils.formattedName(targetUUID) + "&a."));

    }

}
