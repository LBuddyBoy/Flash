package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.util.bukkit.ItemUtils;
import org.bukkit.entity.Player;

@CommandAlias("rename")
@CommandPermission("flash.command.rename")
public class RenameCommand extends BaseCommand {

    @Default
    public static void rename(Player sender, @Name("name") String name) {
        ItemUtils.setDisplayName(sender.getItemInHand(), name);
    }

}
