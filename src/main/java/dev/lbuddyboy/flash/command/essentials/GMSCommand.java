package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@CommandAlias("gms")
@CommandPermission("flash.command.gamemode.survival")
public class GMSCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public static void gms(Player sender, @Name("target") @Default("defaultSelf") Player target) {
        target.setGameMode(GameMode.SURVIVAL);
        if (sender == target) {
            sender.sendMessage(CC.translate("&cGameMode&7: &fSURVIVAL"));
        } else {
            sender.sendMessage(CC.translate("&c" + target.getName() + "'s GameMode&7: &fSURVIVAL"));
        }
    }

}
