package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("feed|eat|saturate")
@CommandPermission("flash.command.feed")
public class FeedCommand extends BaseCommand {

    @Default
    public static void feed(CommandSender sender, @Name("target") @Default("self") Player target) {
        target.setFoodLevel(20);
        target.setSaturation(20);

        sender.sendMessage(CC.translate("&aSuccessfully fed."));
    }

}
