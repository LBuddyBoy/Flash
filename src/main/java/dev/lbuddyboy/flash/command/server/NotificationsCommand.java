package dev.lbuddyboy.flash.command.server;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.server.menu.NotificationsEditorMenu;
import dev.lbuddyboy.flash.server.menu.NotificationsMenu;
import dev.lbuddyboy.flash.server.model.Notification;
import dev.lbuddyboy.flash.server.packet.NotificationsUpdatePacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("notifications|notification|notifs|reminders|reminder")
public class NotificationsCommand extends BaseCommand {

    @Default
    public static void def(Player sender) {
        new NotificationsMenu("all", Flash.getInstance().getUserHandler().tryUser(sender.getUniqueId(), false)).openMenu(sender);
    }

    @Subcommand("editor|manager|manage")
    @CommandPermission("flash.command.notifications.edit")
    public static void editor(Player sender) {
        new NotificationsEditorMenu(sender.getUniqueId()).openMenu(sender);
    }

    @Subcommand("create|add|send")
    @CommandPermission("flash.command.notifications.edit")
    public static void create(CommandSender sender, @Name("title") @Single String title, @Name("message") String message) {
        Notification notification = Flash.getInstance().getServerHandler().createNotification(title, message);

        Flash.getInstance().getServerHandler().getNotifications().add(notification);
        notification.save();

        sender.sendMessage(CC.translate("&aSent out a notification to all players."));
        new NotificationsUpdatePacket(Flash.getInstance().getServerHandler().getNotifications()).send();
    }

}
