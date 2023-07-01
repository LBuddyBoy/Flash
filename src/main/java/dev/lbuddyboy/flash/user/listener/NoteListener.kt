package dev.lbuddyboy.flash.user.listener

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.command.user.note.NotesCommand
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.menu.NotesMenu
import dev.lbuddyboy.flash.user.model.Note
import dev.lbuddyboy.flash.user.packet.NoteRemovePacket
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.Tasks
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.*

class NoteListener : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: AsyncPlayerChatEvent) {
        if (!noteRemoveMap.containsKey(event.player.name)) return
        event.isCancelled = true
        val note = noteRemoveMap.remove(event.player.name)
        val uuid = noteTargetRemoveMap.remove(event.player.name)
        if (event.message.equals("cancel", ignoreCase = true)) {
            Tasks.run { NotesMenu(uuid).openMenu(event.player) }
            return
        }
        val user: User = Flash.instance.userHandler.tryUser(uuid, true)
        note.setRemoved(true)
        note.setRemovedFor(event.message)
        note.setRemovedAt(System.currentTimeMillis())
        note.setRemovedBy(event.player.uniqueId)
        NoteRemovePacket(uuid, note).send()
        user.save(true)
        event.player.sendMessage(CC.translate("&aRemoved the " + note.getTitle() + " note from " + user.coloredName + "&a."))
        Tasks.run { NotesMenu(uuid).openMenu(event.player) }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChatAdd(event: AsyncPlayerChatEvent) {
        if (!noteTargetAddMap.containsKey(event.player.name)) return
        event.isCancelled = true
        var uuid = noteTargetAddMap[event.player.name]
        if (!noteTargetAddTitleMap.containsKey(event.player.name)) {
            if (event.message.equals("cancel", ignoreCase = true)) {
                val finalUuid = uuid
                Tasks.run { NotesMenu(finalUuid) }
                return
            }
            noteTargetAddTitleMap[event.player.name] = event.message
            event.player.sendMessage(CC.translate("&aNow, please type the note you would like to add."))
            return
        }
        uuid = noteTargetAddMap.remove(event.player.name)
        val title = noteTargetAddTitleMap.remove(event.player.name)
        if (event.message.equals("cancel", ignoreCase = true)) {
            val finalUuid1 = uuid
            Tasks.run { NotesMenu(finalUuid1).openMenu(event.player) }
            return
        }
        val user: User = Flash.instance.userHandler.tryUser(uuid, true)
        NotesCommand.add(event.player, uuid, title, event.message)
        event.player.sendMessage(CC.translate("&aAdded the " + title + " note from " + user.coloredName + "&a."))
        val finalUuid2 = uuid
        Tasks.run { NotesMenu(finalUuid2).openMenu(event.player) }
    }

    companion object {
        var noteRemoveMap: MutableMap<String, Note?> = HashMap()
        var noteTargetRemoveMap: MutableMap<String, UUID?> = HashMap()
        var noteTargetAddMap: MutableMap<String, UUID?> = HashMap()
        var noteTargetAddTitleMap: MutableMap<String, String> = HashMap()
    }
}