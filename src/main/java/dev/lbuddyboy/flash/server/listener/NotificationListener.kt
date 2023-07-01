package dev.lbuddyboy.flash.server.listener

import dev.lbuddyboy.flash.command.server.NotificationsCommand
import dev.lbuddyboy.flash.server.menu.NotificationsEditorMenu
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.Tasks
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class NotificationListener : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onChatAdd(event: AsyncPlayerChatEvent) {
        if (!notificationsAdd.contains(event.player.name)) return
        event.isCancelled = true
        if (!notificationTargetAddTitleMap.containsKey(event.player.name)) {
            if (event.message.equals("cancel", ignoreCase = true)) {
                Tasks.run { NotificationsEditorMenu(event.player.uniqueId).openMenu(event.player) }
                return
            }
            notificationTargetAddTitleMap[event.player.name] = event.message
            event.player.sendMessage(CC.translate("&aNow, please type the message of the notification you would like to create."))
            return
        }
        val title = notificationTargetAddTitleMap.remove(event.player.name)
        notificationsAdd.remove(event.player.name)
        if (event.message.equals("cancel", ignoreCase = true)) {
            Tasks.run { NotificationsEditorMenu(event.player.uniqueId).openMenu(event.player) }
            return
        }
        NotificationsCommand.create(event.player, title, event.message)
        event.player.sendMessage(CC.translate("&aCreated the " + title + " notification with the message " + event.message + "&a."))
        Tasks.run { NotificationsEditorMenu(event.player.uniqueId).openMenu(event.player) }
    }

    companion object {
        var notificationsAdd: MutableList<String> = ArrayList()
        var notificationTargetAddTitleMap: MutableMap<String, String> = HashMap()
    }
}