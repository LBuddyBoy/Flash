package dev.lbuddyboy.flash.user.grant.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.user.grant.menu.GrantMenu;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("grant")
public class GrantCommand extends BaseCommand {

    @Default
    public static void grant(Player sender, @Name("target") UUID uuid) {
        new GrantMenu(uuid).openMenu(sender);
    }

}
