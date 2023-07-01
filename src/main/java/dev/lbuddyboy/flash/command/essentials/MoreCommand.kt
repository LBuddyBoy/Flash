package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("more|moreitems|stack")
@CommandPermission("flash.command.more")
public class MoreCommand extends BaseCommand {

    @Default
    public static void more(Player sender) {
        sender.getItemInHand().setAmount(64);

        sender.sendMessage(CC.translate("&aSuccessfully set the item amount in your hand to 64."));
    }

}
