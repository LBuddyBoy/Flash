package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@CommandAlias("gmc")
@CommandPermission("flash.command.gamemode.creative")
public class GMCCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public static void gmc(Player sender, @Name("target") @Default("defaultSelf") Player target) {
        target.setGameMode(GameMode.CREATIVE);
        if (sender == target) {
            sender.sendMessage(CC.translate("&cGameMode&7: &fCREATIVE"));
        } else {
            sender.sendMessage(CC.translate("&c" + target.getName() + "'s GameMode&7: &fCREATIVE"));
        }
    }

}
