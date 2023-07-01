package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.command.CommandSender;

import java.io.IOException;

@CommandAlias("flash|core")
@CommandPermission("flash.command.core")
public class FlashCommand extends BaseCommand {

    @HelpCommand
    public static void help(CommandSender sender, CommandHelp help) {
        sender.sendMessage(CC.translate("&4&lFlash Help"));
        help.showHelp();
    }

    @Subcommand("reload|reloadconfig")
    public static void reload(CommandSender sender) {
        Flash.getInstance().reloadConfig();
        try {
            Flash.getInstance().getMenusYML().reloadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        sender.sendMessage(CC.translate("&cSuccessfully reloaded the menus.yml & config.yml"));
    }

}
