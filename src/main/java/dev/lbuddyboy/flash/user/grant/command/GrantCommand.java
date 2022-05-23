package dev.lbuddyboy.flash.user.grant.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.grant.menu.GrantMenu;
import dev.lbuddyboy.flash.util.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("grant")
public class GrantCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public static void grant(Player sender, @Name("target") UUID uuid) {

        if (Flash.getInstance().getUserHandler().tryUser(uuid, true) == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        new GrantMenu(uuid).openMenu(sender);
    }

}
