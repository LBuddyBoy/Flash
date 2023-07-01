package dev.lbuddyboy.flash.server.listener

import dev.lbuddyboy.flash.server.Server
import dev.lbuddyboy.flash.server.menu.ServersMenu
import dev.lbuddyboy.flash.server.packet.ServerCommandPacket
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.Tasks
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ServerListener : Listener {
    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        if (!serverCommandMap.containsKey(event.player.name)) return
        event.isCancelled = true
        val server = serverCommandMap.remove(event.player.name)
        if (event.message.equals("cancel", ignoreCase = true)) {
            Tasks.run { ServersMenu().openMenu(event.player) }
            return
        }
        event.player.sendMessage(CC.translate("&aSuccessfully sent /" + event.message + " to the " + server.getName() + " server."))
        ServerCommandPacket(server.getName(), event.message).send()
        Tasks.run { ServersMenu().openMenu(event.player) }
    }

    companion object {
        var serverCommandMap: MutableMap<String, Server?> = HashMap()
    }
}