package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("world|switchworld|changeworld")
@CommandPermission("flash.command.world")
public class WorldCommand extends BaseCommand {

    @Default
    @CommandCompletion("@worlds")
    public static void feed(CommandSender sender, @Name("target") @Default("self") Player target, @Name("world") String world) {
        World bukkitWorld = Bukkit.getWorld(world);

        if (bukkitWorld == null) {
            sender.sendMessage(CC.translate("&cThat world does not exist."));
            return;
        }

        target.teleport(new Location(bukkitWorld, target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ()));

    }

}
