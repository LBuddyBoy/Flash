package dev.lbuddyboy.flash.user.punishment.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.user.packet.PunishmentSendPacket;
import dev.lbuddyboy.flash.user.packet.UserUpdatePacket;
import dev.lbuddyboy.flash.user.punishment.menu.PunishmentHistorySelectionMenu;
import dev.lbuddyboy.flash.util.CC;
import dev.lbuddyboy.flash.util.JavaUtils;
import org.bukkit.command.CommandSender;
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
