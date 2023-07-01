package dev.lbuddyboy.flash.rank.editor.listener

import dev.lbuddyboy.flash.rank.Rankimport

dev.lbuddyboy.flash.rank.editor.RankEditimport dev.lbuddyboy.flash.rank.editor.menu.InheritanceEditorMenuimport dev.lbuddyboy.flash.rank.editor.menu.PermissionEditorMenuimport dev.lbuddyboy.flash.rank.editor.menu.RankEditorMenuimport dev.lbuddyboy.flash.util.bukkit.CCimport dev.lbuddyboy.flash.util.bukkit.Tasksimport org.bukkit.entity.Playerimport org.bukkit.event.EventHandlerimport org.bukkit.event.Listenerimport org.bukkit.event.player.AsyncPlayerChatEvent
class RankEditorListener : Listener {
    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        if (!rankEditorMap.containsKey(player)) return
        event.isCancelled = true
        val edit = rankEditorMap.remove(player)
        val type = edit.getType()
        val rank = edit.getRank()
        if (event.message.equals("cancel", ignoreCase = true)) {
            player.sendMessage(CC.translate("&cEdit process cancelled."))
            Tasks.run { RankEditorMenu(rank).openMenu(player) }
            return
        }
        type.edit.call(player, rank, event.message)
        Tasks.run { RankEditorMenu(rank).openMenu(player) }
    }

    @EventHandler
    fun onPermChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        if (!rankPermissionEditMap.containsKey(player)) return
        event.isCancelled = true
        val rank = rankPermissionEditMap.remove(player)
        if (event.message.equals("cancel", ignoreCase = true)) {
            player.sendMessage(CC.translate("&cEdit process cancelled."))
            Tasks.run { PermissionEditorMenu(rank).openMenu(player) }
            return
        }
        rank.getPermissions().add(event.message)
        rank!!.save(true)
        Tasks.run { PermissionEditorMenu(rank).openMenu(player) }
    }

    @EventHandler
    fun onInheritChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        if (!rankInheritanceEditMap.containsKey(player)) return
        event.isCancelled = true
        val rank = rankInheritanceEditMap.remove(player)
        if (event.message.equals("cancel", ignoreCase = true)) {
            player.sendMessage(CC.translate("&cEdit process cancelled."))
            Tasks.run { InheritanceEditorMenu(rank).openMenu(player) }
            return
        }
        rank.getInheritance().add(event.message)
        rank!!.save(true)
        Tasks.run { InheritanceEditorMenu(rank).openMenu(player) }
    }

    companion object {
        var rankEditorMap: MutableMap<Player, RankEdit> = HashMap()
        var rankPermissionEditMap: MutableMap<Player, Rank?> = HashMap()
        var rankInheritanceEditMap: MutableMap<Player, Rank?> = HashMap()
    }
}