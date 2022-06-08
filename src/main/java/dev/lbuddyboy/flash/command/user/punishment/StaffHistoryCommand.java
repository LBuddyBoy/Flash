package dev.lbuddyboy.flash.command.user.punishment;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.user.menu.staffhistory.StaffHistoryMenu;
import dev.lbuddyboy.flash.user.menu.staffhistory.StaffHistorySelectionMenu;
import net.luckperms.api.event.util.Param;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("staffhistory|staffhist|sh")
public class StaffHistoryCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public static void def(Player sender, @Name("target") UUID target) {
        new StaffHistorySelectionMenu(target).openMenu(sender);
    }

}
