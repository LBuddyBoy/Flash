package dev.lbuddyboy.flash.command.server;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import dev.lbuddyboy.flash.server.menu.ServersMenu;
import org.bukkit.entity.Player;

@CommandAlias("servers|serverlist")
@CommandPermission("flash.command.servers")
public class ServersCommand extends BaseCommand {

    @Default
    public static void def(Player sender) {
        new ServersMenu().openMenu(sender);
    }

}
