package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;

@CommandAlias("teleport|tp")
@CommandPermission("flash.command.teleport")
public class TeleportCommand extends BaseCommand {

    @Default
    public static void teleport(Player sender, @Name("target") @Default("other") @Flags("other") Player target) {
        sender.teleport(target);
    }

}
