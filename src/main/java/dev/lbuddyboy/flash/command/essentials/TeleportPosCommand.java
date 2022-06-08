package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("teleportpos|tppos|location|loc|relocate")
@CommandPermission("flash.command.teleport.position")
public class TeleportPosCommand extends BaseCommand {

    @Default
    public static void teleport(Player sender, @Name("target") @Default("self") Player target, @Name("x") int x, @Name("y") int y, @Name("z") int z) {
        Location location = new Location(target.getWorld(), x, y, z);

        location.setX(location.getBlockX() + 0.5);
        location.setZ(location.getBlockZ() + 0.5);

        sender.teleport(location);
    }

}
