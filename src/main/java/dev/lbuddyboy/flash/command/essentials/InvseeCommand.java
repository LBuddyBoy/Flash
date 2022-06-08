package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import org.bukkit.entity.Player;

@CommandAlias("invsee|showinv|showinventory")
@CommandPermission("flash.command.invsee")
public class InvseeCommand extends BaseCommand {

    @Default
    public static void invsee(Player sender, @Name("player") Player target) {
        if (sender == target) return;
        sender.openInventory(target.getInventory());
    }

}
