package dev.lbuddyboy.flash.user.menu

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashMenuLanguage
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.listener.NoteListener
import dev.lbuddyboy.flash.user.model.*
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder
import dev.lbuddyboy.flash.util.bukkit.UserUtils
import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu
import lombok.AllArgsConstructor
import org.bukkit.DyeColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class NotesMenu(var uuid: UUID) : PagedMenu<Note?>() {
    init {
        objects = Flash.instance.userHandler.tryUser(uuid, true).getNotes()
    }

    override fun getPageTitle(player: Player?): String? {
        return translate(
            FlashMenuLanguage.NOTES_MENU_TITLE.string,
            "%PLAYER%", Flash.instance.cacheHandler.getUserCache().getName(uuid)
        )
    }

    override fun getPageButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        var i = 1
        val user: User = Flash.instance.userHandler.tryUser(
            uuid, true
        )
        for (note in user.sortedNotes) {
            buttons.add(NoteButton(uuid, note, i++))
        }
        return buttons
    }

    override fun autoUpdate(): Boolean {
        return true
    }

    override fun autoFill(): Boolean {
        return true
    }

    override fun getGlobalButtons(player: Player?): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(AddNoteButton(uuid))
        return buttons
    }

    @AllArgsConstructor
    private class AddNoteButton : Button() {
        var target: UUID? = null
        override fun getSlot(): Int {
            return 5
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(CompatibleMaterial.getMaterial("STAINED_CLAY"))
                .setDurability(DyeColor.GREEN.woolData.toShort())
                .setName("&aAdd Note &7(Click)")
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            player.closeInventory()
            NoteListener.Companion.noteTargetAddMap.put(player.name, target)
            player.sendMessage(CC.translate("&aType the title of the note you would like to add."))
        }
    }

    @AllArgsConstructor
    private class NoteButton : Button() {
        var target: UUID? = null
        var note: Note? = null
        private override val slot = 0
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return if (note.isRemoved()) {
                ItemBuilder(FlashMenuLanguage.NOTES_MENU_REMOVED_NOTE_BUTTON_MATERIAL.material)
                    .setDurability(FlashMenuLanguage.NOTES_MENU_REMOVED_NOTE_BUTTON_DATA.int)
                    .setName(
                        FlashMenuLanguage.NOTES_MENU_REMOVED_NOTE_BUTTON_NAME.string,
                        "%ADDEDAT%", note!!.dateSentAt,
                        "%ADDEDBY%", UserUtils.formattedName(note.getSender()),
                        "%ADDEDFOR%", note.getMessage(),
                        "%ADDEDTITLE%", note.getTitle(),
                        "%REMOVEDAT%", note!!.removedAtDate,
                        "%REMOVEDBY%", UserUtils.formattedName(note.getRemovedBy()),
                        "%REMOVEDFOR%", note.getRemovedFor()
                    )
                    .setLore(
                        FlashMenuLanguage.NOTES_MENU_REMOVED_NOTE_BUTTON_LORE.stringList,
                        "%ADDEDAT%", note!!.dateSentAt,
                        "%ADDEDBY%", UserUtils.formattedName(note.getSender()),
                        "%ADDEDFOR%", note.getMessage(),
                        "%ADDEDTITLE%", note.getTitle(),
                        "%REMOVEDAT%", note!!.removedAtDate,
                        "%REMOVEDBY%", UserUtils.formattedName(note.getRemovedBy()),
                        "%REMOVEDFOR%", note.getRemovedFor()
                    )
                    .create()
            } else ItemBuilder(FlashMenuLanguage.NOTES_MENU_DEFAULT_NOTE_BUTTON_MATERIAL.material)
                .setDurability(FlashMenuLanguage.NOTES_MENU_DEFAULT_NOTE_BUTTON_DATA.int)
                .setName(
                    FlashMenuLanguage.NOTES_MENU_DEFAULT_NOTE_BUTTON_NAME.string,
                    "%ADDEDAT%", note!!.dateSentAt,
                    "%ADDEDBY%", UserUtils.formattedName(note.getSender()),
                    "%ADDEDFOR%", note.getMessage(),
                    "%ADDEDTITLE%", note.getTitle()
                )
                .setLore(
                    FlashMenuLanguage.NOTES_MENU_DEFAULT_NOTE_BUTTON_LORE.stringList,
                    "%ADDEDAT%", note!!.dateSentAt,
                    "%ADDEDBY%", UserUtils.formattedName(note.getSender()),
                    "%ADDEDFOR%", note.getMessage(),
                    "%ADDEDTITLE%", note.getTitle()
                )
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            if (note.isRemoved()) {
                return
            }
            NoteListener.Companion.noteRemoveMap.put(player.name, note)
            NoteListener.Companion.noteTargetRemoveMap.put(player.name, target)
            player.closeInventory()
            player.sendMessage(CC.translate("&aType the reason for removing this note. Type 'cancel' to stop this process."))
        }
    }
}