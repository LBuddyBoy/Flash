package dev.lbuddyboy.flash.command.user.punishment;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.user.menu.PunishmentHistorySelectionMenu;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("check|c|checkpunishments|punishments|history")
@CommandPermission("flash.command.history")
public class CheckPunishmentsCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public static void ban(Player sender, @Name("target") UUID uuid) {
        new PunishmentHistorySelectionMenu(uuid).openMenu(sender);
    }

}
