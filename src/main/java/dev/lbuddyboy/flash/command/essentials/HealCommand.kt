package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("heal")
@CommandPermission("flash.command.heal")
public class HealCommand extends BaseCommand {

    @Default
    public static void heal(CommandSender sender, @Name("target") @Default("self") Player target) {
        target.setHealth(20);
        target.setFoodLevel(20);
        target.setSaturation(20);

        sender.sendMessage(CC.translate("&aSuccessfully healed."));
    }

}

