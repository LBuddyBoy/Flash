package dev.lbuddyboy.flash.command.server

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.server.menu.NotificationsEditorMenu
import dev.lbuddyboy.flash.server.menu.NotificationsMenu
import dev.lbuddyboy.flash.server.model.Notification
import dev.lbuddyboy.flash.server.packet.NotificationsUpdatePacket
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("notifications|notification|notifs|reminders|reminder")
object NotificationsCommand : BaseCommand() {
    @Default
    fun def(sender: Player) {
        NotificationsMenu("all", Flash.instance.userHandler.tryUser(sender.uniqueId, false)).openMenu(sender)
    }

    @Subcommand("editor|manager|manage")
    @CommandPermission("flash.command.notifications.edit")
    fun editor(sender: Player) {
        NotificationsEditorMenu(sender.uniqueId).openMenu(sender)
    }

    @Subcommand("create|add|send")
    @CommandPermission("flash.command.notifications.edit")
    fun create(sender: CommandSender, @Name("title") @Single title: String?, @Name("message") message: String?) {
        val notification: Notification = Flash.instance.serverHandler.createNotification(title, message)
        Flash.instance.serverHandler.getNotifications().add(notification)
        notification.save()
        sender.sendMessage(CC.translate("&aSent out a notification to all players."))
        NotificationsUpdatePacket(Flash.instance.serverHandler.getNotifications()).send()
    }
}