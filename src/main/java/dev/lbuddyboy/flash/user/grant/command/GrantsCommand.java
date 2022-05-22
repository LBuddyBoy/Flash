package dev.lbuddyboy.flash.user.grant.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.user.grant.menu.GrantMenu;
import dev.lbuddyboy.flash.user.grant.menu.GrantsMenu;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("grants")
public class GrantsCommand extends BaseCommand {

    @Default
    public static void grants(Player sender, @Name("target") UUID uuid) {
        new GrantsMenu(uuid).openMenu(sender);
    }

}
