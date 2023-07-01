package dev.lbuddyboy.flash.command.user.note

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.menu.NotesMenu
import dev.lbuddyboy.flash.user.model.*
import dev.lbuddyboy.flash.user.packet.NoteAddPacket
import dev.lbuddyboy.flash.user.packet.NoteRemovePacket
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("note|notes")
@CommandPermission("flash.command.note")
object NotesCommand : BaseCommand() {
    @Subcommand("add|new|addnote|create")
    @CommandCompletion("@target")
    fun add(
        sender: CommandSender,
        @Name("target") target: UUID?,
        @Name("title") @Single title: String?,
        @Name("note") note: String
    ) {
        val noteObj = Note(
            UUID.randomUUID(),
            title,
            note,
            if (sender is Player) sender.uniqueId else null,
            System.currentTimeMillis()
        )
        val user: User = Flash.instance.userHandler.tryUser(target, true)
        user.getNotes().add(noteObj)
        user.save(true)
        NoteAddPacket(target, noteObj).send()
        sender.sendMessage(CC.translate("&aAdded the '" + note + "' note to " + user.coloredName + "&a."))
    }

    @Subcommand("remove|delnote|delete")
    @CommandCompletion("@target")
    fun remove(sender: CommandSender, @Name("target") target: UUID?, @Name("title") title: String) {
        val user: User = Flash.instance.userHandler.tryUser(target, true)
        val noteObj = user.getNote(title)
        if (noteObj == null) {
            sender.sendMessage(CC.translate("&cCould not find a note with that message"))
            return
        }
        noteObj.isRemoved = true
        noteObj.removedBy = if (sender is Player) sender.uniqueId else null
        noteObj.removedFor = "No reason specified"
        noteObj.removedAt = System.currentTimeMillis()
        user.save(true)
        NoteRemovePacket(target, noteObj).send()
        sender.sendMessage(CC.translate("&aRemoved the " + title + " note from " + user.coloredName + "&a."))
    }

    @Subcommand("list|info")
    @CommandCompletion("@target")
    fun list(sender: CommandSender, @Name("target") target: UUID) {
        val user: User = Flash.instance.userHandler.tryUser(target, true)
        if (sender !is Player) {
            sender.sendMessage(CC.translate(user.coloredName + "'s &7Notes"))
            for (note in user.sortedNotes) {
                sender.sendMessage(CC.translate(" &7&o(" + note!!.dateSentAt + ") &e" + note.title + "&7: &f" + note.message))
            }
            return
        }
        NotesMenu(target).openMenu(sender)
    }
}