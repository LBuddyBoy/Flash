package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;

@CommandAlias("teleporthere|tphere|s|tph")
@CommandPermission("flash.command.teleport.here")
public class TeleportHereCommand extends BaseCommand {

    @Default
    public static void teleport(Player sender, @Name("target") @Default("other") @Flags("other") Player target) {
        target.teleport(sender);
    }

}
