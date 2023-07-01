package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("clear|ci|clearinv|clearinventory")
@CommandPermission("flash.command.clear")
public class ClearCommand extends BaseCommand {

    @Default
    public static void clear(CommandSender sender, @Name("target") @Default("self") Player target) {
        target.getInventory().clear();
        target.updateInventory();

        sender.sendMessage(CC.translate("&aSuccessfully cleared inventory."));
    }

}
