package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("sudo")
@CommandPermission("flash.command.sudo")
public class SudoCommand extends BaseCommand {

    @Default
    public static void sudo(CommandSender sender, @Name("player") @Default("other") Player target, @Name("command|message") String command) {
        target.chat(command);
        sender.sendMessage(CC.translate("&aSuccessfully made " + target.getName() + " do " + command + "."));
    }

}
