package dev.lbuddyboy.flash.command.user.grant;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.menu.GrantMenu;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("grant")
@CommandPermission("flash.command.grant")
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
