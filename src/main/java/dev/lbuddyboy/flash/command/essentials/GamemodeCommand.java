package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@CommandAlias("gamemode|gm")
@CommandPermission("flash.command.gamemode")
public class GamemodeCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target @gamemodes")
    public static void rename(Player sender, @Name("target") @Default("defaultSelf") Player target, @Name("name") GameMode gameMode) {
        target.setGameMode(gameMode);
        if (sender == target) {
            sender.sendMessage(CC.translate("&cGameMode&7: &f" + gameMode.name()));
        } else {
            sender.sendMessage(CC.translate("&c" + target.getName() + "'s GameMode&7: &f" + gameMode.name()));
        }
    }

}
