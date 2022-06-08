package dev.lbuddyboy.flash.command.essentials.message;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.entity.Player;

@CommandAlias("togglemessages|togglepms|tpm|tpms|tmsg|tmsgs|tmessages")
@CommandPermission("flash.command.togglemessages")
public class ToggleMessagesCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public static void message(Player sender) {

        User senderUser = Flash.getInstance().getUserHandler().tryUser(sender.getUniqueId(), false);

        senderUser.getPlayerInfo().setPmsOn(!senderUser.getPlayerInfo().isPmsOn());

        if (senderUser.getPlayerInfo().isPmsOn()) {
            sender.sendMessage(CC.translate("&aYou can now be messaged!"));
            return;
        }

        sender.sendMessage(CC.translate("&cYou can no longer be messaged!"));

    }

}
