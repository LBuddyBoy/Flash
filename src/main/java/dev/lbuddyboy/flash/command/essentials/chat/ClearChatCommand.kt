package dev.lbuddyboy.flash.command.essentials.chat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.UserUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("clearchat|cc")
@CommandPermission("flash.command.clearchat")
public class ClearChatCommand extends BaseCommand {

    @Default
    public static void def(CommandSender sender) {
        for (int i = 0; i < 300; i++) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("flash.staff")) continue;

                player.sendMessage(" ");
            }
        }
        Bukkit.broadcastMessage(CC.translate("&dThe chat has just been cleared by " + UserUtils.formattedName(sender) + "."));
    }

}
